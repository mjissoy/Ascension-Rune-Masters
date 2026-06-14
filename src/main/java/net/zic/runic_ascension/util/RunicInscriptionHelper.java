package net.zic.runic_ascension.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.zic.runic_ascension.content.RunicPathHelper;
import net.zic.runic_ascension.content.RunicPlayerData;
import net.zic.runic_ascension.core.inscriptions.ModRunicInscriptions;
import net.zic.runic_ascension.core.inscriptions.RunicInscription;

import java.util.ArrayList;
import java.util.List;

public final class RunicInscriptionHelper {

    private RunicInscriptionHelper() {
    }

    public static int getTier(LivingEntity entity, RunicInscription inscription) {
        return inscription == null ? 0 : getTier(entity, inscription.getId());
    }

    public static int getTier(LivingEntity entity, ResourceLocation inscriptionId) {
        if (entity == null || inscriptionId == null) {
            return 0;
        }

        return RunicPathHelper.getRunicData(entity).getInscriptionTier(inscriptionId);
    }

    public static boolean hasInscription(LivingEntity entity, RunicInscription inscription) {
        return getTier(entity, inscription) > 0;
    }

    public static boolean canUnlockNextTier(ServerPlayer player, RunicInscription inscription) {
        return getUnlockFailure(player, inscription) == null;
    }

    public static Component getUnlockFailure(ServerPlayer player, RunicInscription inscription) {
        if (player == null || inscription == null) {
            return Component.translatable("runic_ascension.inscription.unlock.invalid");
        }

        if (!RunicPathHelper.hasEnteredRunicPath(player)) {
            return Component.translatable("runic_ascension.inscription.unlock.not_on_path");
        }

        int currentTier = getTier(player, inscription);
        RunicInscription.Tier nextTier = inscription.getNextTier(currentTier);

        if (nextTier == null) {
            return Component.translatable("runic_ascension.inscription.unlock.maxed", inscription.getName());
        }

        int realm = RunicPathHelper.getRunicMajorRealm(player);

        if (realm < nextTier.minimumRunicRealm()) {
            return Component.translatable(
                    "runic_ascension.inscription.unlock.realm_too_low",
                    nextTier.minimumRunicRealm() + 1
            );
        }

        List<ResourceLocation> missingRunes = getMissingRunes(player, nextTier);

        if (!missingRunes.isEmpty()) {
            return Component.translatable(
                    "runic_ascension.inscription.unlock.missing_runes",
                    missingRunes.size()
            );
        }

        return null;
    }

    public static boolean unlockOrUpgrade(ServerPlayer player, RunicInscription inscription) {
        Component failure = getUnlockFailure(player, inscription);

        if (failure != null) {
            player.displayClientMessage(failure.copy().withStyle(ChatFormatting.RED), true);
            return false;
        }

        RunicPlayerData data = RunicPathHelper.getRunicData(player);
        int currentTier = data.getInscriptionTier(inscription.getId());
        int nextTier = currentTier + 1;

        data.setInscriptionTier(inscription.getId(), nextTier);
        RunicPathHelper.saveRunicData(player, data);

        player.displayClientMessage(
                Component.translatable(
                        "runic_ascension.inscription.unlock.success",
                        inscription.getTierName(nextTier)
                ).withStyle(ChatFormatting.LIGHT_PURPLE),
                true
        );

        return true;
    }

    public static List<ResourceLocation> getMissingRunes(ServerPlayer player, RunicInscription.Tier tier) {
        List<ResourceLocation> missing = new ArrayList<>();

        if (player == null || tier == null) {
            return missing;
        }

        for (ResourceLocation runeId : tier.requiredRunes()) {
            if (!RunicPathHelper.canUseRune(player, runeId)) {
                missing.add(runeId);
            }
        }

        return missing;
    }

    public static float fireDamageMultiplier(LivingEntity entity) {
        int tier = getTier(entity, ModRunicInscriptions.FLAMEGUARD);

        return switch (tier) {
            case 1 -> 0.80F;
            case 2 -> 0.60F;
            case 3 -> 0.35F;
            default -> 1.0F;
        };
    }

    public static float backlashMultiplier(LivingEntity entity) {
        int stillTier = getTier(entity, ModRunicInscriptions.STILL_SCRIPT);
        int flameTier = getTier(entity, ModRunicInscriptions.FLAMEGUARD);

        float multiplier = 1.0F;

        multiplier *= switch (stillTier) {
            case 1 -> 0.90F;
            case 2 -> 0.75F;
            case 3 -> 0.60F;
            default -> 1.0F;
        };

        multiplier *= switch (flameTier) {
            case 2 -> 0.95F;
            case 3 -> 0.90F;
            default -> 1.0F;
        };

        return multiplier;
    }

    public static float formulaStabilityBonus(LivingEntity entity) {
        int stillTier = getTier(entity, ModRunicInscriptions.STILL_SCRIPT);

        return switch (stillTier) {
            case 1 -> 0.04F;
            case 2 -> 0.08F;
            case 3 -> 0.14F;
            default -> 0.0F;
        };
    }

    public static double stoneWardRadiusBonus(LivingEntity entity) {
        int stoneTier = getTier(entity, ModRunicInscriptions.STONEHIDE);
        int mirrorTier = getTier(entity, ModRunicInscriptions.MIRROR_MARK);

        return stoneTier * 0.25D + mirrorTier * 0.15D;
    }

    public static float stoneWardPushMultiplier(LivingEntity entity) {
        int stoneTier = getTier(entity, ModRunicInscriptions.STONEHIDE);

        return 1.0F + stoneTier * 0.15F;
    }

    public static float waterMantleHealingMultiplier(LivingEntity entity) {
        int tier = getTier(entity, ModRunicInscriptions.CLEARFLOW);

        return 1.0F + tier * 0.25F;
    }

    public static double traceStepRangeBonus(LivingEntity entity) {
        int tier = getTier(entity, ModRunicInscriptions.WINDSTEP);

        return tier * 0.85D;
    }

    public static int traceStepCooldownReduction(LivingEntity entity) {
        int tier = getTier(entity, ModRunicInscriptions.WINDSTEP);

        return tier * 3;
    }

    public static void afterTraceStep(ServerPlayer player) {
        int tier = getTier(player, ModRunicInscriptions.WINDSTEP);

        if (tier <= 0) {
            return;
        }

        player.addEffect(new MobEffectInstance(
                MobEffects.MOVEMENT_SPEED,
                30 + tier * 15,
                Math.max(0, tier - 1),
                false,
                false,
                true
        ));
    }

    public static void afterWaterMantleHeal(ServerPlayer player) {
        int tier = getTier(player, ModRunicInscriptions.CLEARFLOW);

        if (tier <= 0) {
            return;
        }

        player.addEffect(new MobEffectInstance(
                MobEffects.REGENERATION,
                25 + tier * 10,
                0,
                false,
                false,
                true
        ));
    }

    public static boolean hasMirrorPulse(LivingEntity entity) {
        return getTier(entity, ModRunicInscriptions.MIRROR_MARK) >= 2;
    }
}