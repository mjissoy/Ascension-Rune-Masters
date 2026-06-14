package net.zic.runic_ascension.core.techniques;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;
import net.thejadeproject.ascension.refactor_packages.techniques.ITechnique;
import net.zic.runic_ascension.RunicAscension;
import net.zic.runic_ascension.core.techniques.runic.*;

public class RunicTechniques {

    public static final DeferredRegister<ITechnique> TECHNIQUES =
            DeferredRegister.create(AscensionRegistries.Techniques.TECHNIQUES_REGISTRY, RunicAscension.MOD_ID);

    public static final DeferredHolder<ITechnique, BasicRunicTechnique> RUNIC_APPRENTICE =
            TECHNIQUES.register("runic_apprentice", BasicRunicTechnique::new);

    public static final DeferredHolder<ITechnique, InnerInscriptionTechnique> INNER_INSCRIPTION_METHOD =
            TECHNIQUES.register("inner_inscription_method", InnerInscriptionTechnique::new);

    public static final DeferredHolder<ITechnique, OuterFormulaTechnique> OUTER_FORMULA_METHOD =
            TECHNIQUES.register("outer_formula_method", OuterFormulaTechnique::new);

    public static final DeferredHolder<ITechnique, TraceVisualizationTechnique> TRACE_VISUALIZATION_METHOD =
            TECHNIQUES.register("trace_visualization_method", TraceVisualizationTechnique::new);

    public static void register(IEventBus bus) {
        TECHNIQUES.register(bus);
    }

}
