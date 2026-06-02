package net.zic.runic_ascension.core.items;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zic.runic_ascension.RunicAscension;
import net.zic.runic_ascension.content.runes.ModRunicRunes;

import java.util.List;

public final class RunicItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(RunicAscension.MOD_ID);

    public static final DeferredItem<RunicTomeItem> RUNIC_TOME = ITEMS.register("runic_tome",
            () -> new RunicTomeItem(
                    new Item.Properties().stacksTo(1),
                    ModRunicRunes.allRuneIds()
            ));

    public static final DeferredItem<RunicTomeItem> RUNIC_TOME_EMBER_BOLT = registerFormulaTome(
            "runic_tome_ember_bolt",
            "ember_bolt",
            2,
            3,
            ModRunicRunes.FLAME_RUNE.getId(),
            ModRunicRunes.CUT_RUNE.getId(),
            ModRunicRunes.BOLT_RUNE.getId(),
            ModRunicRunes.LINE_RUNE.getId(),
            ModRunicRunes.VIOLENT_RUNE.getId(),
            ModRunicRunes.STABILISE_RUNE.getId()
    );

    public static final DeferredItem<RunicTomeItem> RUNIC_TOME_STONE_WARD = registerFormulaTome(
            "runic_tome_stone_ward",
            "stone_ward",
            2,
            3,
            ModRunicRunes.EARTH_RUNE.getId(),
            ModRunicRunes.GUARD_RUNE.getId(),
            ModRunicRunes.WALL_RUNE.getId(),
            ModRunicRunes.CIRCLE_RUNE.getId(),
            ModRunicRunes.HEAVY_RUNE.getId(),
            ModRunicRunes.STABILISE_RUNE.getId()
    );

    public static final DeferredItem<RunicTomeItem> RUNIC_TOME_GENTLE_RENEWAL = registerFormulaTome(
            "runic_tome_gentle_renewal",
            "gentle_renewal",
            2,
            3,
            ModRunicRunes.LIFE_RUNE.getId(),
            ModRunicRunes.WATER_RUNE.getId(),
            ModRunicRunes.HEAL_RUNE.getId(),
            ModRunicRunes.CIRCLE_RUNE.getId(),
            ModRunicRunes.VEIL_RUNE.getId(),
            ModRunicRunes.STABILISE_RUNE.getId()
    );

    public static final DeferredItem<RunicTomeItem> RUNIC_TOME_WIND_STEP = registerFormulaTome(
            "runic_tome_wind_step",
            "wind_step",
            2,
            3,
            ModRunicRunes.WIND_RUNE.getId(),
            ModRunicRunes.PUSH_RUNE.getId(),
            ModRunicRunes.PULL_RUNE.getId(),
            ModRunicRunes.PULSE_RUNE.getId(),
            ModRunicRunes.VEIL_RUNE.getId(),
            ModRunicRunes.QUICKEN_RUNE.getId()
    );

    public static final DeferredItem<RunicTomeItem> RUNIC_TOME_FROST_BIND = registerFormulaTome(
            "runic_tome_frost_bind",
            "frost_bind",
            2,
            3,
            ModRunicRunes.FROST_RUNE.getId(),
            ModRunicRunes.WATER_RUNE.getId(),
            ModRunicRunes.BIND_RUNE.getId(),
            ModRunicRunes.CIRCLE_RUNE.getId(),
            ModRunicRunes.MARK_RUNE.getId(),
            ModRunicRunes.HEAVY_RUNE.getId()
    );

    public static final DeferredItem<RunicTomeItem> RUNIC_TOME_STORM_PIERCE = registerFormulaTome(
            "runic_tome_storm_pierce",
            "storm_pierce",
            2,
            3,
            ModRunicRunes.LIGHTNING_RUNE.getId(),
            ModRunicRunes.CUT_RUNE.getId(),
            ModRunicRunes.PIERCE_RUNE.getId(),
            ModRunicRunes.PULSE_RUNE.getId(),
            ModRunicRunes.BOLT_RUNE.getId(),
            ModRunicRunes.VIOLENT_RUNE.getId()
    );

    public static final DeferredItem<RunicScrapItem> RUNIC_SCRAP_ORIGIN = registerScrap("runic_scrap_origin", "origin", 4);
    public static final DeferredItem<RunicScrapItem> RUNIC_SCRAP_INTENT = registerScrap("runic_scrap_intent", "intent", 4);
    public static final DeferredItem<RunicScrapItem> RUNIC_SCRAP_FORMS = registerScrap("runic_scrap_forms", "forms", 4);
    public static final DeferredItem<RunicScrapItem> RUNIC_SCRAP_MODIFIERS = registerScrap("runic_scrap_modifiers", "modifiers", 4);
    public static final DeferredItem<RunicScrapItem> RUNIC_SCRAP_INSTABILITY = registerScrap("runic_scrap_instability", "instability", 4);
    public static final DeferredItem<RunicScrapItem> RUNIC_SCRAP_SUPPRESSION = registerScrap("runic_scrap_suppression", "suppression", 4);
    public static final DeferredItem<RunicScrapItem> RUNIC_SCRAP_FIRST_FORMULA = registerScrap("runic_scrap_first_formula", "first_formula", 5);
    public static final DeferredItem<RunicScrapItem> RUNIC_SCRAP_FREE_CASTING = registerScrap("runic_scrap_free_casting", "free_casting", 4);

    public static final DeferredItem<RunicCodexItem> RUNIC_CODEX = ITEMS.register("runic_codex",
            () -> new RunicCodexItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<RunicBrushItem> BASIC_RUNIC_BRUSH = ITEMS.register("basic_runic_brush",
            () -> new RunicBrushItem(
                    new Item.Properties().stacksTo(1),
                    1,
                    ModRunicRunes.STABILISE_RUNE.getId()
            ));

    private static DeferredItem<RunicTomeItem> registerFormulaTome(
            String path,
            String formulaFamilyKey,
            int minimumRuneGrants,
            int maximumRuneGrants,
            ResourceLocation... runePool
    ) {
        return ITEMS.register(path, () -> new RunicTomeItem(
                new Item.Properties().stacksTo(1),
                List.of(runePool),
                minimumRuneGrants,
                maximumRuneGrants,
                formulaFamilyKey
        ));
    }

    private static DeferredItem<RunicScrapItem> registerScrap(String path, String scrapKey, int lineCount) {
        return ITEMS.register(path, () -> new RunicScrapItem(
                new Item.Properties().stacksTo(16),
                "runic_ascension.runic.scrap." + scrapKey,
                lineCount
        ));
    }

    private RunicItems() {
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
