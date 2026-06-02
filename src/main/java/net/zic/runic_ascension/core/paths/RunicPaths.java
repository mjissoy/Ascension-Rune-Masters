package net.zic.runic_ascension.core.paths;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.refactor_packages.paths.IPath;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;
import net.zic.runic_ascension.RunicAscension;

public final class RunicPaths {

    public static final DeferredRegister<IPath> PATHS =
            DeferredRegister.create(
                    AscensionRegistries.Paths.PATHS_REGISTRY,
                    RunicAscension.MOD_ID
            );

    public static final DeferredHolder<IPath, RunicPath> RUNIC =
            PATHS.register("runic", RunicPath::new);

    private RunicPaths() {
    }

    public static void register(IEventBus bus) {
        PATHS.register(bus);
    }
}