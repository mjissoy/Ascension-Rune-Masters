package net.zic.runic_ascension.common.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.AddTableLootModifier;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;
import net.zic.runic_ascension.RunicAscension;

import java.util.concurrent.CompletableFuture;

public class RunicGlobalLootModifierProvider extends GlobalLootModifierProvider {

    public RunicGlobalLootModifierProvider(
            net.minecraft.data.PackOutput output,
            CompletableFuture<HolderLookup.Provider> registries
    ) {
        super(output, registries, RunicAscension.MOD_ID);
    }

    @Override
    protected void start() {
        addRunicStarterDiscoveryLoot();

        addRunicTechniqueManuals();
        addRunicPhysiques();
        addRunicBloodlines();
    }

    private void addRunicStarterDiscoveryLoot() {
        addChestInjection(
                "add_runic_starter_discovery_to_simple_dungeon",
                BuiltInLootTables.SIMPLE_DUNGEON,
                RunicLootTables.STARTER_DISCOVERY_COMMON,
                0.18f
        );

        addChestInjection(
                "add_runic_starter_discovery_to_abandoned_mineshaft",
                BuiltInLootTables.ABANDONED_MINESHAFT,
                RunicLootTables.STARTER_DISCOVERY_COMMON,
                0.16f
        );

        addChestInjection(
                "add_runic_starter_discovery_to_desert_pyramid",
                BuiltInLootTables.DESERT_PYRAMID,
                RunicLootTables.STARTER_DISCOVERY_COMMON,
                0.20f
        );

        addChestInjection(
                "add_runic_starter_discovery_to_jungle_temple",
                BuiltInLootTables.JUNGLE_TEMPLE,
                RunicLootTables.STARTER_DISCOVERY_COMMON,
                0.20f
        );

        addChestInjection(
                "add_runic_starter_discovery_to_stronghold_library",
                BuiltInLootTables.STRONGHOLD_LIBRARY,
                RunicLootTables.STARTER_DISCOVERY_LIBRARY,
                0.35f
        );

        addChestInjection(
                "add_runic_starter_discovery_to_ancient_city",
                BuiltInLootTables.ANCIENT_CITY,
                RunicLootTables.STARTER_DISCOVERY_LIBRARY,
                0.30f
        );
    }

    private void addChestInjection(
            String name,
            ResourceKey<LootTable> targetTable,
            ResourceKey<LootTable> injectedTable,
            float chance
    ) {
        add(
                name,
                new AddTableLootModifier(
                        new LootItemCondition[]{
                                LootTableIdCondition.builder(targetTable.location()).build(),
                                LootItemRandomChanceCondition.randomChance(chance).build()
                        },
                        injectedTable
                )
        );
    }

    private void addRunicTechniqueManuals() {

    }

    private void addRunicPhysiques() {

    }

    private void addRunicBloodlines() {

    }
}