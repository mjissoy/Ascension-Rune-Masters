package net.zic.runic_ascension.core.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class RunicBrushItem extends Item {

    private final RunicBrushType brushType;
    private final int extraRuneSlots;
    private final ResourceLocation affinityRune;

    public RunicBrushItem(Properties properties, int extraRuneSlots, ResourceLocation affinityRune) {
        this(properties, RunicBrushType.BASIC, extraRuneSlots, affinityRune);
    }

    public RunicBrushItem(Properties properties, RunicBrushType brushType) {
        this(properties, brushType, brushType.extraRuneSlots(), brushType.affinityRune());
    }

    public RunicBrushItem(
            Properties properties,
            RunicBrushType brushType,
            int extraRuneSlots,
            ResourceLocation affinityRune
    ) {
        super(properties);
        this.brushType = brushType == null ? RunicBrushType.BASIC : brushType;
        this.extraRuneSlots = Math.max(0, extraRuneSlots);
        this.affinityRune = affinityRune;
    }

    public RunicBrushType getBrushType() {
        return brushType;
    }

    public int getExtraRuneSlots() {
        return extraRuneSlots;
    }

    public ResourceLocation getAffinityRune() {
        return affinityRune;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);

        tooltip.add(Component.translatable(brushType.translationKey()).withStyle(ChatFormatting.LIGHT_PURPLE));
        tooltip.add(Component.translatable(brushType.tooltipKey()).withStyle(ChatFormatting.DARK_PURPLE));

        if (extraRuneSlots > 0) {
            tooltip.add(Component.translatable("runic_ascension.runic.brush.tooltip.slots", extraRuneSlots)
                    .withStyle(ChatFormatting.GRAY));
        }
    }
}
