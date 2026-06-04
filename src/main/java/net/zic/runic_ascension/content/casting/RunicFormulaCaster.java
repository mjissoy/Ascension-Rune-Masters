package net.zic.runic_ascension.content.casting;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.handlers.AscensionDamageHandler;
import net.zic.runic_ascension.content.RunicPathHelper;
import net.zic.runic_ascension.core.paths.RunicPaths;

import java.util.HashSet;
import java.util.List;

public final class RunicFormulaCaster {

    private RunicFormulaCaster() {
    }

    public static RunicCastingResult tryCast(LivingEntity caster, List<net.minecraft.resources.ResourceLocation> inputRunes) {
        return tryCast(caster, inputRunes, 0);
    }

    public static RunicCastingResult tryCast(
            LivingEntity caster,
            List<net.minecraft.resources.ResourceLocation> inputRunes,
            int selectedSuppressionRealm
    ) {
        if (caster == null) {
            return RunicCastingResult.failure("missing_caster");
        }

        if (!(caster.level() instanceof ServerLevel level)) {
            return RunicCastingResult.failure("not_server_level");
        }

        RunicFormulaEvaluation evaluation = estimate(caster, inputRunes, selectedSuppressionRealm);
        RunicFormula formula = evaluation.formula();
        RunicFormulaStats stats = evaluation.stats();
        RunicEffectProfile profile = evaluation.profile();

        if (formula == null || stats == null) {
            return RunicCastingResult.failure(evaluation.failureReason());
        }

        if (evaluation.shouldBacklashImmediately()) {
            applyFormulaBacklash(caster, formula, stats, evaluation.failureReason());
            return RunicCastingResult.failure(evaluation.failureReason(), true);
        }

        if (!evaluation.canCast()) {
            return RunicCastingResult.failure(evaluation.failureReason());
        }

        if (evaluation.isUnstable() && shouldUnstableFormulaCollapse(level, evaluation)) {
            applyFormulaBacklash(caster, formula, stats, "unstable_formula");
            return RunicCastingResult.failure("unstable_formula", true);
        }

        double qiCost = evaluation.qiCost();

        if (!tryConsumeFormulaQi(caster, qiCost)) {
            if (caster instanceof ServerPlayer player) {
                player.displayClientMessage(getBacklashMessage("not_enough_qi"), true);
            }

            return RunicCastingResult.failure("not_enough_qi");
        }

        switch (profile.archetype()) {
            case SELF -> castVeil(level, caster, formula, stats, profile);
            case AREA -> castPulse(level, caster, formula, stats, profile);
            case MARK -> castMark(level, caster, formula, stats, profile);
            case WALL -> castWall(level, caster, formula, stats, profile);
            case LINE -> castLine(level, caster, formula, stats, profile);
            case PROJECTILE, UNKNOWN -> castBolt(level, caster, formula, stats, profile);
        }

        if (evaluation.isUnstable() && caster instanceof ServerPlayer player) {
            player.displayClientMessage(getBacklashMessage("unstable_formula_cast"), true);
        }

        return RunicCastingResult.success(formula.getFormulaId());
    }

    public static RunicFormulaEvaluation estimate(
            LivingEntity caster,
            List<net.minecraft.resources.ResourceLocation> inputRunes,
            int selectedSuppressionRealm
    ) {
        RunicFormula formula = RunicFormulaParser.parse(inputRunes);
        RunicCastingContext context = RunicCastingContext.create(caster, formula, selectedSuppressionRealm);
        RunicFormulaStats stats = RunicFormulaScaling.calculate(formula, context);
        double qiCost = getFormulaQiCost(formula, stats);
        float stability = estimateStability(formula, stats);
        context = context.withEstimates(qiCost, stability, stats.backlashMultiplier());

        if (inputRunes == null || inputRunes.isEmpty()) {
            return new RunicFormulaEvaluation(
                    formula,
                    context,
                    stats,
                    RunicCastingState.INVALID,
                    "empty_sequence",
                    qiCost,
                    stability
            );
        }

        if (!formula.isValid()) {
            return new RunicFormulaEvaluation(
                    formula,
                    context,
                    stats,
                    RunicCastingState.INVALID,
                    "missing_core_runes",
                    qiCost,
                    stability
            );
        }

        RunicCastingResult validationResult = validateFormula(caster, formula, stats, context.actualRunicRealm());

        if (!validationResult.isSuccess()) {
            RunicCastingState state = stateForFailure(validationResult.getFailureReason(), stability);

            return new RunicFormulaEvaluation(
                    formula,
                    context,
                    stats,
                    state,
                    validationResult.getFailureReason(),
                    qiCost,
                    stability
            );
        }

        RunicCastingState state = stability < 0.80F
                ? RunicCastingState.UNSTABLE
                : RunicCastingState.VALID;

        return new RunicFormulaEvaluation(
                formula,
                context,
                stats,
                state,
                state == RunicCastingState.UNSTABLE ? "unstable_formula" : "",
                qiCost,
                stability
        );
    }

    private static float estimateStability(RunicFormula formula, RunicFormulaStats stats) {
        float stability = stats.stabilityMultiplier();

        if (formula.startsWithModifier()) {
            stability -= 0.18F;
        }

        return Math.max(0.05F, stability);
    }

    private static RunicCastingState stateForFailure(String reason, float stability) {
        return switch (reason) {
            case "formula_too_complex", "rune_beyond_comprehension", "too_many_sources", "too_many_intents", "too_many_forms" ->
                    RunicCastingState.OVERREACHED;
            case "unstable_modifiers", "unstable_opening_modifier" ->
                    stability >= 0.65F ? RunicCastingState.UNSTABLE : RunicCastingState.OVERREACHED;
            default -> RunicCastingState.INVALID;
        };
    }

    private static boolean shouldUnstableFormulaCollapse(ServerLevel level, RunicFormulaEvaluation evaluation) {
        // Valid grammar should usually produce an effect, even when the script is ugly.
        // Collapse is reserved for truly low-stability formulas instead of punishing
        // every creative-but-rough sequence with immediate backlash roulette.
        if (evaluation.stability() >= 0.65F) {
            return false;
        }

        float instability = Math.max(0.0F, 1.0F - evaluation.stability());
        float collapseChance = (instability - 0.25F) * 0.55F * evaluation.stats().backlashMultiplier();

        if (evaluation.profile().hasFlag("stable")) {
            collapseChance *= 0.55F;
        }

        if (evaluation.profile().hasFlag("violent")) {
            collapseChance *= 1.18F;
        }

        collapseChance = Math.min(0.50F, Math.max(0.0F, collapseChance));
        return level.random.nextFloat() < collapseChance;
    }

    private static void castVeil(ServerLevel level, LivingEntity caster, RunicFormula formula, RunicFormulaStats stats, RunicEffectProfile profile) {
        applyIntentToSelf(caster, formula, stats, profile);
        spawnSelfParticles(level, caster, particleFor(formula, profile));
    }

    private static void castPulse(ServerLevel level, LivingEntity caster, RunicFormula formula, RunicFormulaStats stats, RunicEffectProfile profile) {
        double range = (formula.hasModifier("heavy") ? 6.0D : 4.0D)
                * stats.rangeMultiplier()
                * profile.areaMultiplier();

        if (isSupportFormula(formula, profile)) {
            applyIntentToSelf(caster, formula, stats, profile);
        }

        List<LivingEntity> targets = level.getEntitiesOfClass(
                LivingEntity.class,
                caster.getBoundingBox().inflate(range),
                entity -> entity != caster && entity.isAlive()
        );

        for (LivingEntity target : targets) {
            if (isSupportFormula(formula, profile)) {
                if (isFriendlyTarget(caster, target)) {
                    applyIntentToTarget(caster, target, formula, stats, profile, 0.65F);
                }
            } else {
                applyIntentToTarget(caster, target, formula, stats, profile, 0.75F);
            }
        }

        spawnSelfParticles(level, caster, particleFor(formula, profile));
    }

    private static void castMark(ServerLevel level, LivingEntity caster, RunicFormula formula, RunicFormulaStats stats, RunicEffectProfile profile) {
        LivingEntity target = findLookedAtLivingEntity(caster, 8.0D * stats.rangeMultiplier());

        if (target == null) {
            applyIntentToSelf(caster, formula, stats, profile);
            spawnSelfParticles(level, caster, particleFor(formula, profile));
            return;
        }

        applyIntentToTarget(caster, target, formula, stats, profile, 0.8F);
        target.addEffect(new MobEffectInstance(MobEffects.GLOWING, 80, 0));
        spawnParticleLine(level, particleFor(formula, profile), caster.getEyePosition(), target.getBoundingBox().getCenter(), 10);
    }

    private static void castWall(ServerLevel level, LivingEntity caster, RunicFormula formula, RunicFormulaStats stats, RunicEffectProfile profile) {
        int duration = duration(formula, stats, profile);

        if (isSupportFormula(formula, profile) || formula.hasIntent("guard")) {
            caster.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, duration, formula.hasModifier("stabilise") ? 1 : 0));
            caster.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, duration, profile.isDefensive() ? 1 : 0));
        }

        double range = 3.5D * stats.rangeMultiplier() * Math.max(0.85D, profile.areaMultiplier());
        Vec3 start = caster.getEyePosition();
        Vec3 direction = caster.getViewVector(0.0F);
        AABB box = caster.getBoundingBox()
                .expandTowards(direction.scale(range))
                .inflate(1.0D + profile.areaMultiplier() * 0.45D);

        List<LivingEntity> targets = level.getEntitiesOfClass(
                LivingEntity.class,
                box,
                entity -> entity != caster && entity.isAlive()
        );

        for (LivingEntity target : targets) {
            if (isSupportFormula(formula, profile)) {
                if (isFriendlyTarget(caster, target)) {
                    applyIntentToTarget(caster, target, formula, stats, profile, 0.55F);
                }
            } else if (isControlFormula(formula, profile) || isOffensiveFormula(formula, profile)) {
                applyIntentToTarget(caster, target, formula, stats, profile, 0.55F);
            }
        }

        spawnParticleLine(level, particleFor(formula, profile), start, start.add(direction.scale(range)), 10);
        spawnSelfParticles(level, caster, particleFor(formula, profile));
    }

    private static void castLine(ServerLevel level, LivingEntity caster, RunicFormula formula, RunicFormulaStats stats, RunicEffectProfile profile) {
        Vec3 start = caster.getEyePosition();
        Vec3 direction = caster.getViewVector(0.0F);
        Vec3 end = start.add(direction.scale(12.0D * stats.rangeMultiplier()));

        AABB box = caster.getBoundingBox()
                .expandTowards(direction.scale(12.0D * stats.rangeMultiplier()))
                .inflate(Math.max(0.75D, profile.areaMultiplier()));

        List<LivingEntity> targets = level.getEntitiesOfClass(
                LivingEntity.class,
                box,
                entity -> entity != caster && entity.isAlive()
        );

        for (LivingEntity target : targets) {
            applyIntentToTarget(caster, target, formula, stats, profile, 0.65F);
        }

        spawnParticleLine(level, particleFor(formula, profile), start, end, 18);
    }

    private static void castBolt(ServerLevel level, LivingEntity caster, RunicFormula formula, RunicFormulaStats stats, RunicEffectProfile profile) {
        LivingEntity target = findLookedAtLivingEntity(caster, 10.0D * stats.rangeMultiplier() * profile.rangeMultiplier());

        if (target == null) {
            if (isSupportFormula(formula, profile)) {
                applyIntentToSelf(caster, formula, stats, profile);
                spawnSelfParticles(level, caster, particleFor(formula, profile));
            } else {
                spawnParticleLine(
                        level,
                        particleFor(formula, profile),
                        caster.getEyePosition(),
                        caster.getEyePosition().add(caster.getViewVector(0.0F).scale(6.0D * stats.rangeMultiplier())),
                        8
                );
            }
            return;
        }

        applyIntentToTarget(caster, target, formula, stats, profile, 1.0F);
        spawnParticleLine(level, particleFor(formula, profile), caster.getEyePosition(), target.getBoundingBox().getCenter(), 14);
    }

    private static void applyIntentToSelf(LivingEntity caster, RunicFormula formula, RunicFormulaStats stats, RunicEffectProfile profile) {
        int duration = duration(formula, stats, profile);

        switch (formula.intentPath()) {
            case "heal", "gather" -> {
                float healing = formula.sourcePath().equals("life") || formula.sourcePath().equals("water") ? 8.0F : 4.0F;
                caster.heal(healing * profile.healingMultiplier());
                caster.addEffect(new MobEffectInstance(MobEffects.REGENERATION, duration / 2, profile.healingMultiplier() > 1.35F ? 1 : 0));
            }
            case "guard", "bind", "compress" -> {
                caster.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, duration, profile.isDefensive() ? 1 : 0));
                if (profile.hasFlag("fortify") || profile.hasFlag("hardened")) {
                    caster.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, duration, 0));
                }
            }
            case "push", "release" -> caster.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, duration, formula.hasModifier("quicken") ? 1 : 0));
            case "pull" -> caster.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, duration / 2, 0));
            case "cut", "pierce" -> caster.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, duration, profile.hasFlag("violent") ? 1 : 0));
        }

        applySourceSelfBonus(caster, formula, stats, profile);
    }

    private static void applyIntentToTarget(LivingEntity caster, LivingEntity target, RunicFormula formula, RunicFormulaStats stats, RunicEffectProfile profile, float multiplier) {
        float damage = baseDamage(formula, stats, profile) * multiplier;
        int duration = duration(formula, stats, profile);

        switch (formula.intentPath()) {
            case "cut" -> hurtWithRunicDamage(caster, target, damage + 2.0F);
            case "pierce" -> {
                hurtWithRunicDamage(caster, target, damage + 4.0F);
                if (profile.hasFlag("armor_piercing")) {
                    target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, Math.max(40, duration / 2), 0));
                }
            }
            case "compress" -> {
                hurtWithRunicDamage(caster, target, damage + 1.0F);
                target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, duration / 2, 1));
                target.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, duration / 2, 0));
            }
            case "bind" -> {
                target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, duration, formula.hasModifier("heavy") ? 3 : 1));
                if (profile.hasFlag("rooting") || profile.hasFlag("root")) {
                    target.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, duration, 0));
                }
            }
            case "push" -> {
                if (profile.hasFlag("cutting_gale") || profile.hasFlag("storm")) {
                    hurtWithRunicDamage(caster, target, damage * 0.45F);
                }
                pushAway(caster, target, formula, profile);
            }
            case "pull" -> {
                pullToward(caster, target, formula, profile);
                if (profile.hasFlag("gravity") || profile.hasFlag("pressure")) {
                    target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, Math.max(40, duration / 3), 0));
                }
            }
            case "guard" -> {
                if (isFriendlyTarget(caster, target) && isPositiveSource(formula)) {
                    target.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, duration, 0));
                    target.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, duration, 0));
                } else {
                    target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, duration, 0));
                }
            }
            case "heal", "gather" -> {
                if (isPositiveSource(formula) && isFriendlyTarget(caster, target)) {
                    target.heal((3.0F + multiplier * 3.0F) * profile.healingMultiplier());
                    target.addEffect(new MobEffectInstance(MobEffects.REGENERATION, Math.max(40, duration / 3), 0));
                } else if (!isPositiveSource(formula)) {
                    hurtWithRunicDamage(caster, target, damage * 0.85F);
                }
            }
            case "release" -> {
                hurtWithRunicDamage(caster, target, damage + 1.0F);
                pushAway(caster, target, formula, profile);
            }
        }

        applySourceTargetBonus(caster, target, formula, stats, profile);
    }

    private static void applySourceSelfBonus(LivingEntity caster, RunicFormula formula, RunicFormulaStats stats, RunicEffectProfile profile) {
        int duration = duration(formula, stats, profile);

        switch (formula.sourcePath()) {
            case "flame" -> caster.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, duration, 0));
            case "water" -> caster.addEffect(new MobEffectInstance(MobEffects.REGENERATION, duration / 2, 0));
            case "wind" -> caster.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, duration, 0));
            case "earth", "metal" -> caster.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, duration, 0));
            case "light" -> caster.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, duration, 0));
            case "shadow" -> caster.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, duration / 2, 0));
            case "life", "wood" -> caster.heal(2.0F * profile.healingMultiplier());
        }

        applyProfileSelfSideEffects(caster, duration, profile);
    }

    private static void applySourceTargetBonus(LivingEntity caster, LivingEntity target, RunicFormula formula, RunicFormulaStats stats, RunicEffectProfile profile) {
        int duration = duration(formula, stats, profile);

        switch (formula.sourcePath()) {
            case "flame" -> target.igniteForSeconds((formula.hasModifier("violent") ? 6 : 3) + profile.fireSecondsBonus());
            case "frost" -> target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, duration, 1));
            case "lightning" -> target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, duration / 2, 0));
            case "light" -> target.addEffect(new MobEffectInstance(MobEffects.GLOWING, duration, 0));
            case "shadow", "hidden" -> target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, duration / 2, 0));
            case "decay" -> target.addEffect(new MobEffectInstance(MobEffects.WITHER, duration / 2, 0));
            case "life" -> caster.heal(1.5F);
            case "water" -> target.clearFire();
            case "wind" -> pushAway(caster, target, formula, profile);
        }

        applyProfileTargetSideEffects(caster, target, formula, duration, profile);
    }


    private static void applyProfileSelfSideEffects(LivingEntity caster, int duration, RunicEffectProfile profile) {
        if (profile.hasFlag("cleansing") || profile.hasFlag("extinguish")) {
            caster.clearFire();
        }

        if (profile.hasFlag("regenerative") || profile.hasFlag("mending")) {
            caster.addEffect(new MobEffectInstance(MobEffects.REGENERATION, Math.max(40, duration / 2), profile.hasFlag("mending") ? 1 : 0));
        }

        if (profile.hasFlag("swift") || profile.hasFlag("storm")) {
            caster.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, duration, profile.mobilityMultiplier() > 1.2F ? 1 : 0));
        }

        if (profile.hasFlag("defensive") || profile.hasFlag("barrier") || profile.hasFlag("grounded") || profile.hasFlag("fortify")) {
            caster.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, duration, profile.isDefensive() ? 1 : 0));
        }

        if (profile.hasFlag("fortify") || profile.hasFlag("hardened")) {
            caster.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, duration, 0));
        }

        if (profile.hasFlag("stealth") || profile.hasFlag("obscured")) {
            caster.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, Math.max(40, duration / 2), 0));
        }
    }

    private static void applyProfileTargetSideEffects(LivingEntity caster, LivingEntity target, RunicFormula formula, int duration, RunicEffectProfile profile) {
        if (profile.hasFlag("ignition") && profile.fireSecondsBonus() > 0 && !target.isOnFire()) {
            target.igniteForSeconds(profile.fireSecondsBonus());
        }

        if (profile.hasFlag("extinguish") || profile.hasFlag("cleansing")) {
            target.clearFire();
        }

        if (profile.hasFlag("slow") || profile.hasFlag("chill") || profile.hasFlag("restraint") || profile.hasFlag("root")) {
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, Math.max(40, duration / 2), profile.hasFlag("restraint") || profile.hasFlag("root") ? 1 : 0));
        }

        if (profile.hasFlag("root") || profile.hasFlag("pressure") || profile.hasFlag("stun")) {
            target.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, Math.max(40, duration / 2), profile.hasFlag("stun") ? 1 : 0));
        }

        if (profile.hasFlag("shock") || profile.hasFlag("weakening") || profile.hasFlag("storm")) {
            target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, Math.max(40, duration / 2), 0));
        }

        if (profile.hasFlag("blind") || profile.hasFlag("obscured") || profile.hasFlag("eclipse")) {
            target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, Math.max(30, duration / 3), 0));
        }

        if (profile.hasFlag("wither") || profile.hasFlag("corruptive") || profile.hasFlag("erosion")) {
            target.addEffect(new MobEffectInstance(MobEffects.WITHER, Math.max(40, duration / 3), profile.hasFlag("erosion") ? 1 : 0));
        }

        if (profile.hasFlag("reveal") || profile.hasFlag("mark")) {
            target.addEffect(new MobEffectInstance(MobEffects.GLOWING, Math.max(40, duration / 2), 0));
        }

        if (profile.hasFlag("secondary_wind") || profile.hasFlag("secondary_push") || profile.hasFlag("cutting_gale")) {
            pushAway(caster, target, formula, profile);
        }

        if (profile.hasFlag("chain_lightning") || profile.hasFlag("conductive")) {
            arcToNearbyTargets(caster, target, duration, profile);
        }
    }

    private static boolean isSupportFormula(RunicFormula formula, RunicEffectProfile profile) {
        return profile.isHealingFocused()
                || profile.hasFlag("support")
                || profile.hasFlag("mending")
                || formula.hasIntent("heal")
                || (formula.hasIntent("gather") && isPositiveSource(formula));
    }

    private static boolean isControlFormula(RunicFormula formula, RunicEffectProfile profile) {
        return formula.hasIntent("bind")
                || formula.hasIntent("push")
                || formula.hasIntent("pull")
                || formula.hasIntent("compress")
                || profile.hasFlag("slow")
                || profile.hasFlag("root")
                || profile.hasFlag("stun")
                || profile.hasFlag("restraint");
    }

    private static boolean isOffensiveFormula(RunicFormula formula, RunicEffectProfile profile) {
        return formula.hasIntent("cut")
                || formula.hasIntent("pierce")
                || formula.hasIntent("release")
                || !isPositiveSource(formula)
                || profile.hasFlag("slash")
                || profile.hasFlag("violent")
                || profile.hasFlag("corruptive");
    }

    private static boolean isFriendlyTarget(LivingEntity caster, LivingEntity target) {
        if (target == caster) {
            return true;
        }

        return caster instanceof Player && target instanceof Player;
    }

    private static void arcToNearbyTargets(LivingEntity caster, LivingEntity firstTarget, int duration, RunicEffectProfile profile) {
        if (!(firstTarget.level() instanceof ServerLevel level)) {
            return;
        }

        double range = profile.hasFlag("conductive") ? 3.0D : 2.25D;
        List<LivingEntity> nearby = level.getEntitiesOfClass(
                LivingEntity.class,
                firstTarget.getBoundingBox().inflate(range),
                entity -> entity != caster && entity != firstTarget && entity.isAlive()
        );

        int arcs = 0;
        for (LivingEntity secondary : nearby) {
            if (arcs >= 2) {
                break;
            }

            secondary.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, Math.max(30, duration / 3), 0));
            hurtWithRunicDamage(caster, secondary, 2.0F * profile.damageMultiplier());
            spawnParticleLine(level, ParticleTypes.ELECTRIC_SPARK, firstTarget.getBoundingBox().getCenter(), secondary.getBoundingBox().getCenter(), 6);
            arcs++;
        }
    }

    private static float baseDamage(RunicFormula formula, RunicFormulaStats stats, RunicEffectProfile profile) {
        float damage = 18.0F + formula.inputRunes().size() * 4.0F;

        if (formula.hasModifier("violent")) damage += 3.0F;
        if (formula.hasModifier("heavy")) damage += 1.5F;
        if (formula.hasModifier("stabilise")) damage -= 1.0F;

        return Math.max(1.0F, damage * stats.damageMultiplier());
    }

    private static int duration(RunicFormula formula, RunicFormulaStats stats, RunicEffectProfile profile) {
        int duration = 100;

        if (formula.hasModifier("quicken")) duration -= 30;
        if (formula.hasModifier("stabilise")) duration += 40;
        if (formula.hasModifier("heavy")) duration += 20;

        return Math.max(40, (int)(duration * stats.durationMultiplier()));
    }

    private static boolean isPositiveSource(RunicFormula formula) {
        return formula.sourcePath().equals("life")
                || formula.sourcePath().equals("water")
                || formula.sourcePath().equals("wood")
                || formula.sourcePath().equals("light");
    }

    private static void pushAway(LivingEntity caster, LivingEntity target, RunicFormula formula, RunicEffectProfile profile) {
        double strength = (formula.hasModifier("heavy") ? 1.2D : 0.7D) * profile.knockbackMultiplier();
        Vec3 direction = target.position().subtract(caster.position()).normalize();

        target.push(direction.x * strength, 0.2D, direction.z * strength);
        target.hurtMarked = true;
    }

    private static void pullToward(LivingEntity caster, LivingEntity target, RunicFormula formula, RunicEffectProfile profile) {
        double strength = (formula.hasModifier("heavy") ? 1.0D : 0.55D) * profile.knockbackMultiplier();
        Vec3 direction = caster.position().subtract(target.position()).normalize();

        target.push(direction.x * strength, 0.1D, direction.z * strength);
        target.hurtMarked = true;
    }

    private static ParticleOptions particleFor(RunicFormula formula, RunicEffectProfile profile) {
        if (formula.hasModifier("hidden")) {
            return ParticleTypes.POOF;
        }

        return switch (formula.sourcePath()) {
            case "flame" -> ParticleTypes.FLAME;
            case "water" -> ParticleTypes.SPLASH;
            case "wind" -> ParticleTypes.CLOUD;
            case "earth", "metal" -> ParticleTypes.CRIT;
            case "lightning" -> ParticleTypes.ELECTRIC_SPARK;
            case "frost" -> ParticleTypes.SNOWFLAKE;
            case "light" -> ParticleTypes.END_ROD;
            case "shadow" -> ParticleTypes.POOF;
            case "life", "wood" -> ParticleTypes.HAPPY_VILLAGER;
            case "decay" -> ParticleTypes.ASH;
            default -> ParticleTypes.ENCHANT;
        };
    }

    private static void spawnSelfParticles(ServerLevel level, LivingEntity caster, ParticleOptions particle) {
        level.sendParticles(
                particle,
                caster.getX(),
                caster.getY() + caster.getBbHeight() * 0.5D,
                caster.getZ(),
                24,
                0.45D,
                0.45D,
                0.45D,
                0.04D
        );
    }

    private static void spawnParticleLine(ServerLevel level, ParticleOptions particle, Vec3 start, Vec3 end, int steps) {
        Vec3 diff = end.subtract(start);

        for (int i = 0; i <= steps; i++) {
            double progress = i / (double) steps;
            Vec3 pos = start.add(diff.scale(progress));
            level.sendParticles(particle, pos.x, pos.y, pos.z, 1, 0.03D, 0.03D, 0.03D, 0.02D);
        }
    }

    private static LivingEntity findLookedAtLivingEntity(LivingEntity caster, double reach) {
        Vec3 eyePosition = caster.getEyePosition();
        Vec3 viewVector = caster.getViewVector(0.0F);
        Vec3 endPosition = eyePosition.add(viewVector.scale(reach));

        HitResult blockHit = caster.pick(reach, 0.0F, false);

        AABB searchBox = caster.getBoundingBox()
                .expandTowards(viewVector.scale(reach))
                .inflate(1.0D);

        EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(
                caster.level(),
                caster,
                eyePosition,
                endPosition,
                searchBox,
                entity -> isValidLookTarget(caster, entity)
        );

        if (entityHit == null) {
            return null;
        }

        if (blockHit.getType() != HitResult.Type.MISS) {
            double blockDistance = eyePosition.distanceToSqr(blockHit.getLocation());
            double entityDistance = eyePosition.distanceToSqr(entityHit.getLocation());

            if (blockDistance < entityDistance) {
                return null;
            }
        }

        Entity entity = entityHit.getEntity();
        return entity instanceof LivingEntity livingEntity ? livingEntity : null;
    }

    private static boolean isValidLookTarget(LivingEntity caster, Entity entity) {
        return entity instanceof LivingEntity
                && entity != caster
                && entity.isAlive()
                && !entity.isSpectator()
                && entity.isPickable();
    }

    private static void hurtWithRunicDamage(LivingEntity caster, LivingEntity target, float damage) {

        HashSet<ResourceLocation> paths = new HashSet<>();
        paths.add(RunicPaths.RUNIC.getId());

        AscensionDamageHandler.AscensionDamageSource source =
                new AscensionDamageHandler.AscensionDamageSource(paths,
                        target.damageSources().source(target.damageSources().magic().typeHolder().getKey(), caster)
                );

        target.hurt(source, damage);
    }

    private static void applyFormulaBacklash(
            LivingEntity caster,
            RunicFormula formula,
            RunicFormulaStats stats,
            String reason
    ) {
        if (caster == null || caster.level().isClientSide()) {
            return;
        }

        float damage = 3.0F + formula.inputRunes().size() * 2.0F;

        if (formula.hasModifier("violent")) {
            damage += 8.0F;
        }

        if (formula.hasModifier("heavy")) {
            damage += 3.0F;
        }

        if (formula.hasModifier("stabilise")) {
            damage *= 0.55F;
        }

        damage *= stats.backlashMultiplier();

        caster.hurt(caster.damageSources().magic(), Math.max(1.0F, damage));

        int duration = 40 + formula.inputRunes().size() * 10;

        caster.addEffect(new MobEffectInstance(MobEffects.CONFUSION, duration, 0));
        caster.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, duration / 2, 0));

        if (caster instanceof ServerPlayer player) {
            player.displayClientMessage(getBacklashMessage(reason), true);
        }
    }

    private static Component getBacklashMessage(String reason) {
        return switch (reason) {
            case "formula_too_complex" ->
                    Component.translatable("runic_ascension.runic.cast.formula_too_complex");
            case "rune_beyond_comprehension" ->
                    Component.translatable("runic_ascension.runic.cast.rune_beyond_comprehension");
            case "unstable_modifiers" ->
                    Component.translatable("runic_ascension.runic.cast.unstable_modifiers");
            case "missing_core_runes" ->
                    Component.translatable("runic_ascension.runic.cast.missing_core_runes");
            case "too_many_sources" ->
                    Component.translatable("runic_ascension.runic.cast.too_many_sources");
            case "too_many_intents" ->
                    Component.translatable("runic_ascension.runic.cast.too_many_intents");
            case "too_many_forms" ->
                    Component.translatable("runic_ascension.runic.cast.too_many_forms");
            case "unstable_opening_modifier" ->
                    Component.translatable("runic_ascension.runic.cast.unstable_opening_modifier");
            case "not_enough_qi" ->
                    Component.translatable("runic_ascension.runic.cast.not_enough_qi");
            case "unstable_formula" ->
                    Component.translatable("runic_ascension.runic.cast.unstable_formula");
            case "unstable_formula_cast" ->
                    Component.translatable("runic_ascension.runic.cast.unstable_formula_cast");
            default ->
                    Component.translatable("runic_ascension.runic.cast.runes_do_not_align");
        };
    }

    private static RunicCastingResult validateFormula(
            LivingEntity caster,
            RunicFormula formula,
            RunicFormulaStats stats,
            int runicRealm
    ) {
        if (!formula.isValid()) {
            return RunicCastingResult.failure("invalid_formula");
        }

        if (caster == null || !caster.hasData(ModAttachments.ENTITY_DATA)) {
            return RunicCastingResult.failure("missing_entity_data");
        }

        int availableSlots = RunicPathHelper.getRuneSlotCount(caster, true);

        if (formula.inputRunes().size() > availableSlots) {
            return RunicCastingResult.failure("formula_too_complex");
        }

        for (ResourceLocation runeId : formula.inputRunes()) {
            if (!RunicPathHelper.canUseRune(caster, runeId)) {
                return RunicCastingResult.failure("rune_beyond_comprehension");
            }
        }

        return RunicFormulaGrammar.validate(formula, runicRealm, availableSlots);
    }

    private static double getFormulaQiCost(RunicFormula formula, RunicFormulaStats stats) {
        double baseCost = 25.0D + formula.inputRunes().size() * 12.0D;

        switch (formula.formPath()) {
            case "pulse", "circle", "sphere" -> baseCost += 18.0D;
            case "line", "wall" -> baseCost += 12.0D;
            case "mark", "veil" -> baseCost += 8.0D;
        }

        switch (formula.intentPath()) {
            case "pierce", "compress", "release" -> baseCost += 15.0D;
            case "cut", "guard", "heal" -> baseCost += 10.0D;
            case "bind", "push", "pull", "gather" -> baseCost += 6.0D;
        }

        if (formula.hasModifier("violent")) baseCost += 30.0D;
        if (formula.hasModifier("heavy")) baseCost += 18.0D;
        if (formula.hasModifier("hidden")) baseCost += 15.0D;
        if (formula.hasModifier("quicken")) baseCost += 12.0D;
        if (formula.hasModifier("stabilise")) baseCost -= 8.0D;

        return Math.max(10.0D, baseCost * stats.qiCostMultiplier());
    }

    private static boolean tryConsumeFormulaQi(LivingEntity caster, double qiCost) {
        if (caster == null || !caster.hasData(ModAttachments.ENTITY_DATA)) {
            return false;
        }

        IEntityData entityData = caster.getData(ModAttachments.ENTITY_DATA);
        return entityData.getQiContainer().tryConsumeQi(qiCost);
    }



}