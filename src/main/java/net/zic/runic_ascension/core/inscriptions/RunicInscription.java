package net.zic.runic_ascension.core.inscriptions;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RunicInscription {

    private final ResourceLocation id;
    private final List<Tier> tiers = new ArrayList<>();

    public RunicInscription(ResourceLocation id) {
        this.id = id;
    }

    public ResourceLocation getId() {
        return id;
    }

    public String getPath() {
        return id.getPath();
    }

    public Component getName() {
        return Component.translatable("runic_ascension.inscription." + getPath());
    }

    public Component getShortDescription() {
        return Component.translatable("runic_ascension.inscription." + getPath() + ".description.short");
    }

    public Component getDescription() {
        return Component.translatable("runic_ascension.inscription." + getPath() + ".description");
    }

    public Component getTierName(int tier) {
        return Component.translatable("runic_ascension.inscription." + getPath() + ".tier." + tier);
    }

    public Component getTierDescription(int tier) {
        return Component.translatable("runic_ascension.inscription." + getPath() + ".tier." + tier + ".description");
    }

    public RunicInscription addTier(int tier, int minimumRunicRealm, ResourceLocation... requiredRunes) {
        tiers.add(new Tier(tier, minimumRunicRealm, List.of(requiredRunes)));
        tiers.sort(Comparator.comparingInt(Tier::tier));
        return this;
    }

    public int getMaxTier() {
        return tiers.stream()
                .mapToInt(Tier::tier)
                .max()
                .orElse(0);
    }

    public Tier getTier(int tier) {
        return tiers.stream()
                .filter(entry -> entry.tier() == tier)
                .findFirst()
                .orElse(null);
    }

    public Tier getNextTier(int currentTier) {
        return getTier(currentTier + 1);
    }

    public List<Tier> getTiers() {
        return List.copyOf(tiers);
    }

    public record Tier(
            int tier,
            int minimumRunicRealm,
            List<ResourceLocation> requiredRunes
    ) {
    }
}