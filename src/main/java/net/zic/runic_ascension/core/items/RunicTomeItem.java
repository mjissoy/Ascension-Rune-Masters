package net.zic.runic_ascension.core.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.zic.runic_ascension.content.RunicPathHelper;
import net.zic.runic_ascension.content.runes.IRunicRune;
import net.zic.runic_ascension.content.runes.ModRunicRunes;

import java.util.ArrayList;
import java.util.List;

public class RunicTomeItem extends Item {

    private final List<ResourceLocation> runePool;
    private final int minimumRuneGrants;
    private final int maximumRuneGrants;
    private final String formulaFamilyKey;
    private final boolean completeArchive;

    public RunicTomeItem(Properties properties, List<ResourceLocation> taughtRunes) {
        this(properties, taughtRunes, taughtRunes.size(), taughtRunes.size(), "complete_archive", true);
    }

    public RunicTomeItem(
            Properties properties,
            List<ResourceLocation> runePool,
            int minimumRuneGrants,
            int maximumRuneGrants,
            String formulaFamilyKey
    ) {
        this(properties, runePool, minimumRuneGrants, maximumRuneGrants, formulaFamilyKey, false);
    }

    private RunicTomeItem(
            Properties properties,
            List<ResourceLocation> runePool,
            int minimumRuneGrants,
            int maximumRuneGrants,
            String formulaFamilyKey,
            boolean completeArchive
    ) {
        super(properties);
        this.runePool = List.copyOf(runePool);
        this.minimumRuneGrants = Math.max(1, Math.min(minimumRuneGrants, this.runePool.size()));
        this.maximumRuneGrants = Math.max(this.minimumRuneGrants, Math.min(maximumRuneGrants, this.runePool.size()));
        this.formulaFamilyKey = formulaFamilyKey;
        this.completeArchive = completeArchive;
    }

    public List<ResourceLocation> getRunePool() {
        return runePool;
    }

    public int getMinimumRuneGrants() {
        return minimumRuneGrants;
    }

    public int getMaximumRuneGrants() {
        return maximumRuneGrants;
    }

    public String getFormulaFamilyKey() {
        return formulaFamilyKey;
    }

    public boolean isCompleteArchive() {
        return completeArchive;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);

        if (completeArchive) {
            tooltip.add(Component.translatable("runic_ascension.runic.tome.complete_archive")
                    .withStyle(ChatFormatting.DARK_PURPLE));
        } else {
            tooltip.add(Component.translatable("runic_ascension.runic.tome.fragment_family")
                    .withStyle(ChatFormatting.DARK_PURPLE));
            tooltip.add(Component.translatable("runic_ascension.runic.tome.family." + formulaFamilyKey)
                    .withStyle(ChatFormatting.LIGHT_PURPLE));
        }
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (level.isClientSide) {
            return InteractionResultHolder.success(stack);
        }

        if (!RunicPathHelper.hasEnteredRunicPath(player)) {
            player.sendSystemMessage(Component.translatable("runic_ascension.runic.tome.not_on_path"));
            return InteractionResultHolder.success(stack);
        }

        List<ResourceLocation> revealedRunes = completeArchive
                ? runePool
                : selectRevealedRunes(player.getRandom());

        List<ResourceLocation> learnedRunes = RunicPathHelper.learnRunesAndReturnNew(player, revealedRunes);
        int learned = learnedRunes.size();

        if (learned <= 0) {
            player.displayClientMessage(
                    Component.translatable("runic_ascension.runic.tome.no_new_runes")
                            .withStyle(ChatFormatting.DARK_PURPLE),
                    true
            );
        } else {
            player.displayClientMessage(
                    buildLearnedRuneMessage(player, learnedRunes),
                    true
            );
        }

        if (!player.getAbilities().instabuild && shouldConsumeOnStudy(learned)) {
            stack.shrink(1);
        }

        return InteractionResultHolder.success(stack);
    }

    private boolean shouldConsumeOnStudy(int learned) {
        return learned > 0 || !completeArchive;
    }

    private List<ResourceLocation> selectRevealedRunes(RandomSource random) {
        if (runePool.isEmpty()) {
            return List.of();
        }

        int grantCount = minimumRuneGrants;
        if (maximumRuneGrants > minimumRuneGrants) {
            grantCount += random.nextInt(maximumRuneGrants - minimumRuneGrants + 1);
        }

        List<ResourceLocation> remaining = new ArrayList<>(runePool);
        List<ResourceLocation> selected = new ArrayList<>();

        while (!remaining.isEmpty() && selected.size() < grantCount) {
            selected.add(remaining.remove(random.nextInt(remaining.size())));
        }

        return selected;
    }

    private Component buildLearnedRuneMessage(Player player, List<ResourceLocation> learnedRunes) {
        List<IRunicRune> visibleRunes = learnedRunes.stream()
                .filter(runeId -> RunicPathHelper.canUseRune(player, runeId))
                .map(ModRunicRunes::get)
                .filter(rune -> rune != null)
                .toList();

        int veiledCount = learnedRunes.size() - visibleRunes.size();

        if (visibleRunes.isEmpty()) {
            return Component.translatable(
                    "runic_ascension.runic.tome.learned_all_veiled",
                    learnedRunes.size()
            ).withStyle(ChatFormatting.GRAY);
        }

        Component runeNames = joinRuneNames(visibleRunes);

        if (veiledCount > 0) {
            return Component.translatable(
                    "runic_ascension.runic.tome.learned_named_with_veiled",
                    runeNames,
                    veiledCount
            ).withStyle(ChatFormatting.WHITE);
        }

        return Component.translatable(
                "runic_ascension.runic.tome.learned_named",
                runeNames
        ).withStyle(ChatFormatting.WHITE);
    }

    private Component joinRuneNames(List<IRunicRune> runes) {
        Component result = Component.empty();

        for (int i = 0; i < runes.size(); i++) {
            if (i > 0) {
                result = result.copy().append(Component.literal(", ").withStyle(ChatFormatting.GRAY));
            }

            result = result.copy().append(runes.get(i).getName().copy().withStyle(ChatFormatting.DARK_PURPLE));
        }

        return result;
    }
}
