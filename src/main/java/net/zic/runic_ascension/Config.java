package net.zic.runic_ascension;

import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
public class Config {
    public static ModConfigSpec SPEC;

    static {
        ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
        ModConfigSpec SPEC = BUILDER.build();
    }
}
