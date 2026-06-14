package net.zic.runic_ascension.common.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.zic.runic_ascension.core.items.RunicItems;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class RunicLootTableProvider extends LootTableProvider {

    public RunicLootTableProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> registries
    ) {
        super(
                output,
                Set.of(),
                List.of(
                        new SubProviderEntry(
                                RunicDiscoveryLoot::new,
                                LootContextParamSets.CHEST
                        )
                ),
                registries
        );
    }

    private static class RunicDiscoveryLoot implements LootTableSubProvider {

        @SuppressWarnings("unused")
        public RunicDiscoveryLoot(HolderLookup.Provider provider) {
        }

        @Override
        public void generate(BiConsumer<net.minecraft.resources.ResourceKey<LootTable>, LootTable.Builder> consumer) {
            consumer.accept(
                    RunicLootTables.STARTER_DISCOVERY_COMMON,
                    LootTable.lootTable()
                            .withPool(LootPool.lootPool()
                                    .setRolls(ConstantValue.exactly(1))
                                    .add(LootItem.lootTableItem(RunicItems.RUNIC_SCRAP_ORIGIN.get()).setWeight(5))
                                    .add(LootItem.lootTableItem(RunicItems.RUNIC_SCRAP_INTENT.get()).setWeight(5))
                                    .add(LootItem.lootTableItem(RunicItems.RUNIC_SCRAP_FORMS.get()).setWeight(4))
                                    .add(LootItem.lootTableItem(RunicItems.RUNIC_SCRAP_MODIFIERS.get()).setWeight(4))
                                    .add(LootItem.lootTableItem(RunicItems.RUNIC_SCRAP_INSTABILITY.get()).setWeight(3))
                                    .add(LootItem.lootTableItem(RunicItems.RUNIC_SCRAP_FIRST_FORMULA.get()).setWeight(3))
                                    .add(LootItem.lootTableItem(RunicItems.RUNIC_TOME_EMBER_BOLT.get()).setWeight(2))
                                    .add(LootItem.lootTableItem(RunicItems.RUNIC_TOME_STONE_WARD.get()).setWeight(2))
                                    .add(LootItem.lootTableItem(RunicItems.RUNIC_TOME_GENTLE_RENEWAL.get()).setWeight(2))
                                    .add(LootItem.lootTableItem(RunicItems.RUNIC_TOME_WIND_STEP.get()).setWeight(2))
                            )
            );

            consumer.accept(
                    RunicLootTables.STARTER_DISCOVERY_LIBRARY,
                    LootTable.lootTable()
                            .withPool(LootPool.lootPool()
                                    .setRolls(ConstantValue.exactly(1))
                                    .add(LootItem.lootTableItem(RunicItems.RUNIC_SCRAP_FREE_CASTING.get()).setWeight(5))
                                    .add(LootItem.lootTableItem(RunicItems.RUNIC_SCRAP_SUPPRESSION.get()).setWeight(4))
                                    .add(LootItem.lootTableItem(RunicItems.RUNIC_SCRAP_FIRST_FORMULA.get()).setWeight(4))
                                    .add(LootItem.lootTableItem(RunicItems.RUNIC_TOME_EMBER_BOLT.get()).setWeight(3))
                                    .add(LootItem.lootTableItem(RunicItems.RUNIC_TOME_STONE_WARD.get()).setWeight(3))
                                    .add(LootItem.lootTableItem(RunicItems.RUNIC_TOME_GENTLE_RENEWAL.get()).setWeight(3))
                                    .add(LootItem.lootTableItem(RunicItems.RUNIC_TOME_WIND_STEP.get()).setWeight(3))
                                    .add(LootItem.lootTableItem(RunicItems.RUNIC_TOME_FROST_BIND.get()).setWeight(2))
                                    .add(LootItem.lootTableItem(RunicItems.RUNIC_TOME_STORM_PIERCE.get()).setWeight(1))
                            )
            );

            consumer.accept(
                    RunicLootTables.RUNIC_UTILITY_RARE,
                    LootTable.lootTable()
                            .withPool(LootPool.lootPool()
                                    .setRolls(ConstantValue.exactly(1))
                                    .add(LootItem.lootTableItem(RunicItems.BASIC_RUNIC_BRUSH.get()).setWeight(5))
                                    .add(LootItem.lootTableItem(RunicItems.RUNIC_INSCRIPTION_SEAL.get()).setWeight(4))
                                    .add(LootItem.lootTableItem(RunicItems.RUNIC_CODEX.get()).setWeight(3))
                                    .add(LootItem.lootTableItem(RunicItems.EARTH_RUNIC_BRUSH.get()).setWeight(2))
                                    .add(LootItem.lootTableItem(RunicItems.HEAVEN_RUNIC_BRUSH.get()).setWeight(1))
                                    .add(LootItem.lootTableItem(RunicItems.HELL_RUNIC_BRUSH.get()).setWeight(1))
                            )
            );

        }
    }
}