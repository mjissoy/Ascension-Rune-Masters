package net.zic.runic_ascension.core.bloodlines;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.refactor_packages.bloodlines.IBloodline;
import net.thejadeproject.ascension.refactor_packages.bloodlines.generic.GenericBloodline;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;
import net.thejadeproject.ascension.util.ModAttributes;
import net.zic.runic_ascension.RunicAscension;

public class RunicBloodlines {

    public static final DeferredRegister<IBloodline> BLOODLINES =
            DeferredRegister.create(AscensionRegistries.Bloodlines.BLOODLINE_REGISTRY, RunicAscension.MOD_ID);

    private static final ResourceLocation RUNIC_KEY = rl("runic_tester_bloodline");

    public static final DeferredHolder<IBloodline, GenericBloodline> RUNIC_TESTER_BLOODLINE =
            BLOODLINES.register("runic_tester_bloodline", () ->
                            new GenericBloodline(Component.translatable("runic_ascension.bloodline.runic_tester_bloodline"))
                                    .setShortDescription(Component.translatable("runic_ascension.bloodline.runic_tester_bloodline.short"))
                                    .setDescription(Component.translatable("runic_ascension.bloodline.runic_tester_bloodline.desc"))
                                    .addFlatAttribute(ModAttributes.MAX_QI, 100, RUNIC_KEY)
                                    .addFlatAttribute(ModAttributes.QI_REGEN_RATE, 0.5, RUNIC_KEY)
            );

    public static void register(IEventBus bus) {
        BLOODLINES.register(bus);
    }

    private static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(RunicAscension.MOD_ID, path);
    }

}
