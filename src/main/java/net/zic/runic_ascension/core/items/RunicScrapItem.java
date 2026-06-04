package net.zic.runic_ascension.core.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.zic.runic_ascension.content.RunicPathHelper;
import net.zic.runic_ascension.content.RunicPlayerData;
import net.zic.runic_ascension.network.client_bound.OpenRunicCodexScreenPayload;

import java.util.List;

public class RunicScrapItem extends Item {

    private static final String SCRAP_PREFIX = "runic_ascension.runic.scrap.";

    private final String translationBase;
    private final String scrapKey;
    private final int lineCount;

    public RunicScrapItem(Properties properties, String translationBase, int lineCount) {
        super(properties);
        this.translationBase = translationBase;
        this.scrapKey = translationBase.startsWith(SCRAP_PREFIX)
                ? translationBase.substring(SCRAP_PREFIX.length())
                : translationBase;
        this.lineCount = lineCount;
    }

    public String getTranslationBase() {
        return translationBase;
    }

    public String getScrapKey() {
        return scrapKey;
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

        if (!(player instanceof ServerPlayer serverPlayer)) {
            return InteractionResultHolder.success(stack);
        }

        if (!RunicPathHelper.hasEnteredRunicPath(player)) {
            player.sendSystemMessage(Component.translatable("runic_ascension.runic.codex.not_on_path"));
            return InteractionResultHolder.success(stack);
        }

        RunicPlayerData data = RunicPathHelper.getRunicData(player);
        boolean newlyRecorded = data.addDiscoveredScrap(scrapKey);

        if (newlyRecorded) {
            RunicPathHelper.saveRunicData(player, data);
        }

        if (newlyRecorded && !player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        player.displayClientMessage(Component.translatable(
                newlyRecorded
                        ? "runic_ascension.runic.scrap.recorded"
                        : "runic_ascension.runic.scrap.already_recorded",
                Component.translatable(translationBase + ".title")
        ), true);

        OpenRunicCodexScreenPayload.sendTo(serverPlayer, scrapKey);
        return InteractionResultHolder.success(stack);
    }
}
