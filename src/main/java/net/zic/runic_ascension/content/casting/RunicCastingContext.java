package net.zic.runic_ascension.content.casting;

import net.minecraft.world.entity.LivingEntity;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.zic.runic_ascension.content.RunicPathHelper;
import net.zic.runic_ascension.content.RunicPlayerData;

public record RunicCastingContext(
        LivingEntity caster,
        IEntityData entityData,
        int actualRunicRealm,
        int effectiveRunicRealm,
        int selectedSuppressionRealm,
        int maxRuneSlots,
        RunicFormulaMasteryGrade masteryGrade,
        double qiCost,
        float stability,
        float backlashMultiplier
) {
    public static RunicCastingContext create(LivingEntity caster, RunicFormula formula, int selectedSuppressionRealm) {
        IEntityData entityData = caster != null && caster.hasData(ModAttachments.ENTITY_DATA)
                ? caster.getData(ModAttachments.ENTITY_DATA)
                : null;

        int actualRunicRealm = entityData == null ? 0 : RunicPathHelper.getRunicMajorRealm(entityData);
        int effectiveRunicRealm = clampSuppressionRealm(actualRunicRealm, selectedSuppressionRealm);
        int maxRuneSlots = caster == null ? 0 : RunicPathHelper.getRuneSlotCount(caster, true);
        RunicFormulaMasteryGrade masteryGrade = resolveMasteryGrade(caster, formula);

        return new RunicCastingContext(
                caster,
                entityData,
                actualRunicRealm,
                effectiveRunicRealm,
                effectiveRunicRealm,
                maxRuneSlots,
                masteryGrade,
                0.0D,
                1.0F,
                1.0F
        );
    }

    public RunicCastingContext withEstimates(double qiCost, float stability, float backlashMultiplier) {
        return new RunicCastingContext(
                caster,
                entityData,
                actualRunicRealm,
                effectiveRunicRealm,
                selectedSuppressionRealm,
                maxRuneSlots,
                masteryGrade,
                qiCost,
                stability,
                backlashMultiplier
        );
    }

    public boolean isSuppressed() {
        return actualRunicRealm > 0 && effectiveRunicRealm > 0 && effectiveRunicRealm < actualRunicRealm;
    }

    private static int clampSuppressionRealm(int actualRunicRealm, int selectedSuppressionRealm) {
        if (actualRunicRealm <= 0) {
            return 0;
        }

        if (selectedSuppressionRealm <= 0) {
            return actualRunicRealm;
        }

        return Math.max(1, Math.min(actualRunicRealm, selectedSuppressionRealm));
    }

    private static RunicFormulaMasteryGrade resolveMasteryGrade(LivingEntity caster, RunicFormula formula) {
        if (caster == null || formula == null) {
            return RunicFormulaMasteryGrade.UNSTABLE;
        }

        RunicPlayerData runicData = RunicPathHelper.getRunicData(caster);
        RunicDiscoveredFormula discoveredFormula = runicData.getDiscoveredFormula(formula.getFormulaId());

        return discoveredFormula == null
                ? RunicFormulaMasteryGrade.UNSTABLE
                : discoveredFormula.getMasteryGrade();
    }
}
