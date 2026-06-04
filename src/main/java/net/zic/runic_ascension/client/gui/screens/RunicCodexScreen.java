package net.zic.runic_ascension.client.gui.screens;

import net.lucent.easygui.gui.RenderableElement;
import net.lucent.easygui.gui.UIFrame;
import net.lucent.easygui.gui.layout.positioning.rules.PositioningRules;
import net.lucent.easygui.screen.EasyScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.zic.runic_ascension.client.gui.elements.RunicGuiTheme;
import net.zic.runic_ascension.client.gui.elements.RunicLabel;
import net.zic.runic_ascension.client.gui.elements.RunicParchmentPanelElement;
import net.zic.runic_ascension.client.gui.elements.RunicTextBlockElement;
import net.zic.runic_ascension.client.gui.elements.RunicTextButton;
import net.zic.runic_ascension.content.RunicScrapLore;
import net.zic.runic_ascension.content.casting.RunicDiscoveredFormula;
import net.zic.runic_ascension.content.casting.RunicEffectProfile;
import net.zic.runic_ascension.content.casting.RunicFormula;
import net.zic.runic_ascension.content.casting.RunicFormulaInterpreter;
import net.zic.runic_ascension.content.casting.RunicFormulaMasteryGrade;
import net.zic.runic_ascension.content.casting.RunicFormulaParser;
import net.zic.runic_ascension.content.runes.IRunicRune;
import net.zic.runic_ascension.content.runes.ModRunicRunes;
import net.zic.runic_ascension.content.sequences.IRunicSequence;
import net.zic.runic_ascension.content.sequences.ModRunicSequences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RunicCodexScreen extends EasyScreen {

    private static final int PANEL_WIDTH = 540;
    private static final int PANEL_HEIGHT = 348;
    private static final int SIDE_WIDTH = 248;
    private static final int LEFT_X = 18;
    private static final int RIGHT_X = 274;
    private static final int HEADER_Y = 32;
    private static final int LIST_Y = 50;
    private static final int LIST_HEIGHT = 152;
    private static final int PAGE_Y = 210;
    private static final int DETAIL_Y = 232;
    private static final int DETAIL_HEIGHT = 94;
    private static final int PAGE_SIZE = 8;
    private static final int SCRAP_ROTATION_TICKS = 100;

    private final List<ResourceLocation> knownRunes;
    private final List<ResourceLocation> discoveredSequences;
    private final Map<ResourceLocation, RunicDiscoveredFormula> discoveredFormulaData;
    private final List<String> discoveredScraps;

    private RenderableElement runeListContainer;
    private RenderableElement sequenceListContainer;
    private RenderableElement runeDetailContainer;
    private RenderableElement sequenceDetailContainer;
    private RunicLabel runePageLabel;
    private RunicLabel sequencePageLabel;

    private int runePage;
    private int sequencePage;
    private int scrapRotationTicks;
    private int scrapRotationIndex;
    private ResourceLocation selectedRune;
    private ResourceLocation selectedSequence;

    public RunicCodexScreen(List<ResourceLocation> knownRunes, List<ResourceLocation> discoveredSequences) {
        this(knownRunes, discoveredSequences, List.of(), List.of(), "");
    }

    public RunicCodexScreen(
            List<ResourceLocation> knownRunes,
            List<ResourceLocation> discoveredSequences,
            List<RunicDiscoveredFormula> discoveredFormulas
    ) {
        this(knownRunes, discoveredSequences, discoveredFormulas, List.of(), "");
    }

    public RunicCodexScreen(
            List<ResourceLocation> knownRunes,
            List<ResourceLocation> discoveredSequences,
            List<RunicDiscoveredFormula> discoveredFormulas,
            List<String> discoveredScraps,
            String focusedScrap
    ) {
        super(Component.translatable("runic_ascension.runic.codex.title"));

        this.knownRunes = knownRunes == null ? List.of() : List.copyOf(knownRunes);
        this.discoveredSequences = discoveredSequences == null ? List.of() : List.copyOf(discoveredSequences);
        this.discoveredFormulaData = new HashMap<>();
        this.discoveredScraps = normaliseScraps(discoveredScraps, focusedScrap);

        if (discoveredFormulas != null) {
            for (RunicDiscoveredFormula formula : discoveredFormulas) {
                if (formula != null) {
                    this.discoveredFormulaData.put(formula.formulaId(), formula);
                }
            }
        }

        build(getUIFrame());
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();

        if (selectedRune != null && selectedSequence != null) {
            return;
        }

        scrapRotationTicks++;

        if (scrapRotationTicks >= SCRAP_ROTATION_TICKS) {
            scrapRotationTicks = 0;
            advanceScrapRotation();
        }
    }

    private void build(UIFrame frame) {
        frame.setPauseGame(false);

        RunicParchmentPanelElement panel = new RunicParchmentPanelElement(frame, 0, 0, PANEL_WIDTH, PANEL_HEIGHT, true, true);
        panel.getPositioning().setPositioningRule(PositioningRules.CENTER);
        panel.getPositioning().setX(-PANEL_WIDTH / 2);
        panel.getPositioning().setY(-PANEL_HEIGHT / 2);
        frame.setRoot(panel);

        RunicLabel title = new RunicLabel(frame, Component.translatable("runic_ascension.runic.codex.title"), 0, 4, PANEL_WIDTH, 14, RunicGuiTheme.TEXT_TITLE).centered().scaled(1.0F);
        panel.addChild(title);

        panel.addChild(new RunicLabel(frame, Component.translatable("runic_ascension.runic.codex.runes"), LEFT_X, HEADER_Y, SIDE_WIDTH, 12, RunicGuiTheme.CODEX_INK).centered().scaled(0.92F));
        panel.addChild(new RunicLabel(frame, Component.translatable("runic_ascension.runic.codex.sequences"), RIGHT_X, HEADER_Y, SIDE_WIDTH, 12, RunicGuiTheme.CODEX_INK).centered().scaled(0.92F));

        runeListContainer = new RunicParchmentPanelElement(frame, LEFT_X, LIST_Y, SIDE_WIDTH, LIST_HEIGHT);
        panel.addChild(runeListContainer);

        sequenceListContainer = new RunicParchmentPanelElement(frame, RIGHT_X, LIST_Y, SIDE_WIDTH, LIST_HEIGHT);
        panel.addChild(sequenceListContainer);

        runeDetailContainer = new RotatingDetailPanel(frame, LEFT_X, DETAIL_Y, SIDE_WIDTH, DETAIL_HEIGHT, true);
        panel.addChild(runeDetailContainer);

        sequenceDetailContainer = new RotatingDetailPanel(frame, RIGHT_X, DETAIL_Y, SIDE_WIDTH, DETAIL_HEIGHT, false);
        panel.addChild(sequenceDetailContainer);

        addPageControls(panel, frame);
        refreshRuneList();
        refreshSequenceList();
        refreshRuneDetails();
        refreshSequenceDetails();
    }

    private void addPageControls(RenderableElement panel, UIFrame frame) {
        RunicTextButton runePrev = new CodexEntryButton(frame, LEFT_X, PAGE_Y, 38, 16, Component.literal("<")) {
            @Override
            public void onClick() {
                if (runePage > 0) {
                    runePage--;
                    refreshRuneList();
                }
            }
        };
        panel.addChild(runePrev);

        runePageLabel = new RunicLabel(frame, Component.empty(), LEFT_X + 42, PAGE_Y, SIDE_WIDTH - 84, 16, RunicGuiTheme.CODEX_INK_MUTED).centered().scaled(0.78F);
        panel.addChild(runePageLabel);

        RunicTextButton runeNext = new CodexEntryButton(frame, LEFT_X + SIDE_WIDTH - 38, PAGE_Y, 38, 16, Component.literal(">")) {
            @Override
            public void onClick() {
                if (runePage + 1 < getPageCount(knownRunes.size(), PAGE_SIZE)) {
                    runePage++;
                    refreshRuneList();
                }
            }
        };
        panel.addChild(runeNext);

        RunicTextButton sequencePrev = new CodexEntryButton(frame, RIGHT_X, PAGE_Y, 38, 16, Component.literal("<")) {
            @Override
            public void onClick() {
                if (sequencePage > 0) {
                    sequencePage--;
                    refreshSequenceList();
                }
            }
        };
        panel.addChild(sequencePrev);

        sequencePageLabel = new RunicLabel(frame, Component.empty(), RIGHT_X + 42, PAGE_Y, SIDE_WIDTH - 84, 16, RunicGuiTheme.CODEX_INK_MUTED).centered().scaled(0.78F);
        panel.addChild(sequencePageLabel);

        RunicTextButton sequenceNext = new CodexEntryButton(frame, RIGHT_X + SIDE_WIDTH - 38, PAGE_Y, 38, 16, Component.literal(">")) {
            @Override
            public void onClick() {
                if (sequencePage + 1 < getPageCount(discoveredSequences.size(), PAGE_SIZE)) {
                    sequencePage++;
                    refreshSequenceList();
                }
            }
        };
        panel.addChild(sequenceNext);
    }

    private void refreshRuneList() {
        runeListContainer.removeChildren();

        if (knownRunes.isEmpty()) {
            RunicLabel empty = new RunicLabel(getUIFrame(), Component.translatable("runic_ascension.runic.codex.no_runes"), 8, 10, SIDE_WIDTH - 16, 12, RunicGuiTheme.CODEX_INK_MUTED).centered().scaled(0.82F);
            runeListContainer.addChild(empty);
            refreshRunePageLabel();
            return;
        }

        int start = runePage * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, knownRunes.size());

        for (int i = start; i < end; i++) {
            ResourceLocation runeId = knownRunes.get(i);
            int localIndex = i - start;

            RunicTextButton button = new CodexEntryButton(getUIFrame(), 8, 7 + localIndex * 18, SIDE_WIDTH - 16, 16, getRuneName(runeId)) {
                @Override
                protected boolean isSelected() {
                    return runeId.equals(selectedRune);
                }

                @Override
                public void onClick() {
                    selectedRune = runeId.equals(selectedRune) ? null : runeId;
                    refreshRuneList();
                    refreshRuneDetails();
                }
            };

            runeListContainer.addChild(button);
        }

        refreshRunePageLabel();
    }

    private void refreshSequenceList() {
        sequenceListContainer.removeChildren();

        if (discoveredSequences.isEmpty()) {
            RunicLabel empty = new RunicLabel(getUIFrame(), Component.translatable("runic_ascension.runic.codex.no_sequences"), 8, 10, SIDE_WIDTH - 16, 12, RunicGuiTheme.CODEX_INK_MUTED).centered().scaled(0.82F);
            sequenceListContainer.addChild(empty);
            refreshSequencePageLabel();
            return;
        }

        int start = sequencePage * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, discoveredSequences.size());

        for (int i = start; i < end; i++) {
            ResourceLocation sequenceId = discoveredSequences.get(i);
            int localIndex = i - start;

            RunicTextButton button = new CodexEntryButton(getUIFrame(), 8, 7 + localIndex * 18, SIDE_WIDTH - 16, 16, getSequenceName(sequenceId)) {
                @Override
                protected boolean isSelected() {
                    return sequenceId.equals(selectedSequence);
                }

                @Override
                public void onClick() {
                    selectedSequence = sequenceId.equals(selectedSequence) ? null : sequenceId;
                    refreshSequenceList();
                    refreshSequenceDetails();
                }
            };

            sequenceListContainer.addChild(button);
        }

        refreshSequencePageLabel();
    }

    private void refreshRuneDetails() {
        runeDetailContainer.removeChildren();

        if (selectedRune == null) {
            addScrapRotation(runeDetailContainer, true);
            return;
        }

        addRuneDetails(selectedRune);
    }

    private void refreshSequenceDetails() {
        sequenceDetailContainer.removeChildren();

        if (selectedSequence == null) {
            addScrapRotation(sequenceDetailContainer, false);
            return;
        }

        addSequenceDetails(selectedSequence);
    }

    private void addRuneDetails(ResourceLocation runeId) {
        IRunicRune rune = ModRunicRunes.get(runeId);

        addDetailTitle(runeDetailContainer, getRuneName(runeId));

        if (rune == null) {
            addDetailLine(runeDetailContainer, Component.literal(runeId.toString()), 26);
            return;
        }

        addDetailLine(runeDetailContainer, Component.translatable(
                "runic_ascension.runic.codex.rune_details",
                RunicGuiTheme.formatEnumName(rune.getType().name()),
                RunicGuiTheme.formatEnumName(rune.getDepth().name()),
                rune.getMinimumRunicRealmToUse()
        ), 26);

        addDetailLine(runeDetailContainer, Component.translatable("runic_ascension.runic.codex.rune_observe", rune.getMinimumRunicRealmToObserve()), 44);
        addDetailLine(runeDetailContainer, Component.translatable("runic_ascension.runic.codex.rune_id", runeId.toString()), 62);
    }

    private void addSequenceDetails(ResourceLocation sequenceId) {
        if (isFormulaId(sequenceId)) {
            addFormulaDetails(sequenceId);
            return;
        }

        IRunicSequence sequence = ModRunicSequences.get(sequenceId);

        addDetailTitle(sequenceDetailContainer, getSequenceName(sequenceId));

        if (sequence == null) {
            addDetailLine(sequenceDetailContainer, Component.literal(sequenceId.toString()), 26);
            return;
        }

        addDetailLine(sequenceDetailContainer, Component.translatable("runic_ascension.runic.sequence." + sequenceId.getPath() + ".desc"), 24);

        addDetailLine(sequenceDetailContainer, Component.translatable(
                "runic_ascension.runic.codex.sequence_details",
                RunicGuiTheme.formatEnumName(sequence.getTier().name()),
                sequence.getMinimumRunicRealm(),
                sequence.getQiCost()
        ), 46);

        addDetailLine(sequenceDetailContainer, Component.translatable("runic_ascension.runic.codex.sequence_formula", formatRuneList(sequence.getRequiredRunes())), 64);
    }

    private void addFormulaDetails(ResourceLocation formulaId) {
        RunicDiscoveredFormula discoveredFormula = discoveredFormulaData.get(formulaId);
        List<ResourceLocation> runeIds = discoveredFormula == null
                ? getFormulaRuneIds(formulaId)
                : discoveredFormula.runes();

        RunicFormula formula = RunicFormulaParser.parse(runeIds);
        RunicEffectProfile profile = RunicFormulaInterpreter.interpret(formula);

        addDetailTitle(sequenceDetailContainer, Component.literal(profile.displayName()));

        int castCount = discoveredFormula == null ? 0 : discoveredFormula.castCount();
        RunicFormulaMasteryGrade grade = discoveredFormula == null
                ? RunicFormulaMasteryGrade.UNSTABLE
                : discoveredFormula.getMasteryGrade();

        addDetailLine(sequenceDetailContainer, Component.translatable(
                "runic_ascension.runic.codex.formula_summary",
                RunicGuiTheme.formatEnumName(profile.archetype().name()),
                formatProfilePath(profile.damageKind())
        ), 24);

        addDetailLine(sequenceDetailContainer, Component.translatable(
                "runic_ascension.runic.codex.formula_mastery",
                formatMasteryGrade(grade),
                castCount
        ), 42);

        addDetailLine(sequenceDetailContainer, Component.translatable(
                "runic_ascension.runic.codex.formula_profile_numbers",
                formatMultiplier(profile.damageMultiplier()),
                formatMultiplier(profile.rangeMultiplier()),
                formatSignedPercent(profile.stabilityModifier())
        ), 58);

        addDetailLine(sequenceDetailContainer, Component.translatable("runic_ascension.runic.codex.formula_runes", formatRuneList(runeIds)), 75);
    }

    private void addScrapRotation(RenderableElement container, boolean runeSide) {
        if (discoveredScraps.isEmpty()) {
            addDetailTitle(container, Component.translatable("runic_ascension.runic.codex.scrap_hint.title"));
            addDetailLine(container, Component.translatable("runic_ascension.runic.codex.scrap_hint.line_1"), 26);
            addDetailLine(container, Component.translatable("runic_ascension.runic.codex.scrap_hint.line_2"), 44);
            addDetailLine(container, Component.translatable(runeSide
                    ? "runic_ascension.runic.codex.scrap_hint.rune_side"
                    : "runic_ascension.runic.codex.scrap_hint.sequence_side"), 62);
            return;
        }

        String scrapKey = discoveredScraps.get(Math.floorMod(scrapRotationIndex + (runeSide ? 0 : 1), discoveredScraps.size()));
        RunicScrapLore.Entry entry = RunicScrapLore.get(scrapKey);

        if (entry == null) {
            addDetailTitle(container, Component.literal(scrapKey));
            return;
        }

        addDetailTitle(container, Component.translatable(entry.translationBase() + ".title"));

        int maxLines = Math.min(entry.lineCount(), 4);
        for (int i = 1; i <= maxLines; i++) {
            addDetailLine(container, Component.translatable(entry.translationBase() + ".line_" + i), 10 + i * 16);
        }
    }

    private void advanceScrapRotation() {
        if (!discoveredScraps.isEmpty()) {
            scrapRotationIndex = (scrapRotationIndex + 1) % discoveredScraps.size();
        }

        if (selectedRune == null && runeDetailContainer != null) {
            refreshRuneDetails();
        }

        if (selectedSequence == null && sequenceDetailContainer != null) {
            refreshSequenceDetails();
        }
    }

    private void addDetailTitle(RenderableElement container, Component text) {
        RunicTextBlockElement name = new RunicTextBlockElement(getUIFrame(), text, 9, 7, SIDE_WIDTH - 18, 14, RunicGuiTheme.CODEX_INK, 0.9F, true);
        container.addChild(name);
    }

    private void addDetailLine(RenderableElement container, Component text, int y) {
        RunicTextBlockElement line = new RunicTextBlockElement(getUIFrame(), text, 11, y, SIDE_WIDTH - 22, 18, RunicGuiTheme.CODEX_INK_MUTED, 0.78F, false);
        container.addChild(line);
    }

    private void refreshRunePageLabel() {
        if (runePageLabel != null) {
            runePageLabel.setText(Component.translatable(
                    "runic_ascension.runic.codex.page",
                    getDisplayedPage(knownRunes.size(), PAGE_SIZE, runePage),
                    getPageCount(knownRunes.size(), PAGE_SIZE)
            ));
        }
    }

    private void refreshSequencePageLabel() {
        if (sequencePageLabel != null) {
            sequencePageLabel.setText(Component.translatable(
                    "runic_ascension.runic.codex.page",
                    getDisplayedPage(discoveredSequences.size(), PAGE_SIZE, sequencePage),
                    getPageCount(discoveredSequences.size(), PAGE_SIZE)
            ));
        }
    }

    private static int getDisplayedPage(int entryCount, int pageSize, int page) {
        return entryCount <= 0 ? 0 : page + 1;
    }

    private static int getPageCount(int entryCount, int pageSize) {
        return entryCount <= 0 ? 0 : (entryCount + pageSize - 1) / pageSize;
    }

    private static Component getRuneName(ResourceLocation runeId) {
        IRunicRune rune = ModRunicRunes.get(runeId);
        return rune == null ? Component.literal(runeId.getPath()) : rune.getName();
    }

    private Component getSequenceName(ResourceLocation sequenceId) {
        if (isFormulaId(sequenceId)) {
            RunicDiscoveredFormula formula = discoveredFormulaData.get(sequenceId);
            return formula == null ? getFormulaName(sequenceId) : getFormulaName(formula.runes());
        }

        return Component.translatable("runic_ascension.runic.sequence." + sequenceId.getPath());
    }

    private static String formatRuneList(List<ResourceLocation> runeIds) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < runeIds.size(); i++) {
            if (i > 0) {
                builder.append(" > ");
            }

            IRunicRune rune = ModRunicRunes.get(runeIds.get(i));
            builder.append(rune == null ? runeIds.get(i).getPath() : rune.getName().getString());
        }

        return builder.toString();
    }

    private static boolean isFormulaId(ResourceLocation id) {
        return id.getPath().startsWith("formula/");
    }

    private static List<ResourceLocation> getFormulaRuneIds(ResourceLocation formulaId) {
        String raw = formulaId.getPath().substring("formula/".length());
        String[] parts = raw.split("_");
        List<ResourceLocation> runeIds = new ArrayList<>();

        for (String part : parts) {
            runeIds.add(ResourceLocation.fromNamespaceAndPath(formulaId.getNamespace(), part));
        }

        return runeIds;
    }

    private static Component getFormulaName(ResourceLocation formulaId) {
        return getFormulaName(getFormulaRuneIds(formulaId));
    }

    private static Component getFormulaName(List<ResourceLocation> runeIds) {
        if (runeIds.isEmpty()) {
            return Component.translatable("runic_ascension.runic.codex.unknown_formula");
        }

        RunicFormula formula = RunicFormulaParser.parse(runeIds);
        RunicEffectProfile profile = RunicFormulaInterpreter.interpret(formula);
        return Component.literal(profile.displayName());
    }

    private static String formatMasteryGrade(RunicFormulaMasteryGrade grade) {
        return RunicGuiTheme.formatEnumName(grade.name());
    }

    private static String formatProfilePath(String path) {
        if (path == null || path.isBlank()) {
            return "Runic";
        }

        return RunicGuiTheme.formatEnumName(path.replace('_', ' '));
    }

    private static String formatMultiplier(float value) {
        return String.format(java.util.Locale.ROOT, "%.2fx", value);
    }

    private static String formatSignedPercent(float value) {
        return String.format(java.util.Locale.ROOT, "%+d%%", Math.round(value * 100.0F));
    }

    private static List<String> normaliseScraps(List<String> rawScraps, String focusedScrap) {
        List<String> output = new ArrayList<>();

        if (focusedScrap != null && !focusedScrap.isBlank() && RunicScrapLore.get(focusedScrap) != null) {
            output.add(focusedScrap);
        }

        if (rawScraps != null) {
            for (String key : rawScraps) {
                if (key != null && RunicScrapLore.get(key) != null && !output.contains(key)) {
                    output.add(key);
                }
            }
        }

        return List.copyOf(output);
    }

    private class CodexEntryButton extends RunicTextButton {
        private final Component label;

        private CodexEntryButton(UIFrame frame, int x, int y, int width, int height, Component label) {
            super(frame, x, y, width, height, label);
            this.label = label == null ? Component.empty() : label;
        }

        @Override
        public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            boolean hovered = isPointBounded(mouseX, mouseY);
            boolean selected = isSelected();
            int fill = selected ? 0xCC6F4DBA : hovered ? 0x66BDA3E6 : 0x33FFF3D3;
            int outline = selected ? RunicGuiTheme.ACCENT : hovered ? RunicGuiTheme.CODEX_LINE : RunicGuiTheme.PARCHMENT_EDGE;
            int textColor = selected ? RunicGuiTheme.TEXT : RunicGuiTheme.CODEX_INK;

            guiGraphics.fill(0, 0, getWidth(), getHeight(), fill);
            guiGraphics.renderOutline(0, 0, getWidth(), getHeight(), outline);

            Minecraft minecraft = Minecraft.getInstance();
            int textY = (getHeight() - minecraft.font.lineHeight) / 2 + 1;
            int textX = (getWidth() - minecraft.font.width(label)) / 2;
            guiGraphics.drawString(minecraft.font, label, textX, textY, textColor, false);
        }
    }

    private class RotatingDetailPanel extends RunicParchmentPanelElement {
        private final boolean runeSide;

        private RotatingDetailPanel(UIFrame frame, int x, int y, int width, int height, boolean runeSide) {
            super(frame, x, y, width, height);
            this.runeSide = runeSide;
        }

        @Override
        public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            super.render(guiGraphics, mouseX, mouseY, partialTick);

            boolean rotating = runeSide ? selectedRune == null : selectedSequence == null;
            if (rotating) {
                int pulseWidth = discoveredScraps.isEmpty()
                        ? 0
                        : (int) ((getWidth() - 2) * (scrapRotationTicks / (double) SCRAP_ROTATION_TICKS));
                guiGraphics.fill(6, getHeight() - 9, 6 + pulseWidth, getHeight() - 8, RunicGuiTheme.ACCENT_SOFT);
            }
        }
    }
}
