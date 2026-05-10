package net.zic.runic_ascension;

import net.zic.runic_ascension.registries.items.RunicItems;
import net.zic.runic_ascension.registries.paths.RunicPaths;
import net.zic.runic_ascension.registries.skills.RunicSkills;
import net.zic.runic_ascension.registries.techniques.RunicTechniques;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(RunicAscension.MOD_ID)
public class RunicAscension {

    public static final String MOD_ID = "runic_ascension";
    public static final Logger LOGGER = LogUtils.getLogger();

    public void register(IEventBus modEventBus) {
        RunicItems.register(modEventBus);
        RunicPaths.register(modEventBus);
        RunicSkills.register(modEventBus);
        RunicTechniques.register(modEventBus);
    }

    public RunicAscension(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        NeoForge.EVENT_BUS.register(this);
        register(modEventBus);
        modEventBus.addListener(this::addCreative);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM COMMON SETUP");

    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }
}
