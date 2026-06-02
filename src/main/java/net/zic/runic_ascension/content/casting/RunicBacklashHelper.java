package net.zic.runic_ascension.content.casting;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public final class RunicBacklashHelper {

    private RunicBacklashHelper() {
    }

    public static void applyMinorBacklash(ServerPlayer player, String reason) {
        player.displayClientMessage(messageFor(reason), true);

        player.hurt(player.damageSources().magic(), 2.0F);
        player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 80, 0));
    }

    private static Component messageFor(String reason) {
        return switch (reason) {
            case "empty_sequence" -> Component.translatable("runic_ascension.runic.cast.empty_sequence");
            case "missing_core_runes" -> Component.translatable("runic_ascension.runic.cast.missing_core_runes");
            case "too_many_sources" -> Component.translatable("runic_ascension.runic.cast.too_many_sources");
            case "too_many_intents" -> Component.translatable("runic_ascension.runic.cast.too_many_intents");
            case "too_many_forms" -> Component.translatable("runic_ascension.runic.cast.too_many_forms");
            case "unstable_opening_modifier" -> Component.translatable("runic_ascension.runic.cast.unstable_opening_modifier");
            case "not_enough_qi" -> Component.translatable("runic_ascension.runic.cast.not_enough_qi");
            case "formula_too_complex" -> Component.translatable("runic_ascension.runic.cast.formula_too_complex");
            case "rune_beyond_comprehension" -> Component.translatable("runic_ascension.runic.cast.rune_beyond_comprehension");
            case "unstable_modifiers" -> Component.translatable("runic_ascension.runic.cast.unstable_modifiers");
            default -> Component.translatable("runic_ascension.runic.cast.failure");
        };
    }
}
