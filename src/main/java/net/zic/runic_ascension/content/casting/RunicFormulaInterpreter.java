package net.zic.runic_ascension.content.casting;

import java.util.List;

/**
 * Interprets flexible formula grammar into a reusable effect profile.
 *
 * <p>This is the first layer of the "runes as language" approach: source runes
 * provide nature, intent runes provide action, form runes provide delivery, and
 * modifier runes bend cost, stability, strength, risk, and side effects.</p>
 */
public final class RunicFormulaInterpreter {

    private RunicFormulaInterpreter() {
    }

    public static RunicEffectProfile interpret(RunicFormula formula) {
        RunicEffectProfile.Builder builder = RunicEffectProfile.builder(formula);

        if (formula == null || !formula.isValid()) {
            return builder
                    .displayName("Unformed Formula")
                    .archetype(RunicEffectArchetype.UNKNOWN)
                    .damage(0.5F)
                    .healing(0.5F)
                    .stability(-0.20F)
                    .flag("unformed")
                    .build();
        }

        applyPrimaryForm(builder, formula.formPath());
        applyPrimarySource(builder, formula.sourcePath());
        applyPrimaryIntent(builder, formula.intentPath());

        applySecondarySources(builder, formula.sourcePaths());
        applySecondaryIntents(builder, formula.intentPaths());
        applySecondaryForms(builder, formula.formPaths());
        applyModifiers(builder, formula.modifierPaths());
        applyCrossRuneSynergies(builder, formula);

        builder.displayName(buildDisplayName(formula));
        return builder.build();
    }

    private static void applyPrimarySource(RunicEffectProfile.Builder builder, String source) {
        switch (source) {
            case "flame" -> builder
                    .damageKind("fire")
                    .damage(1.18F)
                    .qiCost(1.05F)
                    .stability(-0.08F)
                    .backlash(0.07F)
                    .fireSeconds(3)
                    .flag("fire")
                    .flag("ignition");
            case "water" -> builder
                    .damageKind("water")
                    .damage(0.95F)
                    .healing(1.15F)
                    .duration(1.08F)
                    .stability(0.08F)
                    .flag("extinguish")
                    .flag("cleansing");
            case "wind" -> builder
                    .damageKind("wind")
                    .damage(0.92F)
                    .range(1.22F)
                    .area(1.08F)
                    .qiCost(0.97F)
                    .knockback(1.20F)
                    .mobility(1.18F)
                    .flag("knockback")
                    .flag("swift");
            case "earth" -> builder
                    .damageKind("earth")
                    .duration(1.18F)
                    .range(0.88F)
                    .stability(0.18F)
                    .backlash(-0.08F)
                    .flag("grounded")
                    .flag("barrier");
            case "wood" -> builder
                    .damageKind("wood")
                    .healing(1.20F)
                    .duration(1.15F)
                    .stability(0.08F)
                    .flag("regenerative")
                    .flag("rooting");
            case "metal" -> builder
                    .damageKind("metal")
                    .damage(1.12F)
                    .duration(1.10F)
                    .range(0.95F)
                    .stability(0.12F)
                    .flag("hardened")
                    .flag("armor_piercing");
            case "lightning" -> builder
                    .damageKind("lightning")
                    .damage(1.23F)
                    .range(1.13F)
                    .qiCost(1.12F)
                    .stability(-0.13F)
                    .backlash(0.12F)
                    .flag("shock")
                    .flag("weakening");
            case "frost" -> builder
                    .damageKind("frost")
                    .damage(1.05F)
                    .duration(1.20F)
                    .stability(0.05F)
                    .flag("chill")
                    .flag("slow");
            case "light" -> builder
                    .damageKind("light")
                    .damage(1.07F)
                    .healing(1.10F)
                    .qiCost(1.05F)
                    .flag("reveal")
                    .flag("cleansing");
            case "shadow" -> builder
                    .damageKind("shadow")
                    .damage(1.05F)
                    .range(1.05F)
                    .duration(1.05F)
                    .stability(-0.08F)
                    .backlash(0.06F)
                    .flag("blind")
                    .flag("stealth");
            case "life" -> builder
                    .damageKind("life")
                    .damage(0.78F)
                    .healing(1.45F)
                    .qiCost(1.25F)
                    .stability(0.04F)
                    .flag("healing")
                    .flag("regenerative")
                    .flag("cleansing");
            case "decay" -> builder
                    .damageKind("decay")
                    .damage(1.20F)
                    .healing(0.55F)
                    .qiCost(1.12F)
                    .stability(-0.15F)
                    .backlash(0.18F)
                    .flag("wither")
                    .flag("corruptive");
            default -> builder.flag("untyped_source");
        }
    }

    private static void applyPrimaryIntent(RunicEffectProfile.Builder builder, String intent) {
        switch (intent) {
            case "bind" -> builder
                    .damage(0.65F)
                    .duration(1.25F)
                    .stability(0.05F)
                    .flag("restraint")
                    .flag("slow");
            case "push" -> builder
                    .damage(0.80F)
                    .range(1.10F)
                    .knockback(1.35F)
                    .flag("knockback");
            case "pull" -> builder
                    .damage(0.75F)
                    .range(1.05F)
                    .knockback(1.10F)
                    .flag("pull");
            case "guard" -> builder
                    .damage(0.55F)
                    .duration(1.20F)
                    .stability(0.15F)
                    .backlash(-0.05F)
                    .flag("defensive");
            case "cut" -> builder
                    .damage(1.18F)
                    .qiCost(1.04F)
                    .flag("slash");
            case "heal" -> builder
                    .damage(0.50F)
                    .healing(1.45F)
                    .qiCost(1.15F)
                    .stability(0.05F)
                    .flag("healing")
                    .flag("support");
            case "gather" -> builder
                    .damage(0.65F)
                    .healing(1.10F)
                    .duration(1.15F)
                    .qiCost(0.92F)
                    .stability(0.10F)
                    .flag("gathering")
                    .flag("sustain");
            case "release" -> builder
                    .damage(1.12F)
                    .range(1.10F)
                    .qiCost(1.08F)
                    .stability(-0.05F)
                    .flag("burst");
            case "pierce" -> builder
                    .damage(1.35F)
                    .range(1.05F)
                    .qiCost(1.12F)
                    .stability(-0.04F)
                    .flag("armor_piercing");
            case "compress" -> builder
                    .damage(1.18F)
                    .duration(1.10F)
                    .area(0.88F)
                    .qiCost(1.10F)
                    .stability(-0.02F)
                    .flag("pressure")
                    .flag("slow");
            default -> builder.flag("untyped_intent");
        }
    }

    private static void applyPrimaryForm(RunicEffectProfile.Builder builder, String form) {
        switch (form) {
            case "bolt" -> builder
                    .archetype(RunicEffectArchetype.PROJECTILE)
                    .damage(1.05F)
                    .range(1.10F)
                    .area(0.75F)
                    .flag("single_target");
            case "line" -> builder
                    .archetype(RunicEffectArchetype.LINE)
                    .damage(1.02F)
                    .range(1.18F)
                    .area(0.90F)
                    .flag("line");
            case "circle" -> builder
                    .archetype(RunicEffectArchetype.AREA)
                    .damage(0.85F)
                    .area(1.35F)
                    .qiCost(1.12F)
                    .flag("area");
            case "pulse" -> builder
                    .archetype(RunicEffectArchetype.AREA)
                    .damage(0.95F)
                    .area(1.20F)
                    .range(0.95F)
                    .qiCost(1.10F)
                    .flag("burst");
            case "sphere" -> builder
                    .archetype(RunicEffectArchetype.AREA)
                    .damage(0.82F)
                    .area(1.55F)
                    .qiCost(1.20F)
                    .stability(-0.04F)
                    .flag("wide_area");
            case "wall" -> builder
                    .archetype(RunicEffectArchetype.WALL)
                    .damage(0.60F)
                    .duration(1.25F)
                    .stability(0.12F)
                    .flag("barrier")
                    .flag("defensive");
            case "veil" -> builder
                    .archetype(RunicEffectArchetype.SELF)
                    .damage(0.55F)
                    .healing(1.10F)
                    .duration(1.20F)
                    .stability(0.08F)
                    .flag("self_buff");
            case "mark" -> builder
                    .archetype(RunicEffectArchetype.MARK)
                    .damage(0.90F)
                    .duration(1.20F)
                    .qiCost(0.94F)
                    .flag("mark");
            default -> builder
                    .archetype(RunicEffectArchetype.PROJECTILE)
                    .flag("default_bolt_shape");
        }
    }

    private static void applyModifiers(RunicEffectProfile.Builder builder, List<String> modifiers) {
        for (String modifier : modifiers) {
            switch (modifier) {
                case "quicken" -> builder
                        .duration(0.75F)
                        .range(1.10F)
                        .qiCost(1.08F)
                        .stability(-0.05F)
                        .mobility(1.25F)
                        .flag("fast");
                case "stabilise" -> builder
                        .damage(0.85F)
                        .qiCost(0.95F)
                        .stability(0.30F)
                        .backlash(-0.25F)
                        .flag("stable");
                case "heavy" -> builder
                        .damage(1.12F)
                        .duration(1.20F)
                        .range(0.90F)
                        .qiCost(1.15F)
                        .stability(-0.08F)
                        .knockback(1.25F)
                        .flag("weight");
                case "violent" -> builder
                        .damage(1.32F)
                        .qiCost(1.20F)
                        .stability(-0.20F)
                        .backlash(0.28F)
                        .flag("violent");
                case "hidden" -> builder
                        .damage(0.90F)
                        .range(1.05F)
                        .duration(1.08F)
                        .qiCost(1.12F)
                        .stability(-0.08F)
                        .flag("stealth")
                        .flag("obscured");
                default -> builder.flag("unknown_modifier");
            }
        }
    }

    private static void applySecondarySources(RunicEffectProfile.Builder builder, List<String> sources) {
        for (int i = 1; i < sources.size(); i++) {
            switch (sources.get(i)) {
                case "flame" -> builder.damage(1.06F).stability(-0.02F).fireSeconds(1).flag("secondary_fire");
                case "water" -> builder.healing(1.06F).stability(0.03F).flag("secondary_cleansing");
                case "wind" -> builder.range(1.07F).knockback(1.08F).flag("secondary_wind");
                case "earth" -> builder.duration(1.07F).stability(0.06F).flag("secondary_grounding");
                case "wood" -> builder.healing(1.08F).duration(1.05F).flag("secondary_regrowth");
                case "metal" -> builder.damage(1.05F).stability(0.04F).flag("secondary_hardening");
                case "lightning" -> builder.damage(1.07F).range(1.04F).stability(-0.04F).backlash(0.04F).flag("secondary_shock");
                case "frost" -> builder.duration(1.06F).flag("secondary_chill");
                case "light" -> builder.healing(1.04F).flag("secondary_reveal");
                case "shadow" -> builder.range(1.04F).stability(-0.03F).flag("secondary_shadow");
                case "life" -> builder.healing(1.10F).qiCost(1.04F).flag("secondary_life");
                case "decay" -> builder.damage(1.08F).stability(-0.05F).backlash(0.05F).flag("secondary_decay");
            }
        }
    }

    private static void applySecondaryIntents(RunicEffectProfile.Builder builder, List<String> intents) {
        for (int i = 1; i < intents.size(); i++) {
            switch (intents.get(i)) {
                case "bind" -> builder.duration(1.07F).flag("secondary_bind").flag("slow");
                case "push" -> builder.knockback(1.12F).flag("secondary_push");
                case "pull" -> builder.knockback(1.08F).flag("secondary_pull");
                case "guard" -> builder.stability(0.06F).damage(0.96F).flag("secondary_guard");
                case "cut" -> builder.damage(1.07F).flag("secondary_cut");
                case "heal" -> builder.healing(1.12F).damage(0.95F).flag("secondary_heal");
                case "gather" -> builder.qiCost(0.96F).stability(0.04F).flag("secondary_gather");
                case "release" -> builder.damage(1.06F).range(1.04F).stability(-0.02F).flag("secondary_release");
                case "pierce" -> builder.damage(1.08F).flag("secondary_pierce").flag("armor_piercing");
                case "compress" -> builder.damage(1.06F).duration(1.04F).flag("secondary_compress");
            }
        }
    }

    private static void applySecondaryForms(RunicEffectProfile.Builder builder, List<String> forms) {
        for (int i = 1; i < forms.size(); i++) {
            switch (forms.get(i)) {
                case "bolt" -> builder.range(1.04F).area(0.95F).flag("secondary_bolt");
                case "line" -> builder.range(1.06F).flag("secondary_line");
                case "circle" -> builder.area(1.08F).qiCost(1.04F).flag("secondary_circle");
                case "pulse" -> builder.area(1.06F).qiCost(1.03F).flag("secondary_pulse");
                case "sphere" -> builder.area(1.12F).qiCost(1.06F).stability(-0.02F).flag("secondary_sphere");
                case "wall" -> builder.duration(1.08F).stability(0.05F).flag("secondary_wall");
                case "veil" -> builder.duration(1.06F).stability(0.04F).flag("secondary_veil");
                case "mark" -> builder.duration(1.05F).qiCost(0.98F).flag("secondary_mark");
            }
        }
    }

    private static void applyCrossRuneSynergies(RunicEffectProfile.Builder builder, RunicFormula formula) {
        // These are not new runes. They are the first layer of "meaning between runes":
        // the same symbol changes character when it stands beside another symbol.
        if (formula.hasSource("flame") && formula.hasSource("wind")) {
            builder.damage(1.08F).range(1.08F).area(1.06F).stability(-0.04F).fireSeconds(1)
                    .flag("spreading_fire")
                    .flag("cutting_gale");
        }

        if (formula.hasSource("water") && formula.hasSource("frost")) {
            builder.duration(1.12F).stability(0.08F).backlash(-0.05F)
                    .flag("freezing")
                    .flag("root");
        }

        if (formula.hasSource("earth") && formula.hasSource("metal")) {
            builder.damage(1.04F).duration(1.10F).stability(0.12F).backlash(-0.06F)
                    .flag("fortify")
                    .flag("hardened");
        }

        if (formula.hasSource("life") && formula.hasSource("wood")) {
            builder.healing(1.16F).duration(1.10F).qiCost(0.96F).stability(0.06F)
                    .flag("mending")
                    .flag("regenerative");
        }

        if (formula.hasSource("light") && formula.hasSource("shadow")) {
            builder.damage(1.12F).range(1.05F).stability(-0.14F).backlash(0.12F)
                    .flag("eclipse")
                    .flag("reveal")
                    .flag("blind");
        }

        if (formula.hasSource("shadow") && formula.hasSource("decay")) {
            builder.damage(1.12F).duration(1.08F).stability(-0.06F).backlash(0.06F)
                    .flag("erosion")
                    .flag("corruptive");
        }

        if (formula.hasSource("lightning") && formula.hasSource("metal")) {
            builder.damage(1.10F).range(1.08F).qiCost(1.04F).stability(-0.03F)
                    .flag("conductive")
                    .flag("chain_lightning");
        }

        if (formula.hasSource("lightning") && formula.hasSource("wind")) {
            builder.damage(1.08F).range(1.12F).knockback(1.08F).stability(-0.05F)
                    .flag("storm")
                    .flag("stun");
        }

        if (formula.hasSource("water") && formula.hasSource("lightning")) {
            builder.area(1.08F).qiCost(1.06F).stability(-0.04F)
                    .flag("conductive")
                    .flag("shock");
        }

        if (formula.hasSource("flame") && formula.hasSource("earth")) {
            builder.damage(1.06F).duration(1.06F).stability(0.02F)
                    .flag("molten")
                    .flag("pressure");
        }

        if (formula.hasSource("flame") && formula.hasIntent("cut")) {
            builder.damage(1.05F).fireSeconds(1).flag("searing_edge");
        }

        if (formula.hasSource("frost") && formula.hasIntent("bind")) {
            builder.duration(1.12F).stability(0.04F).flag("root").flag("freezing");
        }

        if (formula.hasSource("earth") && formula.hasIntent("guard")) {
            builder.duration(1.12F).stability(0.10F).backlash(-0.05F).flag("fortify");
        }

        if (formula.hasSource("wind") && (formula.hasIntent("push") || formula.hasIntent("pull"))) {
            builder.range(1.10F).knockback(1.18F).qiCost(0.96F).flag("cutting_gale");
        }

        if (formula.hasSource("lightning") && formula.hasIntent("pierce")) {
            builder.damage(1.10F).range(1.06F).stability(-0.02F).flag("stun").flag("chain_lightning");
        }

        if ((formula.hasSource("life") || formula.hasSource("water") || formula.hasSource("wood")) && formula.hasIntent("heal")) {
            builder.healing(1.14F).stability(0.06F).flag("mending");
        }

        if ((formula.hasSource("decay") || formula.hasSource("shadow")) && formula.hasIntent("compress")) {
            builder.damage(1.08F).duration(1.08F).flag("erosion").flag("pressure");
        }

        if (formula.hasSource("light") && formula.hasForm("mark")) {
            builder.range(1.06F).qiCost(0.96F).flag("reveal");
        }

        if (formula.hasModifier("hidden") && formula.hasSource("shadow")) {
            builder.stability(0.04F).qiCost(0.96F).flag("obscured").flag("eclipse");
        }

        if (formula.hasModifier("stabilise") && (formula.hasSource("earth") || formula.hasSource("water") || formula.hasIntent("guard"))) {
            builder.stability(0.12F).backlash(-0.08F).flag("anchored_script");
        }

        if (formula.hasModifier("violent") && formula.hasModifier("stabilise")) {
            builder.stability(0.10F).backlash(-0.08F).flag("leashed_violence");
        }
    }

    private static String buildDisplayName(RunicFormula formula) {
        String source = title(formula.sourcePath());
        String intent = title(formula.intentPath());
        String form = formula.form() == null ? "Formula" : title(formula.formPath());

        if (formula.form() == null) {
            return source + " " + intent + " Formula";
        }

        return source + " " + intent + " " + form;
    }

    private static String title(String path) {
        if (path == null || path.isBlank()) {
            return "Unknown";
        }

        String[] parts = path.split("_");
        StringBuilder builder = new StringBuilder();

        for (String part : parts) {
            if (part.isBlank()) {
                continue;
            }

            if (builder.length() > 0) {
                builder.append(' ');
            }

            builder.append(Character.toUpperCase(part.charAt(0)));

            if (part.length() > 1) {
                builder.append(part.substring(1));
            }
        }

        return builder.length() == 0 ? "Unknown" : builder.toString();
    }
}
