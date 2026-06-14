package net.zic.runic_ascension.util.runic_sight;

import net.minecraft.resources.ResourceLocation;

/**
 * A symbolic trace that Runic Sight can find in the world.
 *
 * @param runeId the rune carried by this trace
 * @param layer how deeply the trace is buried in the target
 * @param focusGainMultiplier how quickly this trace can be understood through repeated observation
 * @param originKey a small dev/reference key describing where the trace came from
 */
public record RunicSightTrace(
        ResourceLocation runeId,
        RunicSightLayer layer,
        float focusGainMultiplier,
        String originKey
) {

    public static RunicSightTrace surface(ResourceLocation runeId, String originKey) {
        return new RunicSightTrace(runeId, RunicSightLayer.SURFACE, 1.0F, originKey);
    }

    public static RunicSightTrace deep(ResourceLocation runeId, String originKey) {
        return new RunicSightTrace(runeId, RunicSightLayer.DEEP, 0.85F, originKey);
    }

    public static RunicSightTrace hidden(ResourceLocation runeId, String originKey) {
        return new RunicSightTrace(runeId, RunicSightLayer.HIDDEN, 0.65F, originKey);
    }

    public enum RunicSightLayer {
        SURFACE,
        DEEP,
        HIDDEN
    }
}
