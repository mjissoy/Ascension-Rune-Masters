package net.zic.runic_ascension.registries.paths;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zic.runic_ascension.RunicAscension;

public class RunicPaths {

    public static final DeferredRegister<IPath> PATHS =
            DeferredRegister.create(AscensionRegistries.PATHS, RunicAscension.MOD_ID);

    public static final DeferredHolder<IPath, IPath> RUNIC =
            PATHS.register("runic", RunicPath::new);

    public static void register(IEventBus bus) {
        PATHS.register(bus);
    }

}
