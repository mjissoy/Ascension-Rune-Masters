package net.zic.runic_ascension.core.techniques;

import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.refactor_packages.stats.custom.ModStats;
import net.thejadeproject.ascension.refactor_packages.techniques.custom.stat_change_handlers.BasicStatChangeHandler;
import net.thejadeproject.ascension.refactor_packages.util.value_modifiers.ModifierOperation;
import net.thejadeproject.ascension.refactor_packages.util.value_modifiers.ValueContainerModifier;
import net.zic.runic_ascension.RunicAscension;

public class RunicStatHandlers {

    // Keys
    public static final ResourceLocation BASE_RUNIC_KEY = ResourceLocation.fromNamespaceAndPath(RunicAscension.MOD_ID, "base_runic");
    public static final ResourceLocation INNER_INSCRIPTION_KEY = ResourceLocation.fromNamespaceAndPath(RunicAscension.MOD_ID, "inner_inscription_method");
    public static final ResourceLocation OUTER_FORMULA_KEY = ResourceLocation.fromNamespaceAndPath(RunicAscension.MOD_ID, "outer_formula_method");
    public static final ResourceLocation TRACE_VISUALIZATION_KEY = ResourceLocation.fromNamespaceAndPath(RunicAscension.MOD_ID, "trace_visualization_method");

    // Handlers
    public static final BasicStatChangeHandler BASIC_RUNIC_HANDLER = new BasicStatChangeHandler()
            .addMinorRealmStatModifier(ModStats.INTELLIGENCE.getId(), new ValueContainerModifier(3.0, ModifierOperation.ADD_BASE, BASE_RUNIC_KEY))
            .addMinorRealmStatModifier(ModStats.VITALITY.getId(), new ValueContainerModifier(1.25, ModifierOperation.ADD_BASE, BASE_RUNIC_KEY))
            .addMinorRealmStatModifier(ModStats.AGILITY.getId(), new ValueContainerModifier(1.25, ModifierOperation.ADD_BASE, BASE_RUNIC_KEY))
            .addMinorRealmStatModifier(ModStats.STRENGTH.getId(), new ValueContainerModifier(0.75, ModifierOperation.ADD_BASE, BASE_RUNIC_KEY))
            .addMajorRealmStatModifier(ModStats.INTELLIGENCE.getId(), new ValueContainerModifier(0.13, ModifierOperation.MULTIPLY_FINAL, BASE_RUNIC_KEY))
            .addMajorRealmStatModifier(ModStats.VITALITY.getId(), new ValueContainerModifier(0.07, ModifierOperation.MULTIPLY_FINAL, BASE_RUNIC_KEY))
            .addMajorRealmStatModifier(ModStats.AGILITY.getId(), new ValueContainerModifier(0.07, ModifierOperation.MULTIPLY_FINAL, BASE_RUNIC_KEY));

    public static final BasicStatChangeHandler INNER_INSCRIPTION_HANDLER = new BasicStatChangeHandler()
            .addMinorRealmStatModifier(ModStats.VITALITY.getId(), new ValueContainerModifier(3.5, ModifierOperation.ADD_BASE, INNER_INSCRIPTION_KEY))
            .addMinorRealmStatModifier(ModStats.INTELLIGENCE.getId(), new ValueContainerModifier(2.0, ModifierOperation.ADD_BASE, INNER_INSCRIPTION_KEY))
            .addMinorRealmStatModifier(ModStats.STRENGTH.getId(), new ValueContainerModifier(1.5, ModifierOperation.ADD_BASE, INNER_INSCRIPTION_KEY))
            .addMinorRealmStatModifier(ModStats.AGILITY.getId(), new ValueContainerModifier(0.5, ModifierOperation.ADD_BASE, INNER_INSCRIPTION_KEY))
            .addMajorRealmStatModifier(ModStats.VITALITY.getId(), new ValueContainerModifier(0.15, ModifierOperation.MULTIPLY_FINAL, INNER_INSCRIPTION_KEY))
            .addMajorRealmStatModifier(ModStats.INTELLIGENCE.getId(), new ValueContainerModifier(0.08, ModifierOperation.MULTIPLY_FINAL, INNER_INSCRIPTION_KEY))
            .addMajorRealmStatModifier(ModStats.STRENGTH.getId(), new ValueContainerModifier(0.08, ModifierOperation.MULTIPLY_FINAL, INNER_INSCRIPTION_KEY));

    public static final BasicStatChangeHandler OUTER_FORMULA_HANDLER = new BasicStatChangeHandler()
            .addMinorRealmStatModifier(ModStats.INTELLIGENCE.getId(), new ValueContainerModifier(4.0, ModifierOperation.ADD_BASE, OUTER_FORMULA_KEY))
            .addMinorRealmStatModifier(ModStats.STRENGTH.getId(), new ValueContainerModifier(1.5, ModifierOperation.ADD_BASE, OUTER_FORMULA_KEY))
            .addMinorRealmStatModifier(ModStats.AGILITY.getId(), new ValueContainerModifier(1.0, ModifierOperation.ADD_BASE, OUTER_FORMULA_KEY))
            .addMinorRealmStatModifier(ModStats.VITALITY.getId(), new ValueContainerModifier(0.75, ModifierOperation.ADD_BASE, OUTER_FORMULA_KEY))
            .addMajorRealmStatModifier(ModStats.INTELLIGENCE.getId(), new ValueContainerModifier(0.18, ModifierOperation.MULTIPLY_FINAL, OUTER_FORMULA_KEY))
            .addMajorRealmStatModifier(ModStats.STRENGTH.getId(), new ValueContainerModifier(0.08, ModifierOperation.MULTIPLY_FINAL, OUTER_FORMULA_KEY))
            .addMajorRealmStatModifier(ModStats.AGILITY.getId(), new ValueContainerModifier(0.05, ModifierOperation.MULTIPLY_FINAL, OUTER_FORMULA_KEY));

    public static final BasicStatChangeHandler TRACE_VISUALIZATION_HANDLER = new BasicStatChangeHandler()
            .addMinorRealmStatModifier(ModStats.INTELLIGENCE.getId(), new ValueContainerModifier(3.25, ModifierOperation.ADD_BASE, TRACE_VISUALIZATION_KEY))
            .addMinorRealmStatModifier(ModStats.AGILITY.getId(), new ValueContainerModifier(2.5, ModifierOperation.ADD_BASE, TRACE_VISUALIZATION_KEY))
            .addMinorRealmStatModifier(ModStats.VITALITY.getId(), new ValueContainerModifier(1.0, ModifierOperation.ADD_BASE, TRACE_VISUALIZATION_KEY))
            .addMinorRealmStatModifier(ModStats.STRENGTH.getId(), new ValueContainerModifier(0.5, ModifierOperation.ADD_BASE, TRACE_VISUALIZATION_KEY))
            .addMajorRealmStatModifier(ModStats.INTELLIGENCE.getId(), new ValueContainerModifier(0.14, ModifierOperation.MULTIPLY_FINAL, TRACE_VISUALIZATION_KEY))
            .addMajorRealmStatModifier(ModStats.AGILITY.getId(), new ValueContainerModifier(0.14, ModifierOperation.MULTIPLY_FINAL, TRACE_VISUALIZATION_KEY))
            .addMajorRealmStatModifier(ModStats.VITALITY.getId(), new ValueContainerModifier(0.05, ModifierOperation.MULTIPLY_FINAL, TRACE_VISUALIZATION_KEY));
}