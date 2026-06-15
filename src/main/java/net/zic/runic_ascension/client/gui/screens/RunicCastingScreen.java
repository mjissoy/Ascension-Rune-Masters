package net.zic.runic_ascension.client.gui.screens;

import net.lucent.easygui.gui.RenderableElement;
import net.lucent.easygui.gui.UIFrame;
import net.lucent.easygui.screen.EasyScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zic.runic_ascension.client.gui.elements.RunicCastingSlotBarElement;
import net.zic.runic_ascension.client.gui.elements.RunicCenteredPanelElement;
import net.zic.runic_ascension.client.gui.elements.RunicDividerElement;
import net.zic.runic_ascension.client.gui.elements.RunicGuiTheme;
import net.zic.runic_ascension.client.gui.elements.RunicInfoPillElement;
import net.zic.runic_ascension.client.gui.elements.RunicLabel;
import net.zic.runic_ascension.client.gui.elements.RunicPanelElement;
import net.zic.runic_ascension.client.gui.elements.RunicProgressBar;
import net.zic.runic_ascension.client.gui.elements.RunicScrollGridElement;
import net.zic.runic_ascension.client.gui.elements.RunicTextButton;
import net.zic.runic_ascension.content.casting.RunicFormulaCaster;
import net.zic.runic_ascension.content.casting.RunicFormulaEvaluation;
import net.zic.runic_ascension.content.runes.IRunicRune;
import net.zic.runic_ascension.content.runes.ModRunicRunes;
import net.zic.runic_ascension.content.runes.RunicRuneType;
import net.zic.runic_ascension.network.server_bound.CastRunicSequencePayload;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class RunicCastingScreen extends EasyScreen {

    private static final int PANEL_WIDTH = 430;
    private static final int PANEL_HEIGHT = 292;
    private static final int SIDE_PADDING = 14;
    private static final int CONTENT_WIDTH = PANEL_WIDTH - SIDE_PADDING * 2;

    private static final int SLOT_WIDTH = 38;
    private static final int SLOT_HEIGHT = 30;
    private static final int SLOT_GAP = 6;

    private static final int RUNE_BUTTON_WIDTH = 88;
    private static final int RUNE_BUTTON_HEIGHT = 20;
    private static final int RUNE_BUTTON_GAP = 8;
    private static final int RUNE_COLUMNS = 4;

    private final List<ResourceLocation> usableRunes;
    private final List<ResourceLocation> selectedRunes = new ArrayList<>();
    private final Map<RunicRuneType, RunicScrollGridElement> runeScrollBoxes = new EnumMap<>(RunicRuneType.class);

    private RunicRuneType activeRuneType = RunicRuneType.SOURCE;
    private RunicCastingSlotBarElement slotBar;
    private RunicInfoPillElement qiCostPill;

    private final int maxRuneSlots;
    private final int durationSeconds;
    private final int runicRealm;
    private int selectedSuppressionRealm;
    private int remainingTicks;
    private boolean closingSafely;

    public RunicCastingScreen(int maxRuneSlots, int durationSeconds, List<ResourceLocation> usableRunes) {
        this(maxRuneSlots, durationSeconds, 0, usableRunes);
    }

    public RunicCastingScreen(int maxRuneSlots, int durationSeconds, int runicRealm, List<ResourceLocation> usableRunes) {
        super(Component.translatable("runic_ascension.runic.casting.title"));

        this.maxRuneSlots = Math.max(0, maxRuneSlots);
        this.durationSeconds = Math.max(0, durationSeconds);
        this.runicRealm = Math.max(0, runicRealm);
        this.selectedSuppressionRealm = this.runicRealm <= 0 ? 0 : this.runicRealm;
        this.usableRunes = usableRunes == null ? List.of() : List.copyOf(usableRunes);
        this.remainingTicks = this.durationSeconds * 20;

        build(getUIFrame());
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();

        if (usableRunes.isEmpty() || closingSafely || durationSeconds <= 0) {
            return;
        }

        remainingTicks--;

        if (remainingTicks <= 0) {
            closingSafely = true;
            PacketDistributor.sendToServer(new CastRunicSequencePayload(List.of()));
            Minecraft.getInstance().setScreen(null);
        }
    }

    @Override
    public void onClose() {
        if (!usableRunes.isEmpty() && !closingSafely) {
            PacketDistributor.sendToServer(new CastRunicSequencePayload(List.of()));
        }

        super.onClose();
    }

    private double getTimerProgress() {
        if (durationSeconds <= 0) {
            return 0.0D;
        }

        return Math.max(0.0D, Math.min(1.0D, remainingTicks / (durationSeconds * 20.0D)));
    }

    private Component getTimerText() {
        float secondsLeft = Math.max(0.0F, remainingTicks / 20.0F);
        return Component.translatable("runic_ascension.runic.casting.timer", String.format("%.1f", secondsLeft));
    }

    private void build(UIFrame frame) {
        frame.setPauseGame(false);

        RunicCenteredPanelElement panel = new RunicCenteredPanelElement(frame, PANEL_WIDTH, PANEL_HEIGHT, true, false);
        frame.setRoot(panel);

        panel.addChild(new RunicLabel(
                frame,
                Component.translatable("runic_ascension.runic.casting.title"),
                0,
                4,
                PANEL_WIDTH,
                14,
                RunicGuiTheme.TEXT_TITLE
        ).centered().scaled(1.0F));

        addStatusRow(panel, frame);
        addSlotPanel(panel, frame);

        if (usableRunes.isEmpty()) {
            addEmptyState(panel, frame);
        } else {
            addRuneBrowser(panel, frame);
        }

        addControls(panel, frame);
        refreshSelectedDisplay();
    }

    private void addStatusRow(RenderableElement panel, UIFrame frame) {
        qiCostPill = new RunicInfoPillElement(
                frame,
                SIDE_PADDING,
                26,
                116,
                18,
                Component.translatable("runic_ascension.runic.casting.qi.empty")
        );
        panel.addChild(qiCostPill);

        TimerPill timerPill = new TimerPill(frame, PANEL_WIDTH - SIDE_PADDING - 142, 26, 142, 18);
        panel.addChild(timerPill);

        panel.addChild(new RunicProgressBar(frame, SIDE_PADDING, 49, CONTENT_WIDTH, 6, this::getTimerProgress));
    }

    private void addSlotPanel(RenderableElement panel, UIFrame frame) {
        slotBar = new RunicCastingSlotBarElement(
                frame,
                SIDE_PADDING,
                62,
                CONTENT_WIDTH,
                56,
                maxRuneSlots,
                SLOT_WIDTH,
                SLOT_HEIGHT,
                SLOT_GAP
        );
        panel.addChild(slotBar);
    }

    private void addRuneBrowser(RenderableElement panel, UIFrame frame) {
        RunicPanelElement browser = new RunicPanelElement(frame, SIDE_PADDING, 128, CONTENT_WIDTH, 118);
        panel.addChild(browser);

        int tabCount = Math.max(1, RunicRuneType.values().length);
        int tabGap = 5;
        int tabWidth = Math.max(58, (CONTENT_WIDTH - 14 - (tabCount - 1) * tabGap) / tabCount);
        int tabX = 7;

        for (RunicRuneType type : RunicRuneType.values()) {
            RunicTextButton tab = new RunicTextButton(frame, tabX, 7, tabWidth, 18, Component.literal(RunicGuiTheme.formatEnumName(type.name()))) {
                @Override
                protected boolean isSelected() {
                    return activeRuneType == type;
                }

                @Override
                public void onClick() {
                    setActiveRuneType(type);
                }
            };

            browser.addChild(tab);
            tabX += tabWidth + tabGap;

            RunicScrollGridElement scrollBox = new RunicScrollGridElement(
                    frame,
                    10,
                    34,
                    CONTENT_WIDTH - 20,
                    74,
                    RUNE_BUTTON_WIDTH,
                    RUNE_BUTTON_HEIGHT,
                    RUNE_BUTTON_GAP,
                    RUNE_COLUMNS
            );
            scrollBox.setVisible(type == activeRuneType);
            scrollBox.setActive(type == activeRuneType);
            runeScrollBoxes.put(type, scrollBox);
            browser.addChild(scrollBox);
        }

        for (ResourceLocation runeId : usableRunes) {
            IRunicRune rune = ModRunicRunes.get(runeId);

            if (rune == null) {
                continue;
            }

            RunicScrollGridElement scrollBox = runeScrollBoxes.get(rune.getType());

            if (scrollBox == null) {
                continue;
            }

            RuneButton runeButton = new RuneButton(frame, runeId, 0, 0, RUNE_BUTTON_WIDTH, RUNE_BUTTON_HEIGHT);
            scrollBox.addChild(runeButton);
        }

        setActiveRuneType(activeRuneType);
    }

    private void addEmptyState(RenderableElement panel, UIFrame frame) {
        RunicPanelElement emptyBox = new RunicPanelElement(frame, 34, 132, PANEL_WIDTH - 68, 82);
        panel.addChild(emptyBox);

        emptyBox.addChild(new RunicLabel(frame, Component.translatable("runic_ascension.runic.casting.empty.title"), 0, 14, emptyBox.getWidth(), 14, RunicGuiTheme.TEXT_TITLE).centered().scaled(1.0F));
        emptyBox.addChild(new RunicLabel(frame, Component.translatable("runic_ascension.runic.casting.empty.line_1"), 18, 42, emptyBox.getWidth() - 36, 12, RunicGuiTheme.TEXT_MUTED).centered().scaled(0.82F));
        emptyBox.addChild(new RunicLabel(frame, Component.translatable("runic_ascension.runic.casting.empty.line_2"), 18, 60, emptyBox.getWidth() - 36, 12, RunicGuiTheme.TEXT_DIM).centered().scaled(0.78F));
    }

    private void addControls(RenderableElement panel, UIFrame frame) {
        panel.addChild(new RunicDividerElement(frame, SIDE_PADDING, 256, CONTENT_WIDTH, 1));

        RunicTextButton backspace = new RunicTextButton(frame, SIDE_PADDING, 264, 92, 20, Component.translatable("runic_ascension.runic.casting.backspace")) {
            @Override
            public void onClick() {
                if (!selectedRunes.isEmpty()) {
                    selectedRunes.remove(selectedRunes.size() - 1);
                    refreshSelectedDisplay();
                }
            }
        };
        panel.addChild(backspace);

        RunicTextButton clear = new RunicTextButton(frame, SIDE_PADDING + 102, 264, 92, 20, Component.translatable("runic_ascension.runic.casting.clear")) {
            @Override
            public void onClick() {
                selectedRunes.clear();
                refreshSelectedDisplay();
            }
        };
        panel.addChild(clear);

        RunicTextButton cast = new RunicTextButton(
                frame,
                PANEL_WIDTH - SIDE_PADDING - 104,
                264,
                104,
                20,
                Component.translatable(usableRunes.isEmpty()
                        ? "runic_ascension.runic.casting.close"
                        : "runic_ascension.runic.casting.cast")
        ) {
            @Override
            public void onClick() {
                if (usableRunes.isEmpty()) {
                    closingSafely = true;
                    Minecraft.getInstance().setScreen(null);
                    return;
                }

                if (selectedRunes.isEmpty()) {
                    return;
                }

                closingSafely = true;
                PacketDistributor.sendToServer(new CastRunicSequencePayload(List.copyOf(selectedRunes), selectedSuppressionRealm));
                Minecraft.getInstance().setScreen(null);
            }
        };
        panel.addChild(cast);
    }

    private void addRune(ResourceLocation runeId) {
        if (selectedRunes.size() >= maxRuneSlots) {
            return;
        }

        selectedRunes.add(runeId);
        refreshSelectedDisplay();
    }

    private void refreshSelectedDisplay() {
        refreshSelectedSlots();
        refreshQiCostDisplay();
    }

    private void refreshSelectedSlots() {
        if (slotBar == null) {
            return;
        }

        slotBar.setStatusText(getSelectedStatusText());
        slotBar.setSlotNames(selectedRunes.stream().map(RunicCastingScreen::getRuneName).toList());
    }

    private Component getSelectedStatusText() {
        if (usableRunes.isEmpty()) {
            return Component.translatable("runic_ascension.runic.casting.selected.no_usable");
        }

        if (selectedRunes.isEmpty()) {
            return Component.translatable("runic_ascension.runic.casting.selected.empty");
        }

        return Component.translatable("runic_ascension.runic.casting.selected.count", selectedRunes.size(), maxRuneSlots);
    }

    private void refreshQiCostDisplay() {
        if (qiCostPill == null) {
            return;
        }

        if (usableRunes.isEmpty() || selectedRunes.isEmpty()) {
            qiCostPill.setText(Component.translatable("runic_ascension.runic.casting.qi.empty"));
            return;
        }

        RunicFormulaEvaluation evaluation = estimateCurrentFormula();
        if (evaluation == null) {
            qiCostPill.setText(Component.translatable("runic_ascension.runic.casting.qi.unknown"));
            return;
        }

        qiCostPill.setText(Component.translatable("runic_ascension.runic.casting.qi.cost", formatNumber(evaluation.qiCost())));
    }

    private RunicFormulaEvaluation estimateCurrentFormula() {
        if (Minecraft.getInstance().player == null) {
            return null;
        }

        return RunicFormulaCaster.estimate(Minecraft.getInstance().player, selectedRunes, selectedSuppressionRealm);
    }

    private void setActiveRuneType(RunicRuneType type) {
        activeRuneType = type;

        for (Map.Entry<RunicRuneType, RunicScrollGridElement> entry : runeScrollBoxes.entrySet()) {
            boolean active = entry.getKey() == type;
            entry.getValue().setVisible(active);
            entry.getValue().setActive(active);
        }
    }

    private static String formatNumber(double value) {
        return String.format("%.1f", value);
    }

    private static Component getRuneName(ResourceLocation runeId) {
        IRunicRune rune = ModRunicRunes.get(runeId);
        return rune == null ? Component.literal(runeId.getPath()) : rune.getName();
    }

    private class RuneButton extends RunicTextButton {
        private final ResourceLocation runeId;

        private RuneButton(UIFrame frame, ResourceLocation runeId, int x, int y, int width, int height) {
            super(frame, x, y, width, height, getRuneName(runeId));
            this.runeId = runeId;
        }

        @Override
        public void onClick() {
            addRune(runeId);
        }
    }

    private class TimerPill extends RunicInfoPillElement {
        private TimerPill(UIFrame frame, int x, int y, int width, int height) {
            super(frame, x, y, width, height, Component.empty());
        }

        @Override
        public void renderTick(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            setText(getTimerText());
            super.renderTick(guiGraphics, mouseX, mouseY, partialTick);
        }
    }
}
