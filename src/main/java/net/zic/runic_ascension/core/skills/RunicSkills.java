package net.zic.runic_ascension.core.skills;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;
import net.thejadeproject.ascension.refactor_packages.skills.ISkill;
import net.zic.runic_ascension.RunicAscension;
import net.zic.runic_ascension.core.skills.active.attack.CinderBrand;
import net.zic.runic_ascension.core.skills.active.attack.ScriptSever;
import net.zic.runic_ascension.core.skills.active.defense.StoneWard;
import net.zic.runic_ascension.core.skills.active.defense.WaterMantle;
import net.zic.runic_ascension.core.skills.active.utility.TraceStep;

public final class RunicSkills {
    private RunicSkills() {}

    public static final DeferredRegister<ISkill> SKILLS =
            DeferredRegister.create(AscensionRegistries.Skills.SKILL_REGISTRY, RunicAscension.MOD_ID);

    // Fundamental
    public static final DeferredHolder<ISkill, OpenRunicCastingSkill> OPEN_RUNIC_CASTING =
            SKILLS.register("open_runic_casting", OpenRunicCastingSkill::new);
    public static final DeferredHolder<ISkill, RunicSightSkill> RUNIC_SIGHT =
            SKILLS.register("runic_sight", RunicSightSkill::new);


    // Active
        // Attack
    public static final DeferredHolder<ISkill, ScriptSever> SCRIPT_SEVER =
            SKILLS.register("script_sever", ScriptSever::new);
    public static final DeferredHolder<ISkill, CinderBrand> CINDER_BRAND =
            SKILLS.register("cinder_brand", CinderBrand::new);

        // Defense
    public static final DeferredHolder<ISkill, StoneWard> STONE_WARD =
            SKILLS.register("stone_ward", StoneWard::new);
    public static final DeferredHolder<ISkill, WaterMantle> WATER_MANTLE =
            SKILLS.register("water_mantle", WaterMantle::new);

        // Utility
    public static final DeferredHolder<ISkill, TraceStep> TRACE_STEP =
            SKILLS.register("trace_step", TraceStep::new);


    // Passive

    public static void register(IEventBus bus) {
        SKILLS.register(bus);
    }
}
