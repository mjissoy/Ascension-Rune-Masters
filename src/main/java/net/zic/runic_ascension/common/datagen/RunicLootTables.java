package net.zic.runic_ascension.common.datagen;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.zic.runic_ascension.RunicAscension;

public final class RunicLootTables {

    public static final ResourceKey<LootTable> STARTER_DISCOVERY_COMMON =
            create("inject/starter_discovery_common");

    public static final ResourceKey<LootTable> STARTER_DISCOVERY_LIBRARY =
            create("inject/starter_discovery_library");

    public static final ResourceKey<LootTable> RUNIC_UTILITY_RARE =
            create("inject/runic_utility_rare");

    private RunicLootTables() {
    }

    private static ResourceKey<LootTable> create(String path) {
        return ResourceKey.create(
                Registries.LOOT_TABLE,
                ResourceLocation.fromNamespaceAndPath(RunicAscension.MOD_ID, path)
        );
    }
}