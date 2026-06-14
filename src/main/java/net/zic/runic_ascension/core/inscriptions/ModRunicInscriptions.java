package net.zic.runic_ascension.core.inscriptions;

import net.minecraft.resources.ResourceLocation;
import net.zic.runic_ascension.RunicAscension;
import net.zic.runic_ascension.content.runes.ModRunicRunes;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ModRunicInscriptions {

    private static final Map<ResourceLocation, RunicInscription> BY_ID = new LinkedHashMap<>();

    public static final RunicInscription FLAMEGUARD = register("flameguard", new RunicInscription(id("flameguard"))
            .addTier(
                    1,
                    0,
                    ModRunicRunes.FLAME_RUNE.getId(),
                    ModRunicRunes.GUARD_RUNE.getId()
            )
            .addTier(
                    2,
                    2,
                    ModRunicRunes.FLAME_RUNE.getId(),
                    ModRunicRunes.GUARD_RUNE.getId(),
                    ModRunicRunes.STABILISE_RUNE.getId()
            )
            .addTier(
                    3,
                    4,
                    ModRunicRunes.FLAME_RUNE.getId(),
                    ModRunicRunes.GUARD_RUNE.getId(),
                    ModRunicRunes.STABILISE_RUNE.getId(),
                    ModRunicRunes.CIRCLE_RUNE.getId()
            )
    );

    public static final RunicInscription STONEHIDE = register("stonehide", new RunicInscription(id("stonehide"))
            .addTier(
                    1,
                    0,
                    ModRunicRunes.EARTH_RUNE.getId(),
                    ModRunicRunes.GUARD_RUNE.getId()
            )
            .addTier(
                    2,
                    2,
                    ModRunicRunes.EARTH_RUNE.getId(),
                    ModRunicRunes.GUARD_RUNE.getId(),
                    ModRunicRunes.HEAVY_RUNE.getId()
            )
            .addTier(
                    3,
                    4,
                    ModRunicRunes.EARTH_RUNE.getId(),
                    ModRunicRunes.GUARD_RUNE.getId(),
                    ModRunicRunes.HEAVY_RUNE.getId(),
                    ModRunicRunes.WALL_RUNE.getId()
            )
    );

    public static final RunicInscription CLEARFLOW = register("clearflow", new RunicInscription(id("clearflow"))
            .addTier(
                    1,
                    1,
                    ModRunicRunes.WATER_RUNE.getId(),
                    ModRunicRunes.HEAL_RUNE.getId()
            )
            .addTier(
                    2,
                    3,
                    ModRunicRunes.WATER_RUNE.getId(),
                    ModRunicRunes.HEAL_RUNE.getId(),
                    ModRunicRunes.CIRCLE_RUNE.getId()
            )
            .addTier(
                    3,
                    4,
                    ModRunicRunes.WATER_RUNE.getId(),
                    ModRunicRunes.HEAL_RUNE.getId(),
                    ModRunicRunes.CIRCLE_RUNE.getId(),
                    ModRunicRunes.LIFE_RUNE.getId()
            )
    );

    public static final RunicInscription WINDSTEP = register("windstep", new RunicInscription(id("windstep"))
            .addTier(
                    1,
                    1,
                    ModRunicRunes.WIND_RUNE.getId(),
                    ModRunicRunes.QUICKEN_RUNE.getId()
            )
            .addTier(
                    2,
                    3,
                    ModRunicRunes.WIND_RUNE.getId(),
                    ModRunicRunes.QUICKEN_RUNE.getId(),
                    ModRunicRunes.VEIL_RUNE.getId()
            )
            .addTier(
                    3,
                    4,
                    ModRunicRunes.WIND_RUNE.getId(),
                    ModRunicRunes.QUICKEN_RUNE.getId(),
                    ModRunicRunes.VEIL_RUNE.getId(),
                    ModRunicRunes.HIDDEN_RUNE.getId()
            )
    );

    public static final RunicInscription STILL_SCRIPT = register("still_script", new RunicInscription(id("still_script"))
            .addTier(
                    1,
                    1,
                    ModRunicRunes.STABILISE_RUNE.getId(),
                    ModRunicRunes.CIRCLE_RUNE.getId()
            )
            .addTier(
                    2,
                    3,
                    ModRunicRunes.STABILISE_RUNE.getId(),
                    ModRunicRunes.CIRCLE_RUNE.getId(),
                    ModRunicRunes.GATHER_RUNE.getId()
            )
            .addTier(
                    3,
                    4,
                    ModRunicRunes.STABILISE_RUNE.getId(),
                    ModRunicRunes.CIRCLE_RUNE.getId(),
                    ModRunicRunes.GATHER_RUNE.getId(),
                    ModRunicRunes.HIDDEN_RUNE.getId()
            )
    );

    public static final RunicInscription MIRROR_MARK = register("mirror_mark", new RunicInscription(id("mirror_mark"))
            .addTier(
                    1,
                    1,
                    ModRunicRunes.MARK_RUNE.getId(),
                    ModRunicRunes.GUARD_RUNE.getId(),
                    ModRunicRunes.VEIL_RUNE.getId()
            )
            .addTier(
                    2,
                    3,
                    ModRunicRunes.MARK_RUNE.getId(),
                    ModRunicRunes.GUARD_RUNE.getId(),
                    ModRunicRunes.VEIL_RUNE.getId(),
                    ModRunicRunes.PULSE_RUNE.getId()
            )
            .addTier(
                    3,
                    4,
                    ModRunicRunes.MARK_RUNE.getId(),
                    ModRunicRunes.GUARD_RUNE.getId(),
                    ModRunicRunes.VEIL_RUNE.getId(),
                    ModRunicRunes.PULSE_RUNE.getId(),
                    ModRunicRunes.LIGHT_RUNE.getId()
            )
    );

    private ModRunicInscriptions() {
    }

    public static RunicInscription get(ResourceLocation id) {
        return BY_ID.get(id);
    }

    public static Collection<RunicInscription> all() {
        return BY_ID.values();
    }

    private static RunicInscription register(String path, RunicInscription inscription) {
        BY_ID.put(id(path), inscription);
        return inscription;
    }

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(RunicAscension.MOD_ID, path);
    }
}