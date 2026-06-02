package net.zic.runic_ascension.util;

import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.forms.forms.ModForms;
import net.zic.runic_ascension.core.paths.RunicPaths;
import net.zic.runic_ascension.core.skills.RunicSkills;

public final class RunicTechniqueSkillHelper {

    private RunicTechniqueSkillHelper() {
    }

    public static void refresh(IEntityData entityData, boolean shouldHaveRunicCastingSkill) {
        boolean shouldHave = shouldHaveRunicCastingSkill
                && entityData != null
                && entityData.hasPath(RunicPaths.RUNIC.getId());

        refreshSkill(
                entityData,
                RunicSkills.OPEN_RUNIC_CASTING.getId(),
                shouldHave
        );
        refreshSkill(
                entityData,
                RunicSkills.RUNIC_SIGHT.getId(),
                shouldHave
        );
    }

    public static void clear(IEntityData entityData) {
        refresh(entityData, false);
    }

    private static void refreshSkill(IEntityData entityData, ResourceLocation skillId, boolean shouldHave) {
        if (entityData == null) {
            return;
        }

        if (shouldHave) {
            if (!entityData.hasSkill(skillId)) {
                entityData.giveSkill(skillId, ModForms.MORTAL_VESSEL.getId());
            }

            return;
        }

        if (entityData.hasSkill(skillId)) {
            entityData.removeSkill(skillId, ModForms.MORTAL_VESSEL.getId());
        }
    }
}