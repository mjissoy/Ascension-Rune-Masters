package net.zic.runic_ascension.util;

import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.forms.forms.ModForms;
import net.zic.runic_ascension.core.paths.RunicPaths;
import net.zic.runic_ascension.core.skills.RunicSkills;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public final class RunicTechniqueHelper {

    private RunicTechniqueHelper() {}

    public static void refresh(IEntityData entityData, boolean shouldHaveRunicSkills, Collection<ResourceLocation> techniqueSkills) {
        boolean shouldHave = shouldHaveRunicSkills
                && entityData != null
                && entityData.hasPath(RunicPaths.RUNIC.getId());

        Set<ResourceLocation> skills = new LinkedHashSet<>();

        // Core Runic skills every Runic technique should have
        skills.add(RunicSkills.OPEN_RUNIC_CASTING.getId());
        skills.add(RunicSkills.RUNIC_SIGHT.getId());

        if (techniqueSkills != null) {
            skills.addAll(techniqueSkills);
        }

        for (ResourceLocation skillId : skills) {
            refreshSkill(entityData, skillId, shouldHave);
        }
    }

    public static void clear(IEntityData entityData, Collection<ResourceLocation> techniqueSkills) {
        refresh(entityData, false, techniqueSkills);
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