package net.zic.runic_ascension.common.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.AddTableLootModifier;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;
import net.thejadeproject.ascension.datagen.loot.AddBloodlineRandomPurityModifier;
import net.thejadeproject.ascension.datagen.loot.AddPhysiqueRandomPurityModifier;
import net.thejadeproject.ascension.datagen.loot.conditions.AddTechniqueManualModifier;
import net.zic.runic_ascension.RunicAscension;
import net.zic.runic_ascension.core.bloodlines.RunicBloodlines;
import net.zic.runic_ascension.core.physiques.RunicPhysiques;
import net.zic.runic_ascension.core.techniques.RunicTechniques;

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

        addChestInjection(
                "add_runic_utility_to_stronghold_library",
                BuiltInLootTables.STRONGHOLD_LIBRARY,
                RunicLootTables.RUNIC_UTILITY_RARE,
                0.12f
        );

        addChestInjection(
                "add_runic_utility_to_ancient_city",
                BuiltInLootTables.ANCIENT_CITY,
                RunicLootTables.RUNIC_UTILITY_RARE,
                0.14f
        );

        addChestInjection(
                "add_runic_utility_to_simple_dungeon",
                BuiltInLootTables.SIMPLE_DUNGEON,
                RunicLootTables.RUNIC_UTILITY_RARE,
                0.06f
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
        addTechniqueManual("runic_apprentice_manual_from_simple_dungeon", mc("chests/simple_dungeon"), RunicTechniques.RUNIC_APPRENTICE.getId(), 0.10f);
        addTechniqueManual("runic_apprentice_manual_from_abandoned_mineshaft", mc("chests/abandoned_mineshaft"), RunicTechniques.RUNIC_APPRENTICE.getId(), 0.08f);
        addTechniqueManual("runic_apprentice_manual_from_stronghold_library", mc("chests/stronghold_library"), RunicTechniques.RUNIC_APPRENTICE.getId(), 0.10f);

        addTechniqueManual("inner_inscription_manual_from_stronghold_library", mc("chests/stronghold_library"), RunicTechniques.INNER_INSCRIPTION_METHOD.getId(), 0.07f);
        addTechniqueManual("inner_inscription_manual_from_ancient_city", mc("chests/ancient_city"), RunicTechniques.INNER_INSCRIPTION_METHOD.getId(), 0.06f);
        addTechniqueManual("inner_inscription_manual_from_simple_dungeon", mc("chests/simple_dungeon"), RunicTechniques.INNER_INSCRIPTION_METHOD.getId(), 0.04f);

        addTechniqueManual("outer_formula_manual_from_desert_pyramid", mc("chests/desert_pyramid"), RunicTechniques.OUTER_FORMULA_METHOD.getId(), 0.07f);
        addTechniqueManual("outer_formula_manual_from_nether_bridge", mc("chests/nether_bridge"), RunicTechniques.OUTER_FORMULA_METHOD.getId(), 0.06f);
        addTechniqueManual("outer_formula_manual_from_simple_dungeon", mc("chests/simple_dungeon"), RunicTechniques.OUTER_FORMULA_METHOD.getId(), 0.04f);

        addTechniqueManual("trace_visualization_manual_from_jungle_temple", mc("chests/jungle_temple"), RunicTechniques.TRACE_VISUALIZATION_METHOD.getId(), 0.07f);
        addTechniqueManual("trace_visualization_manual_from_abandoned_mineshaft", mc("chests/abandoned_mineshaft"), RunicTechniques.TRACE_VISUALIZATION_METHOD.getId(), 0.06f);
        addTechniqueManual("trace_visualization_manual_from_ancient_city", mc("chests/ancient_city"), RunicTechniques.TRACE_VISUALIZATION_METHOD.getId(), 0.06f);

    }

    private void addRunicPhysiques() {
        addPhysiqueEssence(
                "runic_subject_essence_from_simple_dungeon", mc("chests/simple_dungeon"), RunicPhysiques.RUNIC_SUBJECT.getId(), 20, 45, 0.07f
        );
        addPhysiqueEssence(
                "runic_subject_essence_from_abandoned_mineshaft", mc("chests/abandoned_mineshaft"), RunicPhysiques.RUNIC_SUBJECT.getId(), 20, 45, 0.06f
        );
        addPhysiqueEssence("runic_subject_essence_from_stronghold_library", mc("chests/stronghold_library"), RunicPhysiques.RUNIC_SUBJECT.getId(), 30, 55, 0.06f
        );

        addPhysiqueEssence(
                "runic_eyes_essence_from_stronghold_library", mc("chests/stronghold_library"), RunicPhysiques.RUNIC_EYES.getId(), 20, 45, 0.035f
        );
        addPhysiqueEssence(
                "runic_eyes_essence_from_ancient_city", mc("chests/ancient_city"), RunicPhysiques.RUNIC_EYES.getId(), 25, 55, 0.04f
        );

        addPhysiqueEssence(
                "essence_rune_physique_from_stronghold_library", mc("chests/stronghold_library"), RunicPhysiques.ESSENCE_RUNE_PHYSIQUE.getId(), 25, 50, 0.04f
        );
        addPhysiqueEssence(
                "body_rune_physique_from_desert_pyramid", mc("chests/desert_pyramid"), RunicPhysiques.BODY_RUNE_PHYSIQUE.getId(), 25, 50, 0.04f
        );
        addPhysiqueEssence(
                "body_rune_physique_from_simple_dungeon", mc("chests/simple_dungeon"), RunicPhysiques.BODY_RUNE_PHYSIQUE.getId(), 20, 45, 0.035f
        );
        addPhysiqueEssence(
                "soul_rune_physique_from_ancient_city", mc("chests/ancient_city"), RunicPhysiques.SOUL_RUNE_PHYSIQUE.getId(), 25, 55, 0.04f
        );
        addPhysiqueEssence(
                "soul_rune_physique_from_jungle_temple", mc("chests/jungle_temple"), RunicPhysiques.SOUL_RUNE_PHYSIQUE.getId(), 20, 45, 0.03f
        );
    }

    private void addRunicBloodlines() {

        addBloodlineEssence("ink_blood_lineage_from_ancient_city", mc("chests/ancient_city"), RunicBloodlines.INK_BLOOD_LINEAGE.getId(), 15, 55, 0.045f, false);
        addBloodlineEssence("ink_blood_lineage_from_witch", mc("entities/witch"), RunicBloodlines.INK_BLOOD_LINEAGE.getId(), 5, 30, 0.035f, true);

        addBloodlineEssence("burning_blood_runes_from_nether_bridge", mc("chests/nether_bridge"), RunicBloodlines.BURNING_BLOOD_RUNES.getId(), 10, 45, 0.05f, false);
        addBloodlineEssence("burning_blood_runes_from_blaze", mc("entities/blaze"), RunicBloodlines.BURNING_BLOOD_RUNES.getId(), 5, 35, 0.045f, true);

    }

    private void addTechniqueManual(String name, ResourceLocation targetTable, ResourceLocation techniqueId, float chance) {
        add(name, new AddTechniqueManualModifier(
                new LootItemCondition[]{
                        LootTableIdCondition.builder(targetTable).build(),
                        LootItemRandomChanceCondition.randomChance(chance).build()
                }, techniqueId
                )
        );
    }

    private void addPhysiqueEssence(String name, ResourceLocation targetTable, ResourceLocation physiqueId, int minPurity, int maxPurity, float chance) {
        add(name, new AddPhysiqueRandomPurityModifier(new LootItemCondition[]{
                        LootTableIdCondition.builder(targetTable).build(),
                        LootItemRandomChanceCondition.randomChance(chance).build()
                        }, physiqueId, minPurity, maxPurity
                )
        );
    }

    private void addBloodlineEssence(String name, ResourceLocation targetTable, ResourceLocation bloodlineId, int minPurity, int maxPurity, float chance, boolean killedByPlayer) {
        LootItemCondition[] conditions = killedByPlayer ? new LootItemCondition[]{LootTableIdCondition.builder(targetTable).build(), LootItemKilledByPlayerCondition.killedByPlayer().build(), LootItemRandomChanceCondition.randomChance(chance).build()
        } : new LootItemCondition[]{LootTableIdCondition.builder(targetTable).build(), LootItemRandomChanceCondition.randomChance(chance).build()};

        add(name, new AddBloodlineRandomPurityModifier(conditions, bloodlineId, minPurity, maxPurity));
    }

    private static ResourceLocation mc(String path) {
        return ResourceLocation.withDefaultNamespace(path);
    }
}