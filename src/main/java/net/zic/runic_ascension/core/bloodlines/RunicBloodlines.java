package net.zic.runic_ascension.core.bloodlines;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.refactor_packages.bloodlines.IBloodline;
import net.thejadeproject.ascension.refactor_packages.bloodlines.generic.GenericBloodline;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;
import net.thejadeproject.ascension.refactor_packages.stats.custom.ModStats;
import net.thejadeproject.ascension.util.ModAttributes;
import net.zic.runic_ascension.RunicAscension;

public class RunicBloodlines {

    public static final DeferredRegister<IBloodline> BLOODLINES =
            DeferredRegister.create(AscensionRegistries.Bloodlines.BLOODLINE_REGISTRY, RunicAscension.MOD_ID);

    private static final ResourceLocation RUNIC_KEY_TWO = rl("ink_blood_lineage");
    private static final ResourceLocation RUNIC_KEY_THREE = rl("burning_blood_runes");
//    private static final ResourceLocation RUNIC_KEY_FOUR = rl("runic_tester_bloodline");
//    private static final ResourceLocation RUNIC_KEY_FIVE = rl("runic_tester_bloodline");
//    private static final ResourceLocation RUNIC_KEY_SIX = rl("runic_tester_bloodline");
//    private static final ResourceLocation RUNIC_KEY_SEVEN = rl("runic_tester_bloodline");
//    private static final ResourceLocation RUNIC_KEY_EIGHT = rl("runic_tester_bloodline");
//    private static final ResourceLocation RUNIC_KEY_NINE = rl("runic_tester_bloodline");

    public static final DeferredHolder<IBloodline, GenericBloodline> INK_BLOOD_LINEAGE =
            BLOODLINES.register("ink_blood_lineage", () ->
                            new GenericBloodline(Component.translatable("runic_ascension.bloodline.ink_blood_lineage"))
                                    .setShortDescription(Component.translatable("runic_ascension.bloodline.ink_blood_lineage.short"))
                                    .setDescription(Component.translatable("runic_ascension.bloodline.ink_blood_lineage.desc"))
                                    .addFlatAttribute(ModAttributes.MAX_QI, 100, RUNIC_KEY_TWO)
                                    .addFlatAttribute(ModAttributes.QI_REGEN_RATE, 0.5, RUNIC_KEY_TWO)
                                    // Need to add custom hidden attributes stability and things that can be called by runic sequences
                                    // Also need to add a way to specify runes that get a boost, maybe a custom Bloodline Type Class? idk...
                                    //.addFlatAttribute(RunicAttributes.SEQUENCE_STABILITY, 0.5, RUNIC_KEY_TWO)
                                    //.addRunicBoost(RunicRune.X, Y, RUNIC_KEY_TWO)
            );

    public static final DeferredHolder<IBloodline, GenericBloodline> BURNING_BLOOD_RUNES =
            BLOODLINES.register("burning_blood_runes", () ->
                            new GenericBloodline(Component.translatable("runic_ascension.bloodline.burning_blood_runes"))
                                    .setShortDescription(Component.translatable("runic_ascension.bloodline.burning_blood_runes.short"))
                                    .setDescription(Component.translatable("runic_ascension.bloodline.burning_blood_runes.desc"))
                                    .addFlatAttribute(Attributes.MAX_HEALTH, 50, RUNIC_KEY_THREE)
                                    // Need to add custom hidden attributes stability and things that can be called by runic sequences
                                    // Also need to add a way to specify runes that get a boost, maybe a custom Bloodline Type Class? idk...
                                    // Also need to figure out a way to make bloodlines boost stats as well? idk...
                                    //.addFlatAttribute(ModStats.STRENGTH, 2, RUNIC_KEY_THREE)
                                    //.addFlatAttribute(RunicAttributes.SEQUENCE_VIOLENCE, 0.5, RUNIC_KEY_THREE)
            );




    public static void register(IEventBus bus) {
        BLOODLINES.register(bus);
    }
    private static ResourceLocation rl(String path) { return ResourceLocation.fromNamespaceAndPath(RunicAscension.MOD_ID, path); }
}
