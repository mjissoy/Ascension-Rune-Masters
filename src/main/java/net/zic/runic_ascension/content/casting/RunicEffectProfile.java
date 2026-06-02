package net.zic.runic_ascension.content.casting;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * A readable interpretation of a flexible runic formula.
 *
 * <p>{@link RunicFormulaStats} answers "how strong is it?". This profile answers
 * "what is the formula trying to be?" The numbers here are intentionally light
 * multipliers/modifiers so the interpreter can deepen rune identity without
 * hardcoding every possible sequence.</p>
 */
public record RunicEffectProfile(
        String sourcePath,
        String intentPath,
        String formPath,
        String displayName,
        String damageKind,
        RunicEffectArchetype archetype,
        float damageMultiplier,
        float healingMultiplier,
        float rangeMultiplier,
        float areaMultiplier,
        float durationMultiplier,
        float qiCostMultiplier,
        float stabilityModifier,
        float backlashModifier,
        float knockbackMultiplier,
        float mobilityMultiplier,
        int fireSecondsBonus,
        List<String> flags
) {
    public RunicEffectProfile {
        sourcePath = clean(sourcePath, "unknown");
        intentPath = clean(intentPath, "unknown");
        formPath = clean(formPath, "bolt");
        displayName = displayName == null || displayName.isBlank() ? "Unknown Formula" : displayName;
        damageKind = clean(damageKind, "runic");
        archetype = archetype == null ? RunicEffectArchetype.UNKNOWN : archetype;
        damageMultiplier = clampPositive(damageMultiplier);
        healingMultiplier = clampPositive(healingMultiplier);
        rangeMultiplier = clampPositive(rangeMultiplier);
        areaMultiplier = clampPositive(areaMultiplier);
        durationMultiplier = clampPositive(durationMultiplier);
        qiCostMultiplier = Math.max(0.15F, qiCostMultiplier);
        knockbackMultiplier = clampPositive(knockbackMultiplier);
        mobilityMultiplier = clampPositive(mobilityMultiplier);
        flags = flags == null ? List.of() : List.copyOf(flags);
    }

    public boolean hasFlag(String flag) {
        return flag != null && flags.contains(flag);
    }

    public boolean isHealingFocused() {
        return healingMultiplier > damageMultiplier || hasFlag("healing") || hasFlag("regenerative");
    }

    public boolean isDefensive() {
        return archetype == RunicEffectArchetype.WALL || hasFlag("defensive") || hasFlag("barrier");
    }

    public String flagsForDisplay() {
        return flags.isEmpty() ? "none" : String.join(", ", flags);
    }

    public static Builder builder(RunicFormula formula) {
        Builder builder = new Builder();

        if (formula != null) {
            builder.sourcePath(formula.sourcePath());
            builder.intentPath(formula.intentPath());
            builder.formPath(formula.formPath());
        }

        return builder;
    }

    private static String clean(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }

    private static float clampPositive(float value) {
        return Math.max(0.05F, value);
    }

    public static final class Builder {
        private String sourcePath = "unknown";
        private String intentPath = "unknown";
        private String formPath = "bolt";
        private String displayName = "Unknown Formula";
        private String damageKind = "runic";
        private RunicEffectArchetype archetype = RunicEffectArchetype.PROJECTILE;
        private float damageMultiplier = 1.0F;
        private float healingMultiplier = 1.0F;
        private float rangeMultiplier = 1.0F;
        private float areaMultiplier = 1.0F;
        private float durationMultiplier = 1.0F;
        private float qiCostMultiplier = 1.0F;
        private float stabilityModifier = 0.0F;
        private float backlashModifier = 0.0F;
        private float knockbackMultiplier = 1.0F;
        private float mobilityMultiplier = 1.0F;
        private int fireSecondsBonus = 0;
        private final Set<String> flags = new LinkedHashSet<>();

        public Builder sourcePath(String sourcePath) {
            this.sourcePath = clean(sourcePath, "unknown");
            return this;
        }

        public Builder intentPath(String intentPath) {
            this.intentPath = clean(intentPath, "unknown");
            return this;
        }

        public Builder formPath(String formPath) {
            this.formPath = clean(formPath, "bolt");
            return this;
        }

        public Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder damageKind(String damageKind) {
            this.damageKind = clean(damageKind, "runic");
            return this;
        }

        public Builder archetype(RunicEffectArchetype archetype) {
            this.archetype = archetype == null ? RunicEffectArchetype.UNKNOWN : archetype;
            return this;
        }

        public Builder damage(float multiplier) {
            this.damageMultiplier *= multiplier;
            return this;
        }

        public Builder healing(float multiplier) {
            this.healingMultiplier *= multiplier;
            return this;
        }

        public Builder range(float multiplier) {
            this.rangeMultiplier *= multiplier;
            return this;
        }

        public Builder area(float multiplier) {
            this.areaMultiplier *= multiplier;
            return this;
        }

        public Builder duration(float multiplier) {
            this.durationMultiplier *= multiplier;
            return this;
        }

        public Builder qiCost(float multiplier) {
            this.qiCostMultiplier *= multiplier;
            return this;
        }

        public Builder stability(float modifier) {
            this.stabilityModifier += modifier;
            return this;
        }

        public Builder backlash(float modifier) {
            this.backlashModifier += modifier;
            return this;
        }

        public Builder knockback(float multiplier) {
            this.knockbackMultiplier *= multiplier;
            return this;
        }

        public Builder mobility(float multiplier) {
            this.mobilityMultiplier *= multiplier;
            return this;
        }

        public Builder fireSeconds(int seconds) {
            this.fireSecondsBonus += seconds;
            return this;
        }

        public Builder flag(String flag) {
            if (flag != null && !flag.isBlank()) {
                flags.add(flag);
            }
            return this;
        }

        public RunicEffectProfile build() {
            return new RunicEffectProfile(
                    sourcePath,
                    intentPath,
                    formPath,
                    displayName,
                    damageKind,
                    archetype,
                    damageMultiplier,
                    healingMultiplier,
                    rangeMultiplier,
                    areaMultiplier,
                    durationMultiplier,
                    qiCostMultiplier,
                    stabilityModifier,
                    backlashModifier,
                    knockbackMultiplier,
                    mobilityMultiplier,
                    fireSecondsBonus,
                    new ArrayList<>(flags)
            );
        }
    }
}
