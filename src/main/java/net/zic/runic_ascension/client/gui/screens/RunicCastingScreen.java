package net.zic.runic_ascension.client.gui.screens;

import net.lucent.easygui.gui.RenderableElement;
import net.lucent.easygui.gui.UIFrame;
import net.lucent.easygui.gui.elements.built_in.EasyLabel;
import net.lucent.easygui.gui.layout.positioning.rules.PositioningRules;
import net.lucent.easygui.screen.EasyScreen;
import net.thejadeproject.ascension.refactor_packages.gui.elements.general.ScrollBox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zic.runic_ascension.client.gui.elements.RunicGuiTheme;
import net.zic.runic_ascension.client.gui.elements.RunicLabel;
import net.zic.runic_ascension.client.gui.elements.RunicPanelElement;
import net.zic.runic_ascension.client.gui.elements.RunicProgressBar;
import net.zic.runic_ascension.client.gui.elements.RunicSlotElement;
import net.zic.runic_ascension.client.gui.elements.RunicTextButton;
import net.zic.runic_ascension.client.gui.elements.RunicTextBlockElement;
import net.zic.runic_ascension.content.casting.RunicEffectProfile;
import net.zic.runic_ascension.content.casting.RunicFormula;
import net.zic.runic_ascension.content.casting.RunicFormulaCaster;
import net.zic.runic_ascension.content.casting.RunicFormulaEvaluation;
import net.zic.runic_ascension.content.casting.RunicFormulaInterpreter;
import net.zic.runic_ascension.content.casting.RunicFormulaParser;
import net.zic.runic_ascension.content.runes.IRunicRune;
import net.zic.runic_ascension.content.runes.ModRunicRunes;
import net.zic.runic_ascension.content.runes.RunicRuneType;
import net.zic.runic_ascension.network.server_bound.CastRunicSequencePayload;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class RunicCastingScreen extends EasyScreen {

    private static final int PANEL_WIDTH = 496;
    private static final int PANEL_HEIGHT = 344;
    private static final int SLOT_WIDTH = 34;
    private static final int SLOT_HEIGHT = 30;
    private static final int SLOT_GAP = 5;

    private final List<ResourceLocation> usableRunes;
    private final List<ResourceLocation> selectedRunes = new ArrayList<>();
    private final List<RuneButton> runeButtons = new ArrayList<>();
    private final List<RunicSlotElement> slotElements = new ArrayList<>();
    private final Map<RunicRuneType, RunicRuneScrollBox> runeScrollBoxes = new EnumMap<>(RunicRuneType.class);
    private final Map<RunicRuneType, RunicTextButton> tabButtons = new EnumMap<>(RunicRuneType.class);

    private RunicRuneType activeRuneType = RunicRuneType.SOURCE;
    private RunicTextBlockElement hoverLabel;
    private EasyLabel selectedLabel;
    private RunicTextBlockElement previewTitleLabel;
    private RunicTextBlockElement previewStatsLabel;
    private RunicTextBlockElement previewRiskLabel;
    private EasyLabel suppressionLabel;
    private EasyLabel realmInfoLabel;

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

        RunicPanelElement panel = new RunicPanelElement(frame, 0, 0, PANEL_WIDTH, PANEL_HEIGHT, true, false) {
            @Override
            public void renderTick(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                refreshHoverLabel(findHoveredRune(mouseX, mouseY));
                super.renderTick(guiGraphics, mouseX, mouseY, partialTick);
            }
        };
        panel.getPositioning().setPositioningRule(PositioningRules.CENTER);
        panel.getPositioning().setX(-PANEL_WIDTH / 2);
        panel.getPositioning().setY(-PANEL_HEIGHT / 2);
        frame.setRoot(panel);

        RunicLabel title = new RunicLabel(frame, Component.translatable("runic_ascension.runic.casting.title"), 0, 4, PANEL_WIDTH, 14, RunicGuiTheme.TEXT_TITLE).centered().scaled(1.0F);
        panel.addChild(title);

        panel.addChild(new RunicLabel(frame, Component.translatable("runic_ascension.runic.casting.info", maxRuneSlots, durationSeconds), 16, 25, 230, 10, RunicGuiTheme.TEXT_MUTED).scaled(0.8F));

        TimerLabel timerLabel = new TimerLabel(frame, 322, 25, 158, 10);
        panel.addChild(timerLabel);
        panel.addChild(new RunicProgressBar(frame, 16, 40, 464, 6, this::getTimerProgress));

        addSlotPanel(panel, frame);

        if (usableRunes.isEmpty()) {
            addEmptyState(panel, frame);
        } else {
            addRuneBrowser(panel, frame);
            addSequenceInfoPanel(panel, frame);
        }

        addControls(panel, frame);
        refreshSelectedDisplay();
    }

    private void addSlotPanel(RenderableElement panel, UIFrame frame) {
        RunicPanelElement slotPanel = new RunicPanelElement(frame, 16, 52, 464, 46);
        panel.addChild(slotPanel);

        selectedLabel = new RunicLabel(frame, Component.empty(), 10, 6, 150, 12, RunicGuiTheme.TEXT_MUTED).scaled(0.78F);
        slotPanel.addChild(selectedLabel);

        int totalWidth = maxRuneSlots * SLOT_WIDTH + Math.max(0, maxRuneSlots - 1) * SLOT_GAP;
        int startX = Math.max(170, 448 - totalWidth);

        for (int i = 0; i < maxRuneSlots; i++) {
            RunicSlotElement slot = new RunicSlotElement(
                    frame,
                    startX + i * (SLOT_WIDTH + SLOT_GAP),
                    8,
                    SLOT_WIDTH,
                    SLOT_HEIGHT,
                    i
            );

            slotElements.add(slot);
            slotPanel.addChild(slot);
        }
    }

    private void addRuneBrowser(RenderableElement panel, UIFrame frame) {
        RunicPanelElement browser = new RunicPanelElement(frame, 16, 108, 282, 178);
        panel.addChild(browser);

        int tabX = 7;
        int tabWidth = Math.max(62, (268 / Math.max(1, RunicRuneType.values().length)) - 5);

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

            tabButtons.put(type, tab);
            browser.addChild(tab);
            tabX += tabWidth + 5;

            RunicRuneScrollBox scrollBox = new RunicRuneScrollBox(frame, 7, 32, 268, 136);
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

            RunicRuneScrollBox scrollBox = runeScrollBoxes.get(rune.getType());

            if (scrollBox == null) {
                continue;
            }

            RuneButton runeButton = new RuneButton(frame, runeId, 0, 0, RunicRuneScrollBox.BUTTON_WIDTH, RunicRuneScrollBox.BUTTON_HEIGHT);
            runeButtons.add(runeButton);
            scrollBox.addChild(runeButton);
        }

        setActiveRuneType(activeRuneType);
    }

    private void addSequenceInfoPanel(RenderableElement panel, UIFrame frame) {
        RunicPanelElement infoPanel = new RunicPanelElement(frame, 310, 108, 170, 178);
        panel.addChild(infoPanel);

        infoPanel.addChild(new RunicLabel(frame, Component.translatable("runic_ascension.runic.casting.sequence_panel.title"), 0, 6, 170, 12, RunicGuiTheme.TEXT_TITLE).centered().scaled(0.82F));

        realmInfoLabel = new RunicLabel(frame, Component.translatable("runic_ascension.runic.casting.realm_info", runicRealm, getPreviewInsightTier()), 8, 22, 154, 10, RunicGuiTheme.TEXT_DIM).centered().scaled(0.7F);
        infoPanel.addChild(realmInfoLabel);

        addSuppressionControls(infoPanel, frame);
        addFormulaPreview(infoPanel, frame);

        hoverLabel = new RunicTextBlockElement(frame, Component.translatable("runic_ascension.runic.casting.hover.empty"), 10, 160, 150, 12, RunicGuiTheme.TEXT_DIM, 0.66F, true);
        infoPanel.addChild(hoverLabel);
    }

    private void addSuppressionControls(RenderableElement parent, UIFrame frame) {
        RunicTextButton lower = new RunicTextButton(frame, 10, 38, 24, 18, Component.literal("-")) {
            @Override
            public void onClick() {
                shiftSuppressionRealm(-1);
            }
        };
        parent.addChild(lower);

        suppressionLabel = new RunicLabel(frame, Component.empty(), 38, 38, 94, 18, RunicGuiTheme.TEXT_MUTED).centered().scaled(0.7F);
        parent.addChild(suppressionLabel);
        refreshSuppressionLabel();

        RunicTextButton higher = new RunicTextButton(frame, 136, 38, 24, 18, Component.literal("+")) {
            @Override
            public void onClick() {
                shiftSuppressionRealm(1);
            }
        };
        parent.addChild(higher);
    }

    private void addFormulaPreview(RenderableElement parent, UIFrame frame) {
        RunicPanelElement previewBox = new RunicPanelElement(frame, 10, 64, 150, 90);
        parent.addChild(previewBox);

        previewTitleLabel = new RunicTextBlockElement(frame, Component.translatable("runic_ascension.runic.casting.preview.empty"), 7, 7, 136, 22, RunicGuiTheme.TEXT_TITLE, 0.78F, true);
        previewBox.addChild(previewTitleLabel);

        previewStatsLabel = new RunicTextBlockElement(frame, Component.empty(), 8, 35, 134, 22, RunicGuiTheme.TEXT_MUTED, 0.72F, false);
        previewBox.addChild(previewStatsLabel);

        previewRiskLabel = new RunicTextBlockElement(frame, Component.empty(), 8, 62, 134, 22, RunicGuiTheme.TEXT_DIM, 0.7F, false);
        previewBox.addChild(previewRiskLabel);

        refreshFormulaPreview();
    }

    private void addEmptyState(RenderableElement panel, UIFrame frame) {
        RunicPanelElement emptyBox = new RunicPanelElement(frame, 56, 112, 384, 118);
        panel.addChild(emptyBox);

        emptyBox.addChild(new RunicLabel(frame, Component.translatable("runic_ascension.runic.casting.empty.title"), 0, 14, 384, 14, RunicGuiTheme.TEXT_TITLE).centered().scaled(1.0F));
        emptyBox.addChild(new RunicLabel(frame, Component.translatable("runic_ascension.runic.casting.empty.line_1"), 20, 44, 344, 12, RunicGuiTheme.TEXT_MUTED).centered().scaled(0.8F));
        emptyBox.addChild(new RunicLabel(frame, Component.translatable("runic_ascension.runic.casting.empty.line_2"), 20, 64, 344, 12, RunicGuiTheme.TEXT_MUTED).centered().scaled(0.8F));
        emptyBox.addChild(new RunicLabel(frame, Component.translatable("runic_ascension.runic.casting.empty.line_3"), 20, 84, 344, 12, RunicGuiTheme.TEXT_DIM).centered().scaled(0.76F));
    }

    private void addControls(RenderableElement panel, UIFrame frame) {
        RunicTextButton backspace = new RunicTextButton(frame, 16, 308, 112, 22, Component.translatable("runic_ascension.runic.casting.backspace")) {
            @Override
            public void onClick() {
                if (!selectedRunes.isEmpty()) {
                    selectedRunes.remove(selectedRunes.size() - 1);
                    refreshSelectedDisplay();
                }
            }
        };
        panel.addChild(backspace);

        RunicTextButton clear = new RunicTextButton(frame, 144, 308, 112, 22, Component.translatable("runic_ascension.runic.casting.clear")) {
            @Override
            public void onClick() {
                selectedRunes.clear();
                refreshSelectedDisplay();
            }
        };
        panel.addChild(clear);

        RunicTextButton cast = new RunicTextButton(
                frame,
                368,
                308,
                112,
                22,
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

    private ResourceLocation findHoveredRune(int mouseX, int mouseY) {
        for (RuneButton button : runeButtons) {
            IRunicRune rune = ModRunicRunes.get(button.runeId);

            if (rune != null && rune.getType() == activeRuneType && button.isPointBounded(mouseX, mouseY)) {
                return button.runeId;
            }
        }

        return null;
    }

    private void addRune(ResourceLocation runeId) {
        if (selectedRunes.size() >= maxRuneSlots) {
            return;
        }

        selectedRunes.add(runeId);
        refreshSelectedDisplay();
    }

    private void refreshSelectedDisplay() {
        refreshSelectedLabel();
        refreshSelectedSlots();
        refreshFormulaPreview();
    }

    private void refreshSelectedLabel() {
        if (selectedLabel == null) {
            return;
        }

        if (selectedRunes.isEmpty()) {
            selectedLabel.setText(Component.translatable(
                    usableRunes.isEmpty()
                            ? "runic_ascension.runic.casting.selected.no_usable"
                            : "runic_ascension.runic.casting.selected.empty"
            ));
            return;
        }

        selectedLabel.setText(Component.translatable("runic_ascension.runic.casting.selected.count", selectedRunes.size(), maxRuneSlots));
    }

    private void refreshSelectedSlots() {
        for (int i = 0; i < slotElements.size(); i++) {
            RunicSlotElement slot = slotElements.get(i);
            if (i < selectedRunes.size()) {
                slot.setRuneName(getRuneName(selectedRunes.get(i)));
            } else {
                slot.setRuneName(null);
            }
        }
    }

    private void refreshFormulaPreview() {
        if (previewTitleLabel == null || previewStatsLabel == null || previewRiskLabel == null) {
            return;
        }

        if (selectedRunes.isEmpty()) {
            previewTitleLabel.setText(Component.translatable("runic_ascension.runic.casting.preview.empty"));
            previewStatsLabel.setText(Component.translatable("runic_ascension.runic.casting.preview.hint"));
            previewRiskLabel.setText(Component.empty());
            return;
        }

        RunicFormula formula = RunicFormulaParser.parse(selectedRunes);
        RunicEffectProfile profile = RunicFormulaInterpreter.interpret(formula);
        RunicFormulaEvaluation evaluation = estimateCurrentFormula();
        int insightTier = getPreviewInsightTier();

        previewTitleLabel.setText(getPreviewTitle(profile, insightTier));

        if (evaluation == null || evaluation.stats() == null) {
            previewStatsLabel.setText(Component.translatable(
                    "runic_ascension.runic.casting.preview.profile",
                    profile.flagsForDisplay()
            ));
            previewRiskLabel.setText(Component.translatable("runic_ascension.runic.casting.preview.risk.unknown"));
            return;
        }

        switch (insightTier) {
            case 1 -> {
                previewStatsLabel.setText(Component.translatable(
                        "runic_ascension.runic.casting.preview.insight.low.stats",
                        RunicGuiTheme.formatEnumName(profile.archetype().name())
                ));
                previewRiskLabel.setText(getPreviewRiskWithOptionalBrush(
                        "runic_ascension.runic.casting.preview.insight.low.risk",
                        "runic_ascension.runic.casting.preview.insight.low.risk_brush",
                        evaluation,
                        null,
                        null
                ));
            }
            case 2 -> {
                previewStatsLabel.setText(Component.translatable(
                        "runic_ascension.runic.casting.preview.insight.medium.stats",
                        formatRuneWord(profile.sourcePath()),
                        formatRuneWord(profile.intentPath()),
                        formatRuneWord(profile.formPath())
                ));
                previewRiskLabel.setText(getPreviewRiskWithOptionalBrush(
                        "runic_ascension.runic.casting.preview.insight.medium.risk",
                        "runic_ascension.runic.casting.preview.insight.medium.risk_brush",
                        evaluation,
                        previewStateName(evaluation),
                        null
                ));
            }
            case 3 -> {
                previewStatsLabel.setText(Component.translatable(
                        "runic_ascension.runic.casting.preview.insight.high.stats",
                        describeQiCost(evaluation.qiCost()),
                        describeStability(evaluation.stability()),
                        describeRange(evaluation.stats().rangeMultiplier())
                ));
                previewRiskLabel.setText(getPreviewRiskWithOptionalBrush(
                        "runic_ascension.runic.casting.preview.risk",
                        "runic_ascension.runic.casting.preview.risk_brush",
                        evaluation,
                        previewStateName(evaluation),
                        profile.flagsForDisplay()
                ));
            }
            default -> {
                previewStatsLabel.setText(Component.translatable(
                        "runic_ascension.runic.casting.preview.stats",
                        formatNumber(evaluation.qiCost()),
                        formatPercent(evaluation.stability()),
                        formatMultiplier(evaluation.stats().rangeMultiplier())
                ));
                previewRiskLabel.setText(getPreviewRiskWithOptionalBrush(
                        "runic_ascension.runic.casting.preview.risk",
                        "runic_ascension.runic.casting.preview.risk_brush",
                        evaluation,
                        previewStateName(evaluation),
                        profile.flagsForDisplay()
                ));
            }
        }
    }

    private RunicFormulaEvaluation estimateCurrentFormula() {
        if (Minecraft.getInstance().player == null) {
            return null;
        }

        return RunicFormulaCaster.estimate(Minecraft.getInstance().player, selectedRunes, selectedSuppressionRealm);
    }

    /**
     * Higher Runic realms can read more of the formula before it is cast.
     * 1: only broad omens, 2: grammar pieces, 3: qualitative estimates, 4: exact estimates.
     */
    private int getPreviewInsightTier() {
        if (runicRealm >= 7) {
            return 4;
        }

        if (runicRealm >= 5) {
            return 3;
        }

        if (runicRealm >= 3) {
            return 2;
        }

        return 1;
    }

    private Component getPreviewTitle(RunicEffectProfile profile, int insightTier) {
        if (insightTier <= 1) {
            return Component.translatable(
                    "runic_ascension.runic.casting.preview.insight.low.title",
                    RunicGuiTheme.formatEnumName(profile.archetype().name())
            );
        }

        return Component.translatable(
                "runic_ascension.runic.casting.preview.title",
                profile.displayName(),
                RunicGuiTheme.formatEnumName(profile.archetype().name())
        );
    }

    private Component previewStateName(RunicFormulaEvaluation evaluation) {
        return switch (evaluation.state()) {
            case VALID -> Component.translatable("runic_ascension.runic.casting.preview.state.valid");
            case UNSTABLE -> Component.translatable("runic_ascension.runic.casting.preview.state.unstable");
            case OVERREACHED -> Component.translatable("runic_ascension.runic.casting.preview.state.overreached");
            case INVALID -> Component.translatable(
                    "runic_ascension.runic.casting.preview.state.invalid",
                    Component.translatable("runic_ascension.runic.cast." + evaluation.failureReason())
            );
        };
    }

    private Component getPreviewRiskWithOptionalBrush(
            String normalKey,
            String brushKey,
            RunicFormulaEvaluation evaluation,
            Object firstArgument,
            Object secondArgument
    ) {
        Component brushName = getActiveBrushName(evaluation);

        if (brushName == null) {
            if (firstArgument == null && secondArgument == null) {
                return Component.translatable(normalKey);
            }

            if (secondArgument == null) {
                return Component.translatable(normalKey, firstArgument);
            }

            return Component.translatable(normalKey, firstArgument, secondArgument);
        }

        if (firstArgument == null && secondArgument == null) {
            return Component.translatable(brushKey, brushName);
        }

        if (secondArgument == null) {
            return Component.translatable(brushKey, firstArgument, brushName);
        }

        return Component.translatable(brushKey, firstArgument, brushName, secondArgument);
    }

    private Component getActiveBrushName(RunicFormulaEvaluation evaluation) {
        if (evaluation == null || evaluation.context() == null || !evaluation.context().hasBrush()) {
            return null;
        }

        return Component.translatable(evaluation.context().brushType().translationKey());
    }

    private static String describeQiCost(double qiCost) {
        if (qiCost <= 20.0D) {
            return "low";
        }

        if (qiCost <= 45.0D) {
            return "moderate";
        }

        if (qiCost <= 80.0D) {
            return "high";
        }

        return "severe";
    }

    private static String describeStability(float stability) {
        if (stability >= 0.90F) {
            return "steady";
        }

        if (stability >= 0.75F) {
            return "wavering";
        }

        if (stability >= 0.55F) {
            return "unstable";
        }

        return "fracturing";
    }

    private static String describeRange(float rangeMultiplier) {
        if (rangeMultiplier < 0.85F) {
            return "short";
        }

        if (rangeMultiplier <= 1.25F) {
            return "normal";
        }

        if (rangeMultiplier <= 1.75F) {
            return "long";
        }

        return "far-reaching";
    }

    private static String formatNumber(double value) {
        return String.format("%.1f", value);
    }

    private static String formatPercent(float value) {
        return Math.round(value * 100.0F) + "%";
    }

    private static String formatMultiplier(float value) {
        return String.format("x%.2f", value);
    }

    private static String formatRuneWord(String path) {
        if (path == null || path.isBlank() || path.equals("unknown")) {
            return "?";
        }

        return RunicGuiTheme.formatEnumName(path);
    }

    private void shiftSuppressionRealm(int delta) {
        if (runicRealm <= 1) {
            return;
        }

        selectedSuppressionRealm = Math.max(1, Math.min(runicRealm, selectedSuppressionRealm + delta));
        refreshSuppressionLabel();
        refreshFormulaPreview();
    }

    private void refreshSuppressionLabel() {
        if (suppressionLabel == null) {
            return;
        }

        if (runicRealm <= 0) {
            suppressionLabel.setText(Component.translatable("runic_ascension.runic.casting.suppression.none"));
            return;
        }

        suppressionLabel.setText(Component.translatable(
                "runic_ascension.runic.casting.suppression.short",
                selectedSuppressionRealm,
                runicRealm
        ));
    }

    private void refreshHoverLabel(ResourceLocation hoveredRune) {
        if (hoverLabel == null) {
            return;
        }

        if (hoveredRune == null) {
            hoverLabel.setText(Component.translatable("runic_ascension.runic.casting.hover.empty"));
            return;
        }

        IRunicRune rune = ModRunicRunes.get(hoveredRune);

        if (rune == null) {
            hoverLabel.setText(Component.literal(hoveredRune.toString()));
            return;
        }

        hoverLabel.setText(Component.translatable(
                "runic_ascension.runic.casting.hover",
                rune.getName(),
                RunicGuiTheme.formatEnumName(rune.getType().name()),
                RunicGuiTheme.formatEnumName(rune.getDepth().name())
        ));
    }

    private void setActiveRuneType(RunicRuneType type) {
        activeRuneType = type;

        for (Map.Entry<RunicRuneType, RunicRuneScrollBox> entry : runeScrollBoxes.entrySet()) {
            boolean active = entry.getKey() == type;
            entry.getValue().setVisible(active);
            entry.getValue().setActive(active);
        }
    }

    private static Component getRuneName(ResourceLocation runeId) {
        IRunicRune rune = ModRunicRunes.get(runeId);
        return rune == null ? Component.literal(runeId.getPath()) : rune.getName();
    }

    private static class RunicRuneScrollBox extends ScrollBox {
        private static final int BUTTON_WIDTH = 82;
        private static final int BUTTON_HEIGHT = 18;
        private static final int GAP = 7;
        private static final int COLUMNS = 3;
        private static final int ROW_HEIGHT = BUTTON_HEIGHT + GAP;

        private RunicRuneScrollBox(UIFrame frame, int x, int y, int width, int height) {
            super(frame, ROW_HEIGHT);
            setWidth(width);
            setHeight(height);
            getPositioning().setX(x);
            getPositioning().setY(y);
            useCustomChildAdditionLogic = true;
        }

        @Override
        public void addChild(RenderableElement element) {
            super.addChild(element);
            updateVisibility(element);
        }

        @Override
        public void updatePos(RenderableElement element) {
            int index = getChildren().size();
            int col = index % COLUMNS;
            int row = index / COLUMNS;

            element.getPositioning().setFromRawX(col * (BUTTON_WIDTH + GAP));
            element.getPositioning().setFromRawY(row * ROW_HEIGHT);
        }

        @Override
        public int getMaxYScroll() {
            int rows = Math.ceilDiv(getChildren().size(), COLUMNS);
            return Math.max(0, rows * ROW_HEIGHT - getHeight());
        }

        @Override
        public void updateVisibility(RenderableElement element) {
            boolean visible = element.getPositioning().getY() + element.getHeight() > 0
                    && element.getPositioning().getY() < getHeight();
            element.setActive(visible);
            element.setVisible(visible);
        }

        @Override
        public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            guiGraphics.fill(0, 0, getWidth(), getHeight(), RunicGuiTheme.PANEL_SOFT);
            guiGraphics.renderOutline(0, 0, getWidth(), getHeight(), RunicGuiTheme.BORDER_DARK);
        }
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

    private class TimerLabel extends EasyLabel {
        private TimerLabel(UIFrame frame, int x, int y, int width, int height) {
            super(frame);
            setText(Component.empty());
            setTextColor(RunicGuiTheme.TEXT_MUTED);
            setWidth(width);
            setHeight(height);
            getPositioning().setX(x);
            getPositioning().setY(y);
            setScaleToFit(true);
            setTextScale(0.78F);
            setTextPositioningX(EasyLabel.TextPositionRule.END);
            setTextPositioningY(EasyLabel.TextPositionRule.CENTER);
        }

        @Override
        public void renderTick(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            setText(getTimerText());
            super.renderTick(guiGraphics, mouseX, mouseY, partialTick);
        }
    }
}
