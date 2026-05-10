package net.zic.runic_ascension.registries.techniques;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zic.runic_ascension.RunicAscension;

public class RunicTechniques {

    public static final DeferredRegister<ITechnique> TECHNIQUES =
            DeferredRegister.create(AscensionRegistries.TECHNIQUES, RunicAscension.MOD_ID);

    public static final DeferredHolder<ITechnique, ITechnique> RUNIC_APPRENTICE =
            TECHNIQUES.register("runic_apprentice", GeneralRunicTechnique::new);

    public static void register(IEventBus bus) {
        PATHS.register(bus);
    }

}
