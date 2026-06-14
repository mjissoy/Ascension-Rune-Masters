package net.zic.runic_ascension.core.items;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.zic.runic_ascension.content.RunicPathHelper;
import net.zic.runic_ascension.core.inscriptions.ModRunicInscriptions;
import net.zic.runic_ascension.core.inscriptions.RunicInscription;
import net.zic.runic_ascension.util.RunicInscriptionHelper;

import java.util.ArrayList;
import java.util.List;

public class RunicInscriptionSealItem extends Item {

    private static final String KEY_SELECTED = "SelectedInscriptionIndex";

    public RunicInscriptionSealItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack sealStack = player.getItemInHand(hand);

        if (level.isClientSide()) {
            return InteractionResultHolder.success(sealStack);
        }

        if (!(player instanceof ServerPlayer serverPlayer)) {
            return InteractionResultHolder.success(sealStack);
        }

        if (!RunicPathHelper.hasEnteredRunicPath(serverPlayer)) {
            serverPlayer.displayClientMessage(
                    Component.translatable("runic_ascension.inscription.unlock.not_on_path")
                            .withStyle(ChatFormatting.RED),
                    true
            );
            return InteractionResultHolder.success(sealStack);
        }

        if (serverPlayer.isShiftKeyDown()) {
            cycleSelectedInscription(sealStack, serverPlayer);
            return InteractionResultHolder.success(sealStack);
        }

        RunicInscription inscription = getSelectedInscription(sealStack);

        if (inscription == null) {
            serverPlayer.displayClientMessage(
                    Component.translatable("runic_ascension.inscription.seal.no_selection")
                            .withStyle(ChatFormatting.RED),
                    true
            );
            return InteractionResultHolder.success(sealStack);
        }

        int currentTier = RunicInscriptionHelper.getTier(serverPlayer, inscription);
        RunicInscription.Tier nextTier = inscription.getNextTier(currentTier);

        if (nextTier == null) {
            serverPlayer.displayClientMessage(
                    Component.translatable("runic_ascension.inscription.unlock.maxed", inscription.getName())
                            .withStyle(ChatFormatting.RED),
                    true
            );
            return InteractionResultHolder.success(sealStack);
        }

        Item requiredMaterial = requiredMaterialFor(inscription, nextTier.tier());
        ItemStack materialStack = hand == InteractionHand.MAIN_HAND
                ? serverPlayer.getOffhandItem()
                : serverPlayer.getMainHandItem();

        if (materialStack.isEmpty()) {
            inspectSelectedInscription(serverPlayer, sealStack, inscription);
            return InteractionResultHolder.success(sealStack);
        }

        if (materialStack.getItem() != requiredMaterial) {
            serverPlayer.displayClientMessage(
                    Component.translatable(
                            "runic_ascension.inscription.seal.missing_material",
                            requiredMaterial.getDescription()
                    ).withStyle(ChatFormatting.RED),
                    true
            );

            inspectSelectedInscription(serverPlayer, sealStack, inscription);
            return InteractionResultHolder.success(sealStack);
        }

        boolean upgraded = RunicInscriptionHelper.unlockOrUpgrade(serverPlayer, inscription);

        if (upgraded && !serverPlayer.getAbilities().instabuild) {
            materialStack.shrink(1);
        }

        return InteractionResultHolder.success(sealStack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);

        RunicInscription selected = getSelectedInscription(stack);

        tooltip.add(Component.translatable("runic_ascension.inscription.seal.tooltip")
                .withStyle(ChatFormatting.GRAY));

        tooltip.add(Component.translatable(
                "runic_ascension.inscription.seal.tooltip.selected",
                selected == null ? Component.translatable("runic_ascension.runic.codex.none") : selected.getName()
        ).withStyle(ChatFormatting.LIGHT_PURPLE));

        tooltip.add(Component.translatable("runic_ascension.inscription.seal.tooltip.cycle")
                .withStyle(ChatFormatting.DARK_PURPLE));
    }

    private void cycleSelectedInscription(ItemStack stack, ServerPlayer player) {
        List<RunicInscription> inscriptions = inscriptionList();

        if (inscriptions.isEmpty()) {
            player.displayClientMessage(
                    Component.translatable("runic_ascension.inscription.seal.no_selection")
                            .withStyle(ChatFormatting.RED),
                    true
            );

            return;
        }

        int current = getSelectedIndex(stack);
        int next = Math.floorMod(current + 1, inscriptions.size());

        setSelectedIndex(stack, next);

        player.displayClientMessage(
                Component.translatable(
                        "runic_ascension.inscription.seal.selected",
                        inscriptions.get(next).getName()
                ).withStyle(ChatFormatting.LIGHT_PURPLE),
                true
        );
        inspectSelectedInscription(player, stack, inscriptions.get(next));
    }

    private RunicInscription getSelectedInscription(ItemStack stack) {
        List<RunicInscription> inscriptions = inscriptionList();

        if (inscriptions.isEmpty()) {
            return null;
        }

        int index = Math.floorMod(getSelectedIndex(stack), inscriptions.size());
        return inscriptions.get(index);
    }

    private int getSelectedIndex(ItemStack stack) {
        CustomData data = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag tag = data.copyTag();

        if (!tag.contains(KEY_SELECTED)) {
            return 0;
        }

        return tag.getInt(KEY_SELECTED);
    }

    private void setSelectedIndex(ItemStack stack, int index) {
        CustomData data = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag tag = data.copyTag();

        tag.putInt(KEY_SELECTED, index);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    private List<RunicInscription> inscriptionList() {
        return new ArrayList<>(ModRunicInscriptions.all());
    }

    private void inspectSelectedInscription(ServerPlayer player, ItemStack sealStack, RunicInscription inscription) {
        int currentTier = RunicInscriptionHelper.getTier(player, inscription);
        RunicInscription.Tier nextTier = inscription.getNextTier(currentTier);

        player.displayClientMessage(
                Component.literal("◆ ").withStyle(ChatFormatting.DARK_PURPLE)
                        .append(inscription.getName().copy().withStyle(ChatFormatting.LIGHT_PURPLE)),
                false
        );

        if (currentTier <= 0) {
            player.displayClientMessage(
                    Component.translatable("runic_ascension.inscription.seal.status.none")
                            .withStyle(ChatFormatting.GRAY),
                    false
            );
        } else {
            player.displayClientMessage(
                    Component.translatable(
                            "runic_ascension.inscription.seal.status.current",
                            inscription.getTierName(currentTier)
                    ).withStyle(ChatFormatting.AQUA),
                    false
            );

            player.displayClientMessage(
                    inscription.getTierDescription(currentTier).copy()
                            .withStyle(ChatFormatting.GRAY),
                    false
            );
        }

        if (nextTier == null) {
            player.displayClientMessage(
                    Component.translatable("runic_ascension.inscription.seal.status.maxed")
                            .withStyle(ChatFormatting.GOLD),
                    false
            );
            return;
        }

        Item requiredMaterial = requiredMaterialFor(inscription, nextTier.tier());

        player.displayClientMessage(
                Component.translatable(
                        "runic_ascension.inscription.seal.status.next",
                        inscription.getTierName(nextTier.tier())
                ).withStyle(ChatFormatting.YELLOW),
                false
        );

        player.displayClientMessage(
                Component.translatable(
                        "runic_ascension.inscription.seal.status.material",
                        requiredMaterial.getDescription()
                ).withStyle(ChatFormatting.GRAY),
                false
        );

        Component failure = RunicInscriptionHelper.getUnlockFailure(player, inscription);

        if (failure != null) {
            player.displayClientMessage(
                    failure.copy().withStyle(ChatFormatting.RED),
                    false
            );
        }
    }

    private Item requiredMaterialFor(RunicInscription inscription, int tier) {
        String path = inscription.getPath();

        return switch (path) {
            case "flameguard" -> switch (tier) {
                case 1 -> Items.BLAZE_POWDER;
                case 2 -> Items.MAGMA_CREAM;
                default -> Items.BLAZE_ROD;
            };

            case "stonehide" -> switch (tier) {
                case 1 -> Items.COBBLED_DEEPSLATE;
                case 2 -> Items.IRON_INGOT;
                default -> Items.OBSIDIAN;
            };

            case "clearflow" -> switch (tier) {
                case 1 -> Items.KELP;
                case 2 -> Items.PRISMARINE_SHARD;
                default -> Items.PRISMARINE_CRYSTALS;
            };

            case "windstep" -> switch (tier) {
                case 1 -> Items.FEATHER;
                case 2 -> Items.PHANTOM_MEMBRANE;
                default -> Items.ECHO_SHARD;
            };

            case "still_script" -> switch (tier) {
                case 1 -> Items.AMETHYST_SHARD;
                case 2 -> Items.LAPIS_LAZULI;
                default -> Items.ECHO_SHARD;
            };

            case "mirror_mark" -> switch (tier) {
                case 1 -> Items.GLASS_PANE;
                case 2 -> Items.ENDER_PEARL;
                default -> Items.ENDER_EYE;
            };

            default -> Items.AMETHYST_SHARD;
        };
    }
}