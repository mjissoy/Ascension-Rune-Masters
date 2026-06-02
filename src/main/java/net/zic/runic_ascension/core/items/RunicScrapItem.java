package net.zic.runic_ascension.core.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class RunicScrapItem extends Item {

    private final String translationBase;
    private final int lineCount;

    public RunicScrapItem(Properties properties, String translationBase, int lineCount) {
        super(properties);
        this.translationBase = translationBase;
        this.lineCount = lineCount;
    }

    public String getTranslationBase() {
        return translationBase;
    }

    public int getLineCount() {
        return lineCount;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);

        tooltip.add(Component.translatable(translationBase + ".tooltip")
                .withStyle(ChatFormatting.DARK_PURPLE));
        tooltip.add(Component.translatable("runic_ascension.runic.scrap.tooltip.read")
                .withStyle(ChatFormatting.GRAY));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (level.isClientSide) {
            return InteractionResultHolder.success(stack);
        }

        player.sendSystemMessage(Component.literal(" "));
        player.sendSystemMessage(Component.translatable(translationBase + ".title")
                .withStyle(ChatFormatting.DARK_PURPLE));

        for (int i = 1; i <= lineCount; i++) {
            player.sendSystemMessage(Component.literal("  ")
                    .append(Component.translatable(translationBase + ".line_" + i)
                            .withStyle(ChatFormatting.WHITE)));
        }

        return InteractionResultHolder.success(stack);
    }
}
