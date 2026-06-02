package net.zic.runic_ascension.core.techniques;

import net.minecraft.network.chat.Component;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.techniques.ITechniqueData;
import net.thejadeproject.ascension.refactor_packages.techniques.custom.GenericTechnique;
import net.zic.runic_ascension.core.paths.RunicPaths;
import net.zic.runic_ascension.util.RunicTechniqueSkillHelper;

import java.util.Set;

public class GeneralRunicTechnique extends GenericTechnique {

    public GeneralRunicTechnique() {
        super(
                RunicPaths.RUNIC.getId(),
                Component.translatable("runic_ascension.technique.runic_apprentice"),
                5.0D,
                Set.of()
        );
        this.setStatChangeHandler(RunicStatHandlers.BASIC_RUNIC_HANDLER);
    }

    @Override
    public Component getShortDescription() {
        return Component.translatable("runic_ascension.technique.runic_apprentice.description.short");
    }

    @Override
    public Component getDescription() {
        return Component.translatable("runic_ascension.technique.runic_apprentice.description");
    }

    @Override
    public void onTechniqueAdded(IEntityData heldEntity) {
        super.onTechniqueAdded(heldEntity);
        RunicTechniqueSkillHelper.refresh(heldEntity, true);
    }

    @Override
    public void onTechniqueRemoved(IEntityData heldEntity, ITechniqueData techniqueData) {
        super.onTechniqueRemoved(heldEntity, techniqueData);
        RunicTechniqueSkillHelper.clear(heldEntity);
    }

    @Override
    public void onRealmChange(
            IEntityData entityData,
            int oldMajorRealm,
            int oldMinorRealm,
            int newMajorRealm,
            int newMinorRealm
    ) {
        super.onRealmChange(entityData, oldMajorRealm, oldMinorRealm, newMajorRealm, newMinorRealm);
        RunicTechniqueSkillHelper.refresh(entityData, true);
    }
}
