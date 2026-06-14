package net.zic.runic_ascension.core.physiques;

import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.refactor_packages.paths.ModPaths;
import net.thejadeproject.ascension.refactor_packages.physiques.IPhysique;
import net.thejadeproject.ascension.refactor_packages.physiques.custom.EvolvingPhysique;
import net.thejadeproject.ascension.refactor_packages.physiques.custom.GenericPhysique;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;
import net.zic.runic_ascension.RunicAscension;
import net.zic.runic_ascension.core.paths.RunicPaths;

public class RunicPhysiques {

    public static final DeferredRegister<IPhysique> PHYSIQUES =
            DeferredRegister.create(AscensionRegistries.Physiques.PHSIQUES_REGISTRY, RunicAscension.MOD_ID);

    // A simple Runic Physique findable in some chest look? idk...
    public static final DeferredHolder<IPhysique, ? extends GenericPhysique> RUNIC_SUBJECT = PHYSIQUES.register("runic_subject", () ->
            new EvolvingPhysique(Component.translatable("runic_ascension.physiques.runic_subject"))
                    .addEvolution(RunicPhysiques.INVOKING_SOUL.getId())
                    .addEvolution(RunicPhysiques.INSCRIBING_BODY.getId())
                    .addEvolution(RunicPhysiques.ETCHING_MERIDIANS.getId())
                    .setShortDescription(Component.translatable("runic_ascension.physiques.runic_subject.description.short"))
                    .setDescription(Component.translatable("runic_ascension.physiques.runic_subject.description"))
                    .addPath(RunicPaths.RUNIC.getId())
                    .addPathBonus(RunicPaths.RUNIC.getId(), 0.5)
    );

    // Second physique attainable in the mod, also focused on the Runic Path
    public static final DeferredHolder<IPhysique, ? extends GenericPhysique> RUNIC_EYES = PHYSIQUES.register("runic_eyes", () ->
            new EvolvingPhysique(Component.translatable("runic_ascension.physiques.runic_eyes"))
                    .addEvolution(RunicPhysiques.RUNIC_PERFECTION.getId())
                    .setShortDescription(Component.translatable("runic_ascension.physiques.runic_eyes.description.short"))
                    .setDescription(Component.translatable("runic_ascension.physiques.runic_eyes.description"))
                    .addPath(RunicPaths.RUNIC.getId())
                    .addPathBonus(RunicPaths.RUNIC.getId(), 2.0)
    );

    // Temp Physiques just so others can practice the Runic Path alongside other paths
    public static final DeferredHolder<IPhysique, ? extends GenericPhysique> ESSENCE_RUNE_PHYSIQUE = PHYSIQUES.register("essence_rune_physique", () ->
            new GenericPhysique(Component.translatable("runic_ascension.physiques.essence_rune_physique"))
                    .setShortDescription(Component.translatable("runic_ascension.physiques.essence_rune_physique.description.short"))
                    .setDescription(Component.translatable("runic_ascension.physiques.essence_rune_physique.description"))
                    .addPath(RunicPaths.RUNIC.getId())
                    .addPath(ModPaths.ESSENCE.getId())
                    .addPathBonus(RunicPaths.RUNIC.getId(), 1.5)
                    .addPathBonus(ModPaths.ESSENCE.getId(), 1.0)
    );
    public static final DeferredHolder<IPhysique, ? extends GenericPhysique> BODY_RUNE_PHYSIQUE = PHYSIQUES.register("body_rune_physique", () ->
            new GenericPhysique(Component.translatable("runic_ascension.physiques.body_rune_physique"))
                    .setShortDescription(Component.translatable("runic_ascension.physiques.body_rune_physique.description.short"))
                    .setDescription(Component.translatable("runic_ascension.physiques.body_rune_physique.description"))
                    .addPath(RunicPaths.RUNIC.getId())
                    .addPath(ModPaths.BODY.getId())
                    .addPathBonus(RunicPaths.RUNIC.getId(), 1.5)
                    .addPathBonus(ModPaths.BODY.getId(), 1.0)
    );
    public static final DeferredHolder<IPhysique, ? extends GenericPhysique> SOUL_RUNE_PHYSIQUE = PHYSIQUES.register("soul_rune_physique", () ->
            new GenericPhysique(Component.translatable("runic_ascension.physiques.soul_rune_physique"))
                    .setShortDescription(Component.translatable("runic_ascension.physiques.soul_rune_physique.description.short"))
                    .setDescription(Component.translatable("runic_ascension.physiques.soul_rune_physique.description"))
                    .addPath(RunicPaths.RUNIC.getId())
                    .addPath(ModPaths.SOUL.getId())
                    .addPathBonus(RunicPaths.RUNIC.getId(), 1.5)
                    .addPathBonus(ModPaths.SOUL.getId(), 1.0)
    );


    // These four are unobtainable for now
    public static final DeferredHolder<IPhysique, ? extends GenericPhysique> INVOKING_SOUL= PHYSIQUES.register("invoking_soul", () ->
            new GenericPhysique(Component.translatable("runic_ascension.physiques.invoking_soul"))
                    .setShortDescription(Component.translatable("runic_ascension.physiques.invoking_soul.description.short"))
                    .setDescription(Component.translatable("runic_ascension.physiques.invoking_soul.description"))
                    .addPath(RunicPaths.RUNIC.getId())
                    .addPath(ModPaths.SOUL.getId())
                    .addPathBonus(RunicPaths.RUNIC.getId(), 3.0)
                    .addPathBonus(ModPaths.SOUL.getId(), 2.0)
    );

    public static final DeferredHolder<IPhysique, ? extends GenericPhysique> INSCRIBING_BODY= PHYSIQUES.register("inscribing_body", () ->
            new GenericPhysique(Component.translatable("runic_ascension.physiques.inscribing_body"))
                    .setShortDescription(Component.translatable("runic_ascension.physiques.inscribing_body.description.short"))
                    .setDescription(Component.translatable("runic_ascension.physiques.inscribing_body.description"))
                    .addPath(RunicPaths.RUNIC.getId())
                    .addPath(ModPaths.BODY.getId())
                    .addPathBonus(RunicPaths.RUNIC.getId(), 3.0)
                    .addPathBonus(ModPaths.BODY.getId(), 2.0)
    );

    public static final DeferredHolder<IPhysique, ? extends GenericPhysique> ETCHING_MERIDIANS= PHYSIQUES.register("etching_meridians", () ->
            new GenericPhysique(Component.translatable("runic_ascension.physiques.etching_meridians"))
                    .setShortDescription(Component.translatable("runic_ascension.physiques.etching_meridians.description.short"))
                    .setDescription(Component.translatable("runic_ascension.physiques.etching_meridians.description"))
                    .addPath(RunicPaths.RUNIC.getId())
                    .addPath(ModPaths.ESSENCE.getId())
                    .addPathBonus(RunicPaths.RUNIC.getId(), 3.0)
                    .addPathBonus(ModPaths.ESSENCE.getId(), 2.0)
    );

    public static final DeferredHolder<IPhysique, ? extends GenericPhysique> RUNIC_PERFECTION= PHYSIQUES.register("runic_perfection", () ->
            new GenericPhysique(Component.translatable("runic_ascension.physiques.runic_perfection"))
                    .setShortDescription(Component.translatable("runic_ascension.physiques.runic_perfection.description.short"))
                    .setDescription(Component.translatable("runic_ascension.physiques.runic_perfection.description"))
                    .addPath(RunicPaths.RUNIC.getId())
                    .addPathBonus(RunicPaths.RUNIC.getId(), 10.0)
    );

    public static void register(IEventBus bus) {
        PHYSIQUES.register(bus);
    }

}
