package net.zic.runic_ascension.core.items;

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

    public static final DeferredItem<RunicTomeItem> RUNIC_TOME_BASE_ELEMENTS = ITEMS.register("runic_tome_base_elements",
            () -> new RunicTomeItem(
                    new Item.Properties().stacksTo(1),
                    List.of(
                            ModRunicRunes.FLAME_RUNE.getId(),
                            ModRunicRunes.WATER_RUNE.getId(),
                            ModRunicRunes.WIND_RUNE.getId(),
                            ModRunicRunes.EARTH_RUNE.getId()
                    )
            ));

    public static final DeferredItem<RunicTomeItem> RUNIC_TOME_DEEP_ELEMENTS = ITEMS.register("runic_tome_deep_elements",
            () -> new RunicTomeItem(
                    new Item.Properties().stacksTo(1),
                    List.of(
                            ModRunicRunes.WOOD_RUNE.getId(),
                            ModRunicRunes.METAL_RUNE.getId(),
                            ModRunicRunes.LIGHTNING_RUNE.getId(),
                            ModRunicRunes.FROST_RUNE.getId(),
                            ModRunicRunes.LIGHT_RUNE.getId(),
                            ModRunicRunes.SHADOW_RUNE.getId(),
                            ModRunicRunes.LIFE_RUNE.getId(),
                            ModRunicRunes.DECAY_RUNE.getId()
                    )
            ));

    public static final DeferredItem<RunicTomeItem> RUNIC_TOME_INTENTS = ITEMS.register("runic_tome_intents",
            () -> new RunicTomeItem(
                    new Item.Properties().stacksTo(1),
                    List.of(
                            ModRunicRunes.BIND_RUNE.getId(),
                            ModRunicRunes.PUSH_RUNE.getId(),
                            ModRunicRunes.PULL_RUNE.getId(),
                            ModRunicRunes.GUARD_RUNE.getId(),
                            ModRunicRunes.CUT_RUNE.getId(),
                            ModRunicRunes.HEAL_RUNE.getId(),
                            ModRunicRunes.GATHER_RUNE.getId(),
                            ModRunicRunes.RELEASE_RUNE.getId(),
                            ModRunicRunes.COMPRESS_RUNE.getId(),
                            ModRunicRunes.PIERCE_RUNE.getId()
                    )
            ));

    public static final DeferredItem<RunicTomeItem> RUNIC_TOME_FORMS = ITEMS.register("runic_tome_forms",
            () -> new RunicTomeItem(
                    new Item.Properties().stacksTo(1),
                    List.of(
                            ModRunicRunes.BOLT_RUNE.getId(),
                            ModRunicRunes.VEIL_RUNE.getId(),
                            ModRunicRunes.CIRCLE_RUNE.getId(),
                            ModRunicRunes.MARK_RUNE.getId(),
                            ModRunicRunes.WALL_RUNE.getId(),
                            ModRunicRunes.PULSE_RUNE.getId(),
                            ModRunicRunes.LINE_RUNE.getId(),
                            ModRunicRunes.SPHERE_RUNE.getId()
                    )
            ));

    public static final DeferredItem<RunicTomeItem> RUNIC_TOME_MODIFIERS = ITEMS.register("runic_tome_modifiers",
            () -> new RunicTomeItem(
                    new Item.Properties().stacksTo(1),
                    List.of(
                            ModRunicRunes.QUICKEN_RUNE.getId(),
                            ModRunicRunes.STABILISE_RUNE.getId(),
                            ModRunicRunes.HEAVY_RUNE.getId(),
                            ModRunicRunes.VIOLENT_RUNE.getId(),
                            ModRunicRunes.HIDDEN_RUNE.getId()
                    )
            ));

    public static final DeferredItem<RunicCodexItem> RUNIC_CODEX = ITEMS.register("runic_codex",
            () -> new RunicCodexItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<RunicBrushItem> BASIC_RUNIC_BRUSH = ITEMS.register("basic_runic_brush",
            () -> new RunicBrushItem(
                    new Item.Properties().stacksTo(1),
                    1,
                    ModRunicRunes.STABILISE_RUNE.getId()
            ));

    private RunicItems() {
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
