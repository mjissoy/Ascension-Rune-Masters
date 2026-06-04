package net.zic.runic_ascension.core.items;

import net.minecraft.resources.ResourceLocation;
import net.zic.runic_ascension.RunicAscension;

/**
 * Defines the first equipment layer for runic casting.
 *
 * <p>Brushes are intentionally simple for the MVP: they add a small number of
 * rune slots and then bend the semantic formula stats in a distinct direction.
 * Later this can grow into grades, materials, durability, inscriptions, or
 * datapack-driven brush families.</p>
 */
public enum RunicBrushType {
    BASIC(
            "basic",
            "stabilise",
            1,
            1.00F,
            1.00F,
            1.00F,
            0.96F,
            0.92F,
            1.08F
    ),
    EARTH(
            "earth",
            "earth",
            1,
            0.94F,
            1.12F,
            0.90F,
            0.98F,
            0.82F,
            1.24F
    ),
    HEAVEN(
            "heaven",
            "light",
            1,
            1.04F,
            1.08F,
            1.14F,
            0.88F,
            0.92F,
            1.10F
    ),
    HELL(
            "hell",
            "violent",
            1,
            1.22F,
            0.96F,
            1.02F,
            1.10F,
            1.28F,
            0.82F
    );

    private final String id;
    private final String affinityRunePath;
    private final int extraRuneSlots;
    private final float damageMultiplier;
    private final float durationMultiplier;
    private final float rangeMultiplier;
    private final float qiCostMultiplier;
    private final float backlashMultiplier;
    private final float stabilityMultiplier;

    RunicBrushType(
            String id,
            String affinityRunePath,
            int extraRuneSlots,
            float damageMultiplier,
            float durationMultiplier,
            float rangeMultiplier,
            float qiCostMultiplier,
            float backlashMultiplier,
            float stabilityMultiplier
    ) {
        this.id = id;
        this.affinityRunePath = affinityRunePath;
        this.extraRuneSlots = extraRuneSlots;
        this.damageMultiplier = damageMultiplier;
        this.durationMultiplier = durationMultiplier;
        this.rangeMultiplier = rangeMultiplier;
        this.qiCostMultiplier = qiCostMultiplier;
        this.backlashMultiplier = backlashMultiplier;
        this.stabilityMultiplier = stabilityMultiplier;
    }

    public String id() {
        return id;
    }

    public String translationKey() {
        return "runic_ascension.runic.brush.type." + id;
    }

    public String tooltipKey() {
        return "runic_ascension.runic.brush.tooltip." + id;
    }

    public ResourceLocation affinityRune() {
        return ResourceLocation.fromNamespaceAndPath(RunicAscension.MOD_ID, affinityRunePath);
    }

    public int extraRuneSlots() {
        return extraRuneSlots;
    }

    public float damageMultiplier() {
        return damageMultiplier;
    }

    public float durationMultiplier() {
        return durationMultiplier;
    }

    public float rangeMultiplier() {
        return rangeMultiplier;
    }

    public float qiCostMultiplier() {
        return qiCostMultiplier;
    }

    public float backlashMultiplier() {
        return backlashMultiplier;
    }

    public float stabilityMultiplier() {
        return stabilityMultiplier;
    }

    public boolean isPowerLeaning() {
        return damageMultiplier > 1.10F || backlashMultiplier > 1.10F;
    }

    public boolean isStabilityLeaning() {
        return stabilityMultiplier > 1.10F || backlashMultiplier < 0.90F;
    }
}
