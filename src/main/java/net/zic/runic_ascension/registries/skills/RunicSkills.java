package net.zic.runic_ascension.registries.skills;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zic.runic_ascension.RunicAscension;
import net.zic.runic_ascension.registries.paths.RunicPath;

public class RunicSkills {

    public static final DeferredRegister<ISkill> SKILLS =
            DeferredRegister.create(AscensionRegistries.SKILLS, RunicAscension.MOD_ID);

    public static final DeferredHolder<ITechnique, ITechnique> OPEN_RUNIC_CASTING =
            SKILLS.register("open_runic_casting", OpenRunicCasting::new);

    public static void register(IEventBus bus) {
        SKILLS.register(bus);
    }

}
