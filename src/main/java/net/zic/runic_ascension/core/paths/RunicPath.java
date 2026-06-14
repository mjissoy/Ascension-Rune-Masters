package net.zic.runic_ascension.core.paths;

import net.minecraft.network.chat.Component;
import net.thejadeproject.ascension.refactor_packages.paths.custom.FoundationPath;

public class RunicPath extends FoundationPath {

    public RunicPath() {
        super(Component.translatable("runic_ascension.path.runic"));

        setDescription(Component.translatable("runic_ascension.path.runic.description"));
        addFoundationRequirement(0,100000);
        addFoundationRequirement(1,200000);
        addFoundationRequirement(2,300000);
        addFoundationRequirement(3,400000);
        addFoundationRequirement(4,1000000);
        addMajorRealmName("runic_ascension.path.runic.trace_sensing");
        addMajorRealmName("runic_ascension.path.runic.rune_knowing");
        addMajorRealmName("runic_ascension.path.runic.script_visualisation");
        addMajorRealmName("runic_ascension.path.runic.pattern_enlightenment");
        addMajorRealmName("runic_ascension.path.runic.rune_breath");
        addMajorRealmName("runic_ascension.path.runic.soul_inscription");
        addMajorRealmName("runic_ascension.path.runic.living_script");
        addMajorRealmName("runic_ascension.path.runic.dao_seed");
        addMajorRealmName("runic_ascension.path.runic.origin_spark");
        addMajorRealmName("runic_ascension.path.runic.infinite_script");
    }

//    @Override
//    public int getMaxMajorRealm() {
//        return 10;
//    }

//    @Override
//    public int getMaxMinorRealm(int majorRealm) {
//        return 9;
//    }

}