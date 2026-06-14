package net.zic.runic_ascension.content.casting;

import net.minecraft.world.entity.LivingEntity;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.stats.Stat;
import net.thejadeproject.ascension.refactor_packages.stats.custom.ModStats;
import net.zic.runic_ascension.core.items.RunicBrushType;
import net.zic.runic_ascension.util.RunicInscriptionHelper;

public final class RunicFormulaScaling {

    private RunicFormulaScaling() {
    }

    public static RunicFormulaStats calculate(RunicFormula formula, LivingEntity caster, int runicRealm) {
        RunicCastingContext context = RunicCastingContext.create(caster, formula, runicRealm);
        return calculate(formula, context);
    }

    public static RunicFormulaStats calculate(RunicFormula formula, RunicCastingContext context) {
        LivingEntity caster = context.caster();
        int effectiveRealm = context.effectiveRunicRealm();
        int actualRealm = context.actualRunicRealm();
        RunicEffectProfile profile = RunicFormulaInterpreter.interpret(formula);

        float intelligence = getStat(caster, ModStats.INTELLIGENCE.get());
        float strength = getStat(caster, ModStats.STRENGTH.get());
        float agility = getStat(caster, ModStats.AGILITY.get());
        float vitality = getStat(caster, ModStats.VITALITY.get());

        float damage = 1.0F + (effectiveRealm * 0.35F);
        float duration = 1.0F + (effectiveRealm * 0.15F);
        float range = 1.0F + (effectiveRealm * 0.10F);
        float qiCost = 1.0F + (formula.inputRunes().size() * 0.22F) + (effectiveRealm * 0.04F);
        float backlash = 1.0F - (actualRealm * 0.035F);
        float stability = 1.0F + (actualRealm * 0.08F) + (effectiveRealm * 0.03F);

        damage += intelligence * 0.025F;

        damage += Math.max(0, formula.sourceCount() - 1) * 0.22F;
        damage += Math.max(0, formula.intentCount() - 1) * 0.16F;
        duration += Math.max(0, formula.formCount() - 1) * 0.12F;
        range += Math.max(0, formula.formCount() - 1) * 0.10F;

        qiCost += Math.max(0, formula.sourceCount() - 1) * 0.28F;
        qiCost += Math.max(0, formula.intentCount() - 1) * 0.22F;
        qiCost += Math.max(0, formula.formCount() - 1) * 0.18F;
        backlash += Math.max(0, formula.grammaticalWeight() - 4) * 0.04F;
        stability -= Math.max(0, formula.grammaticalWeight() - 4) * 0.025F;

        if (formula.hasIntent("cut") || formula.hasIntent("pierce")) {
            damage += strength * 0.012F;
        }

        if (formula.hasSource("wind")
                || formula.hasSource("lightning")
                || formula.hasModifier("quicken")) {
            damage += agility * 0.008F;
            range += agility * 0.003F;
        }

        if (formula.hasIntent("guard")
                || formula.hasIntent("heal")
                || formula.hasForm("veil")) {
            duration += vitality * 0.008F;
            stability += vitality * 0.002F;
        }

        if (formula.hasSource("flame") && formula.hasSource("wind")) {
            damage += 0.20F;
            range += 0.08F;
            stability -= 0.05F;
        }

        if (formula.hasSource("water") && formula.hasSource("frost")) {
            duration += 0.20F;
            backlash -= 0.05F;
            stability += 0.08F;
        }

        if (formula.hasSource("earth") && formula.hasSource("metal")) {
            duration += 0.18F;
            damage += 0.08F;
            stability += 0.12F;
        }

        if (formula.hasSource("life") && formula.hasSource("wood")) {
            duration += 0.25F;
            qiCost -= 0.10F;
            stability += 0.06F;
        }

        if (formula.hasSource("light") && formula.hasSource("shadow")) {
            damage += 0.25F;
            backlash += 0.20F;
            stability -= 0.22F;
        }

        if (formula.hasModifier("violent")) {
            damage += 0.42F;
            backlash += 0.42F;
            qiCost += 0.30F;
            stability -= 0.26F;
        }

        if (formula.hasModifier("heavy")) {
            duration += 0.25F;
            qiCost += 0.2F;
            stability -= 0.06F;
        }

        if (formula.hasModifier("quicken")) {
            duration -= 0.2F;
            qiCost += 0.1F;
            stability -= 0.05F;
        }

        if (formula.hasModifier("stabilise")) {
            backlash -= 0.4F;
            damage -= 0.10F;
            stability += 0.45F;
        }

        if (formula.hasModifier("hidden")) {
            damage -= 0.1F;
            qiCost += 0.15F;
            stability -= 0.06F;
        }

        if (context.isSuppressed()) {
            float suppressionGap = context.actualRunicRealm() - context.effectiveRunicRealm();
            qiCost -= suppressionGap * 0.055F;
            backlash -= suppressionGap * 0.025F;
            stability += suppressionGap * 0.045F;
        }

        damage *= profile.damageMultiplier();
        duration *= profile.durationMultiplier();
        range *= profile.rangeMultiplier();
        qiCost *= profile.qiCostMultiplier();
        backlash += profile.backlashModifier();
        stability += profile.stabilityModifier();
        stability += RunicInscriptionHelper.formulaStabilityBonus(caster);

        RunicFormulaStats stats = new RunicFormulaStats(
                Math.max(0.5F, damage),
                Math.max(0.5F, duration),
                Math.max(0.5F, range),
                Math.max(0.35F, qiCost),
                Math.max(0.2F, backlash),
                Math.max(0.05F, stability)
        );

        stats = applyBrush(stats, context.brushType(), profile);

        return context.masteryGrade().applyTo(stats);
    }

    private static RunicFormulaStats applyBrush(
            RunicFormulaStats stats,
            RunicBrushType brushType,
            RunicEffectProfile profile
    ) {
        if (brushType == null) {
            return stats;
        }

        float damage = stats.damageMultiplier() * brushType.damageMultiplier();
        float duration = stats.durationMultiplier() * brushType.durationMultiplier();
        float range = stats.rangeMultiplier() * brushType.rangeMultiplier();
        float qiCost = stats.qiCostMultiplier() * brushType.qiCostMultiplier();
        float backlash = stats.backlashMultiplier() * brushType.backlashMultiplier();
        float stability = stats.stabilityMultiplier() * brushType.stabilityMultiplier();

        if (brushType == RunicBrushType.EARTH && profile.isDefensive()) {
            duration *= 1.08F;
            stability *= 1.08F;
            backlash *= 0.92F;
        }

        if (brushType == RunicBrushType.HEAVEN && profile.isHealingFocused()) {
            damage *= 1.08F;
            qiCost *= 0.94F;
            stability *= 1.04F;
        }

        if (brushType == RunicBrushType.HELL && brushType.isPowerLeaning()) {
            if (profile.archetype() == RunicEffectArchetype.PROJECTILE
                    || profile.archetype() == RunicEffectArchetype.LINE
                    || profile.archetype() == RunicEffectArchetype.AREA) {
                damage *= 1.08F;
                backlash *= 1.08F;
                stability *= 0.96F;
            }
        }

        return new RunicFormulaStats(
                Math.max(0.5F, damage),
                Math.max(0.5F, duration),
                Math.max(0.5F, range),
                Math.max(0.35F, qiCost),
                Math.max(0.2F, backlash),
                Math.max(0.05F, stability)
        );
    }

    private static float getStat(LivingEntity entity, Stat stat) {
        if (entity == null || !entity.hasData(ModAttachments.ENTITY_DATA)) {
            return 0.0F;
        }

        IEntityData entityData = entity.getData(ModAttachments.ENTITY_DATA);

        if (entityData.getActiveFormData() == null || entityData.getActiveFormData().getStatSheet() == null) {
            return 0.0F;
        }

        var statInstance = entityData.getActiveFormData().getStatSheet().getStatInstance(stat);

        if (statInstance == null) {
            return 0.0F;
        }

        return (float) statInstance.getValue();
    }
}
