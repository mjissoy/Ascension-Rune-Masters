package net.zic.runic_ascension.core.techniques;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;
import net.thejadeproject.ascension.refactor_packages.techniques.ITechnique;
import net.zic.runic_ascension.RunicAscension;

public class RunicTechniques {

    public static final DeferredRegister<ITechnique> TECHNIQUES =
            DeferredRegister.create(AscensionRegistries.Techniques.TECHNIQUES_REGISTRY, RunicAscension.MOD_ID);

    public static final DeferredHolder<ITechnique, GeneralRunicTechnique> RUNIC_APPRENTICE =
            TECHNIQUES.register("runic_apprentice", GeneralRunicTechnique::new);

    public static void register(IEventBus bus) {
        TECHNIQUES.register(bus);
    }

}
