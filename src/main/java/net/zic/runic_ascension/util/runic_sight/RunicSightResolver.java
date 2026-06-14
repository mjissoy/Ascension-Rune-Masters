package net.zic.runic_ascension.util.runic_sight;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.zic.runic_ascension.content.runes.ModRunicRunes;

import java.util.ArrayList;
import java.util.List;

public final class RunicSightResolver {

    private RunicSightResolver() {}

    public static List<RunicSightTrace> getTracesForBlock(BlockState state) {
        List<RunicSightTrace> traces = new ArrayList<>();

        if (state.is(Blocks.FIRE) || state.is(Blocks.LAVA) || state.is(Blocks.MAGMA_BLOCK) || state.is(Blocks.CAMPFIRE)) {
            addSurface(traces, ModRunicRunes.FLAME_RUNE.getId(), "common_flame");
            addDeep(traces, ModRunicRunes.RELEASE_RUNE.getId(), "common_flame");
        }

        if (state.is(Blocks.SOUL_FIRE) || state.is(Blocks.SOUL_CAMPFIRE)) {
            addSurface(traces, ModRunicRunes.FLAME_RUNE.getId(), "soul_flame");
            addDeep(traces, ModRunicRunes.SHADOW_RUNE.getId(), "soul_flame");
            addDeep(traces, ModRunicRunes.DECAY_RUNE.getId(), "soul_flame");
        }

        if (state.is(Blocks.WATER) || state.is(Blocks.KELP) || state.is(Blocks.SEAGRASS)) {
            addSurface(traces, ModRunicRunes.WATER_RUNE.getId(), "water_growth");
            addDeep(traces, ModRunicRunes.GATHER_RUNE.getId(), "water_growth");
        }

        if (state.is(Blocks.ICE) || state.is(Blocks.PACKED_ICE) || state.is(Blocks.BLUE_ICE) || state.is(Blocks.SNOW_BLOCK) || state.is(Blocks.POWDER_SNOW)) {
            addSurface(traces, ModRunicRunes.WATER_RUNE.getId(), "frozen_water");
            addDeep(traces, ModRunicRunes.FROST_RUNE.getId(), "frozen_water");
            addDeep(traces, ModRunicRunes.STABILISE_RUNE.getId(), "frozen_water");
        }

        if (state.is(BlockTags.STONE_ORE_REPLACEABLES) || state.is(Blocks.STONE) || state.is(Blocks.COBBLESTONE) || state.is(Blocks.DEEPSLATE)) {
            addSurface(traces, ModRunicRunes.EARTH_RUNE.getId(), "stone_body");
            addDeep(traces, ModRunicRunes.HEAVY_RUNE.getId(), "stone_body");
            addDeep(traces, ModRunicRunes.STABILISE_RUNE.getId(), "stone_body");
        }

        if (state.is(Blocks.OBSIDIAN) || state.is(Blocks.CRYING_OBSIDIAN) || state.is(Blocks.ANCIENT_DEBRIS) || state.is(Blocks.NETHERITE_BLOCK)) {
            addSurface(traces, ModRunicRunes.EARTH_RUNE.getId(), "ancient_stone");
            addDeep(traces, ModRunicRunes.HEAVY_RUNE.getId(), "ancient_stone");
            addDeep(traces, ModRunicRunes.STABILISE_RUNE.getId(), "ancient_stone");
            addHidden(traces, ModRunicRunes.HIDDEN_RUNE.getId(), "ancient_stone");
        }

        if (state.is(BlockTags.LOGS) || state.is(BlockTags.LEAVES) || state.is(BlockTags.SAPLINGS) || state.is(BlockTags.FLOWERS)
                || state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.MOSS_BLOCK) || state.is(Blocks.VINE)) {
            addSurface(traces, ModRunicRunes.WOOD_RUNE.getId(), "living_green");
            addDeep(traces, ModRunicRunes.LIFE_RUNE.getId(), "living_green");
            addDeep(traces, ModRunicRunes.GATHER_RUNE.getId(), "living_green");
        }

        if (state.is(Blocks.WHEAT) || state.is(Blocks.CARROTS) || state.is(Blocks.POTATOES) || state.is(Blocks.BEETROOTS)
                || state.is(Blocks.SUGAR_CANE) || state.is(Blocks.BAMBOO) || state.is(Blocks.SWEET_BERRY_BUSH)) {
            addSurface(traces, ModRunicRunes.WOOD_RUNE.getId(), "cultivated_growth");
            addDeep(traces, ModRunicRunes.LIFE_RUNE.getId(), "cultivated_growth");
            addDeep(traces, ModRunicRunes.GATHER_RUNE.getId(), "cultivated_growth");
        }

        if (state.is(Blocks.IRON_BLOCK) || state.is(Blocks.IRON_ORE) || state.is(Blocks.DEEPSLATE_IRON_ORE)
                || state.is(Blocks.GOLD_BLOCK) || state.is(Blocks.GOLD_ORE) || state.is(Blocks.DEEPSLATE_GOLD_ORE)
                || state.is(Blocks.COPPER_BLOCK) || state.is(Blocks.COPPER_ORE) || state.is(Blocks.DEEPSLATE_COPPER_ORE)) {
            addSurface(traces, ModRunicRunes.METAL_RUNE.getId(), "worked_metal");
            addDeep(traces, ModRunicRunes.HEAVY_RUNE.getId(), "worked_metal");
        }

        if (state.is(Blocks.REDSTONE_BLOCK) || state.is(Blocks.REDSTONE_ORE) || state.is(Blocks.DEEPSLATE_REDSTONE_ORE)
                || state.is(Blocks.REDSTONE_TORCH) || state.is(Blocks.REDSTONE_WIRE)) {
            addSurface(traces, ModRunicRunes.LIGHTNING_RUNE.getId(), "redstone_current");
            addDeep(traces, ModRunicRunes.PULSE_RUNE.getId(), "redstone_current");
            addDeep(traces, ModRunicRunes.QUICKEN_RUNE.getId(), "redstone_current");
        }

        if (state.is(Blocks.TORCH) || state.is(Blocks.WALL_TORCH) || state.is(Blocks.LANTERN)
                || state.is(Blocks.GLOWSTONE) || state.is(Blocks.SEA_LANTERN) || state.is(Blocks.END_ROD)) {
            addSurface(traces, ModRunicRunes.LIGHT_RUNE.getId(), "common_light");
            addDeep(traces, ModRunicRunes.RELEASE_RUNE.getId(), "common_light");
        }

        if (state.is(Blocks.BOOKSHELF) || state.is(Blocks.CHISELED_BOOKSHELF) || state.is(Blocks.ENCHANTING_TABLE)
                || state.is(Blocks.AMETHYST_BLOCK) || state.is(Blocks.BUDDING_AMETHYST)) {
            addSurface(traces, ModRunicRunes.GATHER_RUNE.getId(), "stored_pattern");
            addDeep(traces, ModRunicRunes.LIGHT_RUNE.getId(), "stored_pattern");
            addDeep(traces, ModRunicRunes.HIDDEN_RUNE.getId(), "stored_pattern");
        }

        if (state.is(Blocks.SCULK) || state.is(Blocks.SCULK_SENSOR) || state.is(Blocks.SCULK_CATALYST) || state.is(Blocks.SCULK_SHRIEKER)) {
            addSurface(traces, ModRunicRunes.SHADOW_RUNE.getId(), "sculk_resonance");
            addDeep(traces, ModRunicRunes.DECAY_RUNE.getId(), "sculk_resonance");
            addDeep(traces, ModRunicRunes.PULSE_RUNE.getId(), "sculk_resonance");
            addHidden(traces, ModRunicRunes.HIDDEN_RUNE.getId(), "sculk_resonance");
        }

        if (state.is(Blocks.SOUL_SAND) || state.is(Blocks.SOUL_SOIL)) {
            addSurface(traces, ModRunicRunes.SHADOW_RUNE.getId(), "soul_soil");
            addDeep(traces, ModRunicRunes.DECAY_RUNE.getId(), "soul_soil");
            addDeep(traces, ModRunicRunes.BIND_RUNE.getId(), "soul_soil");
        }

        return traces;
    }

    public static List<RunicSightTrace> getTracesForEntity(Entity entity) {
        List<RunicSightTrace> traces = new ArrayList<>();
        EntityType<?> type = entity.getType();

        if (type == EntityType.BLAZE) {
            addSurface(traces, ModRunicRunes.FLAME_RUNE.getId(), "blaze_core");
            addDeep(traces, ModRunicRunes.RELEASE_RUNE.getId(), "blaze_core");
            addDeep(traces, ModRunicRunes.VIOLENT_RUNE.getId(), "blaze_core");
        }

        if (type == EntityType.CREEPER) {
            addSurface(traces, ModRunicRunes.VIOLENT_RUNE.getId(), "creeper_charge");
            addDeep(traces, ModRunicRunes.PULSE_RUNE.getId(), "creeper_charge");
            addDeep(traces, ModRunicRunes.LIGHTNING_RUNE.getId(), "creeper_charge");
        }

        if (type == EntityType.IRON_GOLEM) {
            addSurface(traces, ModRunicRunes.METAL_RUNE.getId(), "golem_body");
            addSurface(traces, ModRunicRunes.GUARD_RUNE.getId(), "golem_body");
            addDeep(traces, ModRunicRunes.HEAVY_RUNE.getId(), "golem_body");
        }

        if (type == EntityType.WOLF) {
            addSurface(traces, ModRunicRunes.WIND_RUNE.getId(), "wolf_instinct");
            addSurface(traces, ModRunicRunes.BIND_RUNE.getId(), "wolf_instinct");
            addDeep(traces, ModRunicRunes.LIFE_RUNE.getId(), "wolf_instinct");
        }

        if (type == EntityType.ZOMBIE || type == EntityType.DROWNED || type == EntityType.HUSK) {
            addSurface(traces, ModRunicRunes.DECAY_RUNE.getId(), "restless_dead");
            addDeep(traces, ModRunicRunes.HEAVY_RUNE.getId(), "restless_dead");
            addDeep(traces, ModRunicRunes.BIND_RUNE.getId(), "restless_dead");
        }

        if (type == EntityType.SKELETON || type == EntityType.STRAY || type == EntityType.WITHER_SKELETON) {
            addSurface(traces, ModRunicRunes.DECAY_RUNE.getId(), "bone_memory");
            addDeep(traces, ModRunicRunes.PIERCE_RUNE.getId(), "bone_memory");
            addDeep(traces, ModRunicRunes.FROST_RUNE.getId(), "bone_memory");
        }

        if (type == EntityType.SPIDER || type == EntityType.CAVE_SPIDER) {
            addSurface(traces, ModRunicRunes.BIND_RUNE.getId(), "webbed_hunger");
            addDeep(traces, ModRunicRunes.HIDDEN_RUNE.getId(), "webbed_hunger");
            addDeep(traces, ModRunicRunes.VIOLENT_RUNE.getId(), "webbed_hunger");
        }

        if (type == EntityType.ENDERMAN) {
            addSurface(traces, ModRunicRunes.SHADOW_RUNE.getId(), "enderman_step");
            addDeep(traces, ModRunicRunes.PULL_RUNE.getId(), "enderman_step");
            addDeep(traces, ModRunicRunes.HIDDEN_RUNE.getId(), "enderman_step");
        }

        if (type == EntityType.VILLAGER) {
            addSurface(traces, ModRunicRunes.GATHER_RUNE.getId(), "village_memory");
            addDeep(traces, ModRunicRunes.GUARD_RUNE.getId(), "village_memory");
            addDeep(traces, ModRunicRunes.LIFE_RUNE.getId(), "village_memory");
        }

        if (type == EntityType.COW || type == EntityType.SHEEP || type == EntityType.PIG || type == EntityType.CHICKEN || type == EntityType.HORSE) {
            addSurface(traces, ModRunicRunes.LIFE_RUNE.getId(), "living_beast");
            addDeep(traces, ModRunicRunes.GATHER_RUNE.getId(), "living_beast");
        }

        return traces;
    }

    public static List<ResourceLocation> getRunesForBlock(BlockState state) {
        return getTracesForBlock(state).stream()
                .map(RunicSightTrace::runeId)
                .toList();
    }

    public static List<ResourceLocation> getRunesForEntity(Entity entity) {
        return getTracesForEntity(entity).stream()
                .map(RunicSightTrace::runeId)
                .toList();
    }

    private static void addSurface(List<RunicSightTrace> traces, ResourceLocation runeId, String originKey) {
        addTrace(traces, RunicSightTrace.surface(runeId, originKey));
    }

    private static void addDeep(List<RunicSightTrace> traces, ResourceLocation runeId, String originKey) {
        addTrace(traces, RunicSightTrace.deep(runeId, originKey));
    }

    private static void addHidden(List<RunicSightTrace> traces, ResourceLocation runeId, String originKey) {
        addTrace(traces, RunicSightTrace.hidden(runeId, originKey));
    }

    private static void addTrace(List<RunicSightTrace> traces, RunicSightTrace trace) {
        boolean alreadyPresent = traces.stream()
                .anyMatch(existing -> existing.runeId().equals(trace.runeId()));

        if (!alreadyPresent) {
            traces.add(trace);
        }
    }
}
