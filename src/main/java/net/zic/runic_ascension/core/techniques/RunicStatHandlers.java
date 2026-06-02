package net.zic.runic_ascension.core.techniques;

import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.refactor_packages.stats.custom.ModStats;
import net.thejadeproject.ascension.refactor_packages.techniques.custom.stat_change_handlers.BasicStatChangeHandler;
import net.thejadeproject.ascension.refactor_packages.util.value_modifiers.ModifierOperation;
import net.thejadeproject.ascension.refactor_packages.util.value_modifiers.ValueContainerModifier;
import net.zic.runic_ascension.RunicAscension;

public class RunicStatHandlers {

    public static final ResourceLocation BASE_RUNIC_KEY =
            ResourceLocation.fromNamespaceAndPath(RunicAscension.MOD_ID, "base_runic");

    public static final BasicStatChangeHandler BASIC_RUNIC_HANDLER = new BasicStatChangeHandler()
            .addMinorRealmStatModifier(ModStats.INTELLIGENCE.getId(), new ValueContainerModifier(3, ModifierOperation.ADD_BASE, BASE_RUNIC_KEY))
            .addMinorRealmStatModifier(ModStats.VITALITY.getId(), new ValueContainerModifier(1.25, ModifierOperation.ADD_BASE, BASE_RUNIC_KEY))
            .addMinorRealmStatModifier(ModStats.AGILITY.getId(), new ValueContainerModifier(1.25, ModifierOperation.ADD_BASE, BASE_RUNIC_KEY))
            .addMajorRealmStatModifier(ModStats.INTELLIGENCE.getId(), new ValueContainerModifier(0.13, ModifierOperation.MULTIPLY_FINAL, BASE_RUNIC_KEY))
            .addMajorRealmStatModifier(ModStats.VITALITY.getId(), new ValueContainerModifier(0.07, ModifierOperation.MULTIPLY_FINAL, BASE_RUNIC_KEY))
            .addMajorRealmStatModifier(ModStats.AGILITY.getId(), new ValueContainerModifier(0.07, ModifierOperation.MULTIPLY_FINAL, BASE_RUNIC_KEY));

}
