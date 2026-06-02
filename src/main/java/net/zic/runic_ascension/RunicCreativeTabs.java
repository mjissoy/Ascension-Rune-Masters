package net.zic.runic_ascension;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zic.runic_ascension.core.items.RunicItems;

public final class RunicCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, RunicAscension.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> RUNIC_ASCENSION_TAB =
            CREATIVE_MODE_TABS.register("runic_ascension", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.runic_ascension"))
                    .icon(() -> new ItemStack(RunicItems.RUNIC_CODEX.get()))
                    .displayItems((parameters, output) -> {
                        RunicItems.ITEMS.getEntries().forEach(item -> output.accept(item.get()));
                    })
                    .build()
            );

    private RunicCreativeTabs() {
    }

    public static void register(IEventBus bus) {
        CREATIVE_MODE_TABS.register(bus);
    }
}