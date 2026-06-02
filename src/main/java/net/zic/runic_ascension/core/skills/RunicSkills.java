package net.zic.runic_ascension.core.skills;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;
import net.thejadeproject.ascension.refactor_packages.skills.ISkill;
import net.zic.runic_ascension.RunicAscension;
import net.zic.runic_ascension.core.skills.active.OpenRunicCastingSkill;
import net.zic.runic_ascension.core.skills.active.RunicSightSkill;

public final class RunicSkills {

    public static final DeferredRegister<ISkill> SKILLS =
            DeferredRegister.create(AscensionRegistries.Skills.SKILL_REGISTRY, RunicAscension.MOD_ID);

    public static final DeferredHolder<ISkill, OpenRunicCastingSkill> OPEN_RUNIC_CASTING =
            SKILLS.register("open_runic_casting", OpenRunicCastingSkill::new);

    public static final DeferredHolder<ISkill, RunicSightSkill> RUNIC_SIGHT =
            SKILLS.register("runic_sight", RunicSightSkill::new);

    private RunicSkills() {
    }

    public static void register(IEventBus bus) {
        SKILLS.register(bus);
    }
}
