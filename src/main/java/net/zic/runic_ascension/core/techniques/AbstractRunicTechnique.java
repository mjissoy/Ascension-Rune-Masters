package net.zic.runic_ascension.core.techniques;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.techniques.ITechniqueData;
import net.thejadeproject.ascension.refactor_packages.techniques.custom.GenericTechnique;
import net.thejadeproject.ascension.refactor_packages.techniques.custom.stat_change_handlers.BasicStatChangeHandler;
import net.zic.runic_ascension.core.paths.RunicPaths;
import net.zic.runic_ascension.util.RunicTechniqueHelper;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public abstract class AbstractRunicTechnique extends GenericTechnique {

    private final String translationKey;
    private final Collection<ResourceLocation> grantedSkills;

    protected AbstractRunicTechnique(
            String translationKey,
            double cultivationSpeed,
            BasicStatChangeHandler statHandler,
            Collection<ResourceLocation> grantedSkills
    ) {
        super(
                RunicPaths.RUNIC.getId(),
                Component.translatable(translationKey),
                cultivationSpeed,
                Set.of()
        );

        this.translationKey = translationKey;
        this.grantedSkills = new LinkedHashSet<>(grantedSkills);
        this.setStatChangeHandler(statHandler);
    }

    @Override
    public Component getShortDescription() {
        return Component.translatable(translationKey + ".description.short");
    }

    @Override
    public Component getDescription() {
        return Component.translatable(translationKey + ".description");
    }

    @Override
    public void onTechniqueAdded(IEntityData heldEntity) {
        super.onTechniqueAdded(heldEntity);
        RunicTechniqueHelper.refresh(heldEntity, true, grantedSkills);
    }

    @Override
    public void onTechniqueRemoved(IEntityData heldEntity, ITechniqueData techniqueData) {
        super.onTechniqueRemoved(heldEntity, techniqueData);
        RunicTechniqueHelper.clear(heldEntity, grantedSkills);
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
        RunicTechniqueHelper.refresh(entityData, true, grantedSkills);
    }
}