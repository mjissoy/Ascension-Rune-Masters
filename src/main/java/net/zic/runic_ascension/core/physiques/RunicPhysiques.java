package net.zic.runic_ascension.core.physiques;

import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.refactor_packages.paths.ModPaths;
import net.thejadeproject.ascension.refactor_packages.physiques.IPhysique;
import net.thejadeproject.ascension.refactor_packages.physiques.custom.GenericPhysique;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;
import net.zic.runic_ascension.RunicAscension;
import net.zic.runic_ascension.core.paths.RunicPaths;

public class RunicPhysiques {

    public static final DeferredRegister<IPhysique> PHYSIQUES =
            DeferredRegister.create(AscensionRegistries.Physiques.PHSIQUES_REGISTRY, RunicAscension.MOD_ID);

    public static final DeferredHolder<IPhysique, ? extends GenericPhysique> RUNIC_TESTING = PHYSIQUES.register("runic_testing", () ->
            new GenericPhysique(Component.translatable("runic_ascension.physiques.runic_testing"))
                    .setShortDescription(Component.translatable("runic_ascension.physiques.runic_testing.description.short"))
                    .setDescription(Component.translatable("runic_ascension.physiques.runic_testing.description"))
                    .addPath(RunicPaths.RUNIC.getId())
                    .addPathBonus(RunicPaths.RUNIC.getId(), 5.0)
    );

    /* Ideas for Physiques... */

//    public static final DeferredHolder<IPhysique, ? extends GenericPhysique> RUNIC_EYES = PHYSIQUES.register("runic_eyes", () ->
//            new EvolvingPhysique(Component.translatable("runic_ascension.physiques.runic_eyes"))
//                    .addEvolution(RunicPhysiques.RUNIC_SOUL.getId())
//                    .setShortDescription(Component.translatable("runic_ascension.physiques.runic_eyes.description.short"))
//                    .setDescription(Component.translatable("runic_ascension.physiques.runic_eyes.description"))
//                    .addPath(RunicPaths.RUNIC.getId())
//                    .addPathBonus(RunicPaths.RUNIC.getId(), 1.5)
//    );
//
//    public static final DeferredHolder<IPhysique, ? extends GenericPhysique> RUNIC_HEART = PHYSIQUES.register("runic_heart", () ->
//            new EvolvingPhysique(Component.translatable("runic_ascension.physiques.runic_heart"))
//                    .addEvolution(RunicPhysiques.RUNIC_BODY.getId())
//                    .setShortDescription(Component.translatable("runic_ascension.physiques.runic_heart.description.short"))
//                    .setDescription(Component.translatable("runic_ascension.physiques.runic_heart.description"))
//                    .addPath(RunicPaths.RUNIC.getId())
//                    .addPathBonus(RunicPaths.RUNIC.getId(), 1.5)
//    );
//
//    public static final DeferredHolder<IPhysique, ? extends GenericPhysique> RUNIC_SKIN = PHYSIQUES.register("runic_skin", () ->
//            new EvolvingPhysique(Component.translatable("runic_ascension.physiques.runic_skin"))
//                    .addEvolution(RunicPhysiques.RUNIC_MERIDIANS.getId())
//                    .setShortDescription(Component.translatable("runic_ascension.physiques.runic_skin.description.short"))
//                    .setDescription(Component.translatable("runic_ascension.physiques.runic_skin.description"))
//                    .addPath(RunicPaths.RUNIC.getId())
//                    .addPathBonus(RunicPaths.RUNIC.getId(), 1.5)
//    );
//
//    public static final DeferredHolder<IPhysique, ? extends GenericPhysique> RUNIC_SOUL= PHYSIQUES.register("runic_soul", () ->
//            new GenericPhysique(Component.translatable("runic_ascension.physiques.runic_soul"))
//                    .setShortDescription(Component.translatable("runic_ascension.physiques.runic_soul.description.short"))
//                    .setDescription(Component.translatable("runic_ascension.physiques.runic_soul.description"))
//                    .addPath(RunicPaths.RUNIC.getId())
//                    .addPath(ModPaths.SOUL.getId())
//                    .addPathBonus(RunicPaths.RUNIC.getId(), 5.0)
//                    .addPathBonus(ModPaths.SOUL.getId(), 4.0)
//    );
//
//    public static final DeferredHolder<IPhysique, ? extends GenericPhysique> RUNIC_BODY= PHYSIQUES.register("runic_body", () ->
//            new GenericPhysique(Component.translatable("runic_ascension.physiques.runic_body"))
//                    .setShortDescription(Component.translatable("runic_ascension.physiques.runic_body.description.short"))
//                    .setDescription(Component.translatable("runic_ascension.physiques.runic_body.description"))
//                    .addPath(RunicPaths.RUNIC.getId())
//                    .addPath(ModPaths.BODY.getId())
//                    .addPathBonus(RunicPaths.RUNIC.getId(), 5.0)
//                    .addPathBonus(ModPaths.BODY.getId(), 4.0)
//    );
//
    public static final DeferredHolder<IPhysique, ? extends GenericPhysique> RUNIC_MERIDIANS= PHYSIQUES.register("runic_meridians", () ->
            new GenericPhysique(Component.translatable("runic_ascension.physiques.runic_meridians"))
                    .setShortDescription(Component.translatable("runic_ascension.physiques.runic_meridians.description.short"))
                    .setDescription(Component.translatable("runic_ascension.physiques.runic_meridians.description"))
                    .addPath(RunicPaths.RUNIC.getId())
                    .addPath(ModPaths.ESSENCE.getId())
                    .addPathBonus(RunicPaths.RUNIC.getId(), 5.0)
                    .addPathBonus(ModPaths.ESSENCE.getId(), 4.0)
    );

    public static void register(IEventBus bus) {
        PHYSIQUES.register(bus);
    }

}
