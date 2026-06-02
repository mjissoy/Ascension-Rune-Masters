package net.zic.runic_ascension.common.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.zic.runic_ascension.RunicAscension;
import java.util.concurrent.CompletableFuture;

public class RunicGlobalLootModifierProvider extends GlobalLootModifierProvider {

    public RunicGlobalLootModifierProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> registries
    ) {
        super(output, registries, RunicAscension.MOD_ID);
    }

    @Override
    protected void start() {
        addRunicTechniqueManuals();
        addRunicPhysiques();
        addRunicBloodlines();
    }

    private void addRunicTechniqueManuals() {

    }

    private void addRunicPhysiques() {

    }

    private void addRunicBloodlines() {

    }

}