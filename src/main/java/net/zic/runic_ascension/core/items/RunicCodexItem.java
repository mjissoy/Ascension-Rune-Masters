package net.zic.runic_ascension.core.items;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.zic.runic_ascension.content.RunicPathHelper;
import net.zic.runic_ascension.network.client_bound.OpenRunicCodexScreenPayload;

public class RunicCodexItem extends Item {

    public RunicCodexItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (level.isClientSide) {
            return InteractionResultHolder.success(stack);
        }

        if (!RunicPathHelper.hasEnteredRunicPath(player)) {
            player.sendSystemMessage(Component.translatable("runic_ascension.runic.codex.not_on_path"));
            return InteractionResultHolder.success(stack);
        }

        if (player instanceof ServerPlayer serverPlayer) {
            OpenRunicCodexScreenPayload.sendTo(serverPlayer);
        }

        return InteractionResultHolder.success(stack);
    }

    // TODO: Store favourite sequences, known sequence history, and maybe cached rune notes.
}
