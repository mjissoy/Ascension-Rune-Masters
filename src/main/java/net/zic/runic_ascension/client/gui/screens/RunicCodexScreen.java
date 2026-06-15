package net.zic.runic_ascension.client.gui.screens;

import net.lucent.easygui.gui.RenderableElement;
import net.lucent.easygui.gui.UIFrame;
import net.lucent.easygui.gui.layout.positioning.rules.PositioningRules;
import net.lucent.easygui.screen.EasyScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.zic.runic_ascension.client.gui.elements.RunicBookTabElement;
import net.zic.runic_ascension.client.gui.elements.RunicDividerElement;
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
import net.zic.runic_ascension.core.inscriptions.ModRunicInscriptions;
import net.zic.runic_ascension.core.inscriptions.RunicInscription;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RunicCodexScreen extends EasyScreen {

    private static final int PANEL_WIDTH = 560;
    private static final int PANEL_HEIGHT = 356;
    private static final int PAGE_WIDTH = 244;
    private static final int PAGE_HEIGHT = 286;
    private static final int LEFT_PAGE_X = 24;
    private static final int RIGHT_PAGE_X = 292;
    private static final int PAGE_Y = 42;
    private static final int CONTENT_X = 12;
    private static final int CONTENT_WIDTH = PAGE_WIDTH - 24;
    private static final int LIST_START_Y = 36;
    private static final int ENTRY_HEIGHT = 18;
    private static final int ENTRY_GAP = 4;
    private static final int PAGE_CONTROL_Y = 260;
    private static final int PAGE_SIZE = 9;

    private final List<ResourceLocation> knownRunes;
    private final List<ResourceLocation> discoveredSequences;
    private final Map<ResourceLocation, RunicDiscoveredFormula> discoveredFormulaData;
    private final List<String> discoveredScraps;
    private final Map<ResourceLocation, Integer> inscriptionTiers;
    private final int runicRealm;

    private RenderableElement leftPage;
    private RenderableElement rightPage;
    private CodexSection activeSection = CodexSection.RUNES;

    private int runePage;
    private int sequencePage;
    private int inscriptionPage;
    private int scrapPage;

    private ResourceLocation selectedRune;
    private ResourceLocation selectedSequence;
    private ResourceLocation selectedInscription;
    private String selectedScrap;

    public RunicCodexScreen(List<ResourceLocation> knownRunes, List<ResourceLocation> discoveredSequences) {
        this(knownRunes, discoveredSequences, List.of(), List.of(), "", Map.of(), 0);
    }

    public RunicCodexScreen(
            List<ResourceLocation> knownRunes,
            List<ResourceLocation> discoveredSequences,
            List<RunicDiscoveredFormula> discoveredFormulas
    ) {
        this(knownRunes, discoveredSequences, discoveredFormulas, List.of(), "", Map.of(), 0);
    }

    public RunicCodexScreen(
            List<ResourceLocation> knownRunes,
            List<ResourceLocation> discoveredSequences,
            List<RunicDiscoveredFormula> discoveredFormulas,
            List<String> discoveredScraps,
            String focusedScrap
    ) {
        this(knownRunes, discoveredSequences, discoveredFormulas, discoveredScraps, focusedScrap, Map.of(), 0);
    }

    public RunicCodexScreen(
            List<ResourceLocation> knownRunes,
            List<ResourceLocation> discoveredSequences,
            List<RunicDiscoveredFormula> discoveredFormulas,
            List<String> discoveredScraps,
            String focusedScrap,
            Map<ResourceLocation, Integer> inscriptionTiers,
            int runicRealm
    ) {
        super(Component.translatable("runic_ascension.runic.codex.title"));

        this.knownRunes = knownRunes == null ? List.of() : List.copyOf(knownRunes);
        this.discoveredSequences = discoveredSequences == null ? List.of() : List.copyOf(discoveredSequences);
        this.discoveredFormulaData = new HashMap<>();
        this.discoveredScraps = normaliseScraps(discoveredScraps, focusedScrap);
        this.inscriptionTiers = inscriptionTiers == null ? Map.of() : Map.copyOf(inscriptionTiers);
        this.runicRealm = Math.max(0, runicRealm);
        this.selectedScrap = this.discoveredScraps.isEmpty() ? null : this.discoveredScraps.getFirst();

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

    private void build(UIFrame frame) {
        frame.setPauseGame(false);

        RunicParchmentPanelElement panel = new RunicParchmentPanelElement(frame, 0, 0, PANEL_WIDTH, PANEL_HEIGHT, true, true);
        panel.getPositioning().setPositioningRule(PositioningRules.CENTER);
        panel.getPositioning().setX(-PANEL_WIDTH / 2);
        panel.getPositioning().setY(-PANEL_HEIGHT / 2);
        frame.setRoot(panel);

        RunicLabel title = new RunicLabel(frame, Component.translatable("runic_ascension.runic.codex.title"), 0, 5, PANEL_WIDTH, 14, RunicGuiTheme.TEXT_TITLE).centered().scaled(1.0F);
        panel.addChild(title);

        addTabs(panel, frame);

        leftPage = new RenderableElement(frame, LEFT_PAGE_X, PAGE_Y);
        leftPage.setWidth(PAGE_WIDTH);
        leftPage.setHeight(PAGE_HEIGHT);
        panel.addChild(leftPage);

        rightPage = new RenderableElement(frame, RIGHT_PAGE_X, PAGE_Y);
        rightPage.setWidth(PAGE_WIDTH);
        rightPage.setHeight(PAGE_HEIGHT);
        panel.addChild(rightPage);

        refreshPages();
    }

    private void addTabs(RenderableElement panel, UIFrame frame) {
        int x = -34;
        int y = 42;
        int width = 42;
        int height = 28;
        int gap = 5;

        addTab(panel, frame, x, y, width, height, CodexSection.RUNES, Component.translatable("runic_ascension.runic.codex.tab.runes"));
        addTab(panel, frame, x, y + (height + gap), width, height, CodexSection.SEQUENCES, Component.translatable("runic_ascension.runic.codex.tab.sequences"));
        addTab(panel, frame, x, y + 2 * (height + gap), width, height, CodexSection.INSCRIPTIONS, Component.translatable("runic_ascension.runic.codex.tab.inscriptions"));
        addTab(panel, frame, x, y + 3 * (height + gap), width, height, CodexSection.SCRAPS, Component.translatable("runic_ascension.runic.codex.tab.scraps"));
    }

    private void addTab(RenderableElement panel, UIFrame frame, int x, int y, int width, int height, CodexSection section, Component label) {
        panel.addChild(new RunicBookTabElement(
                frame,
                x,
                y,
                width,
                height,
                label,
                () -> activeSection == section,
                () -> {
                    activeSection = section;
                    refreshPages();
                }
        ));
    }

    private void refreshPages() {
        leftPage.removeChildren();
        rightPage.removeChildren();

        switch (activeSection) {
            case RUNES -> refreshRunesPages();
            case SEQUENCES -> refreshSequencePages();
            case INSCRIPTIONS -> refreshInscriptionPages();
            case SCRAPS -> refreshScrapPages();
        }
    }

    private void refreshRunesPages() {
        addPageTitle(leftPage, Component.translatable("runic_ascension.runic.codex.page.runes.left"));
        addPageTitle(rightPage, selectedRune == null
                ? Component.translatable("runic_ascension.runic.codex.page.runes.right.empty")
                : getRuneName(selectedRune));

        if (knownRunes.isEmpty()) {
            addText(leftPage, Component.translatable("runic_ascension.runic.codex.no_runes"), CONTENT_X, LIST_START_Y, CONTENT_WIDTH, 28, RunicGuiTheme.CODEX_INK_MUTED, 0.82F, true);
        } else {
            addResourceList(leftPage, knownRunes, runePage, runeId -> runeId.equals(selectedRune), runeId -> {
                selectedRune = runeId.equals(selectedRune) ? null : runeId;
                refreshPages();
            }, RunicCodexScreen::getRuneName);
            addPageControls(leftPage, knownRunes.size(), runePage, value -> {
                runePage = value;
                refreshPages();
            });
        }

        if (selectedRune == null) {
            addText(rightPage, Component.translatable("runic_ascension.runic.codex.rune_select_hint"), CONTENT_X, 48, CONTENT_WIDTH, 56, RunicGuiTheme.CODEX_INK_MUTED, 0.82F, true);
            return;
        }

        addRuneDetails(rightPage, selectedRune, 34);
        rightPage.addChild(new RunicDividerElement(getUIFrame(), CONTENT_X, 134, CONTENT_WIDTH, 1, RunicGuiTheme.CODEX_LINE));
        addSubheading(rightPage, Component.translatable("runic_ascension.runic.codex.rune_sequences"), 142);
        addSequencesUsingRune(rightPage, selectedRune, 160);
    }

    private void refreshSequencePages() {
        addPageTitle(leftPage, Component.translatable("runic_ascension.runic.codex.page.sequences.left"));
        addPageTitle(rightPage, selectedSequence == null
                ? Component.translatable("runic_ascension.runic.codex.page.sequences.right.empty")
                : getSequenceName(selectedSequence));

        if (discoveredSequences.isEmpty()) {
            addText(leftPage, Component.translatable("runic_ascension.runic.codex.no_sequences"), CONTENT_X, LIST_START_Y, CONTENT_WIDTH, 28, RunicGuiTheme.CODEX_INK_MUTED, 0.82F, true);
        } else {
            addResourceList(leftPage, discoveredSequences, sequencePage, sequenceId -> sequenceId.equals(selectedSequence), sequenceId -> {
                selectedSequence = sequenceId.equals(selectedSequence) ? null : sequenceId;
                refreshPages();
            }, this::getSequenceName);
            addPageControls(leftPage, discoveredSequences.size(), sequencePage, value -> {
                sequencePage = value;
                refreshPages();
            });
        }

        if (selectedSequence == null) {
            addText(rightPage, Component.translatable("runic_ascension.runic.codex.sequence_select_hint"), CONTENT_X, 48, CONTENT_WIDTH, 56, RunicGuiTheme.CODEX_INK_MUTED, 0.82F, true);
            return;
        }

        addSequenceDetails(rightPage, selectedSequence);
    }

    private void refreshInscriptionPages() {
        List<ResourceLocation> inscriptions = getRegisteredInscriptionIds();

        if (selectedInscription == null && !inscriptions.isEmpty()) {
            selectedInscription = inscriptions.getFirst();
        }

        addPageTitle(leftPage, Component.translatable("runic_ascension.runic.codex.page.inscriptions.left"));
        addPageTitle(rightPage, selectedInscription == null
                ? Component.translatable("runic_ascension.runic.codex.page.inscriptions.right.empty")
                : getInscriptionName(selectedInscription));

        if (inscriptions.isEmpty()) {
            addText(leftPage, Component.translatable("runic_ascension.runic.codex.no_inscriptions"), CONTENT_X, LIST_START_Y, CONTENT_WIDTH, 28, RunicGuiTheme.CODEX_INK_MUTED, 0.82F, true);
        } else {
            addResourceList(leftPage, inscriptions, inscriptionPage, id -> id.equals(selectedInscription), id -> {
                selectedInscription = id;
                refreshPages();
            }, this::getInscriptionListName);
            addPageControls(leftPage, inscriptions.size(), inscriptionPage, value -> {
                inscriptionPage = value;
                refreshPages();
            });
        }

        if (selectedInscription == null) {
            addText(rightPage, Component.translatable("runic_ascension.runic.codex.inscription_select_hint"), CONTENT_X, 48, CONTENT_WIDTH, 56, RunicGuiTheme.CODEX_INK_MUTED, 0.82F, true);
            return;
        }

        addInscriptionDetails(rightPage, selectedInscription);
    }

    private void refreshScrapPages() {
        addPageTitle(leftPage, Component.translatable("runic_ascension.runic.codex.page.scraps.left"));
        addPageTitle(rightPage, selectedScrap == null
                ? Component.translatable("runic_ascension.runic.codex.page.scraps.right.empty")
                : getScrapTitle(selectedScrap));

        if (discoveredScraps.isEmpty()) {
            addText(leftPage, Component.translatable("runic_ascension.runic.codex.no_scraps"), CONTENT_X, LIST_START_Y, CONTENT_WIDTH, 36, RunicGuiTheme.CODEX_INK_MUTED, 0.82F, true);
            addText(rightPage, Component.translatable("runic_ascension.runic.codex.scrap_hint.line_1"), CONTENT_X, 48, CONTENT_WIDTH, 40, RunicGuiTheme.CODEX_INK_MUTED, 0.82F, true);
            return;
        }

        addStringList(leftPage, discoveredScraps, scrapPage, key -> key.equals(selectedScrap), key -> {
            selectedScrap = key.equals(selectedScrap) ? null : key;
            refreshPages();
        }, RunicCodexScreen::getScrapTitle);
        addPageControls(leftPage, discoveredScraps.size(), scrapPage, value -> {
            scrapPage = value;
            refreshPages();
        });

        if (selectedScrap == null) {
            addText(rightPage, Component.translatable("runic_ascension.runic.codex.scrap_select_hint"), CONTENT_X, 48, CONTENT_WIDTH, 56, RunicGuiTheme.CODEX_INK_MUTED, 0.82F, true);
            return;
        }

        addScrapDetails(rightPage, selectedScrap);
    }

    private void addRuneDetails(RenderableElement page, ResourceLocation runeId, int y) {
        IRunicRune rune = ModRunicRunes.get(runeId);

        if (rune == null) {
            addText(page, Component.literal(runeId.toString()), CONTENT_X, y, CONTENT_WIDTH, 24, RunicGuiTheme.CODEX_INK_MUTED, 0.82F, false);
            return;
        }

        addText(page, Component.translatable(
                "runic_ascension.runic.codex.rune_details",
                RunicGuiTheme.formatEnumName(rune.getType().name()),
                RunicGuiTheme.formatEnumName(rune.getDepth().name()),
                rune.getMinimumRunicRealmToUse()
        ), CONTENT_X, y, CONTENT_WIDTH, 32, RunicGuiTheme.CODEX_INK_MUTED, 0.78F, false);

        addText(page, Component.translatable("runic_ascension.runic.codex.rune_observe", rune.getMinimumRunicRealmToObserve()), CONTENT_X, y + 34, CONTENT_WIDTH, 18, RunicGuiTheme.CODEX_INK_MUTED, 0.78F, false);
        addText(page, Component.translatable("runic_ascension.runic.codex.rune_id", runeId.toString()), CONTENT_X, y + 54, CONTENT_WIDTH, 32, RunicGuiTheme.CODEX_INK_MUTED, 0.72F, false);
    }

    private void addSequencesUsingRune(RenderableElement page, ResourceLocation runeId, int y) {
        List<ResourceLocation> matches = discoveredSequences.stream()
                .filter(sequenceId -> getSequenceRuneIds(sequenceId).contains(runeId))
                .sorted(Comparator.comparing(ResourceLocation::toString))
                .toList();

        if (matches.isEmpty()) {
            addText(page, Component.translatable("runic_ascension.runic.codex.rune_sequences.none"), CONTENT_X, y, CONTENT_WIDTH, 32, RunicGuiTheme.CODEX_INK_MUTED, 0.78F, true);
            return;
        }

        int max = Math.min(matches.size(), 5);
        for (int i = 0; i < max; i++) {
            ResourceLocation sequenceId = matches.get(i);
            addText(page, Component.literal("• ").append(getSequenceName(sequenceId)), CONTENT_X, y + i * 19, CONTENT_WIDTH, 18, RunicGuiTheme.CODEX_INK_MUTED, 0.78F, false);
        }

        if (matches.size() > max) {
            addText(page, Component.translatable("runic_ascension.runic.codex.more_entries", matches.size() - max), CONTENT_X, y + max * 19, CONTENT_WIDTH, 18, RunicGuiTheme.CODEX_INK_MUTED, 0.72F, false);
        }
    }

    private void addSequenceDetails(RenderableElement page, ResourceLocation sequenceId) {
        if (isFormulaId(sequenceId)) {
            addFormulaDetails(page, sequenceId);
            return;
        }

        IRunicSequence sequence = ModRunicSequences.get(sequenceId);

        if (sequence == null) {
            addText(page, Component.literal(sequenceId.toString()), CONTENT_X, 42, CONTENT_WIDTH, 24, RunicGuiTheme.CODEX_INK_MUTED, 0.82F, false);
            return;
        }

        addText(page, Component.translatable("runic_ascension.runic.sequence." + sequenceId.getPath() + ".desc"), CONTENT_X, 34, CONTENT_WIDTH, 44, RunicGuiTheme.CODEX_INK, 0.78F, false);
        int y = 84;

        addText(page, Component.translatable(
                "runic_ascension.runic.codex.sequence_details",
                RunicGuiTheme.formatEnumName(sequence.getTier().name()),
                sequence.getMinimumRunicRealm(),
                sequence.getQiCost()
        ), CONTENT_X, y, CONTENT_WIDTH, 28, RunicGuiTheme.CODEX_INK_MUTED, 0.78F, false);
        y += 34;

        if (runicRealm >= 2) {
            addText(page, Component.translatable("runic_ascension.runic.codex.sequence_order", sequence.isOrderSensitive()
                    ? Component.translatable("runic_ascension.runic.codex.yes")
                    : Component.translatable("runic_ascension.runic.codex.no")), CONTENT_X, y, CONTENT_WIDTH, 18, RunicGuiTheme.CODEX_INK_MUTED, 0.78F, false);
            y += 22;
        }

        if (runicRealm >= 3) {
            addText(page, Component.translatable("runic_ascension.runic.codex.sequence_formula", formatRuneList(sequence.getRequiredRunes())), CONTENT_X, y, CONTENT_WIDTH, 44, RunicGuiTheme.CODEX_INK_MUTED, 0.76F, false);
            y += 50;
        }

        if (runicRealm >= 5) {
            addText(page, Component.translatable("runic_ascension.runic.codex.sequence_known_note"), CONTENT_X, y, CONTENT_WIDTH, 42, RunicGuiTheme.CODEX_INK_MUTED, 0.76F, false);
            y += 48;
        }

        addRealmVeilHint(page, y, 5);
    }

    private void addFormulaDetails(RenderableElement page, ResourceLocation formulaId) {
        RunicDiscoveredFormula discoveredFormula = discoveredFormulaData.get(formulaId);
        List<ResourceLocation> runeIds = discoveredFormula == null ? getFormulaRuneIds(formulaId) : discoveredFormula.runes();

        RunicFormula formula = RunicFormulaParser.parse(runeIds);
        RunicEffectProfile profile = RunicFormulaInterpreter.interpret(formula);

        addText(page, Component.translatable("runic_ascension.runic.codex.formula_type"), CONTENT_X, 34, CONTENT_WIDTH, 18, RunicGuiTheme.CODEX_INK_MUTED, 0.78F, false);
        addText(page, Component.translatable("runic_ascension.runic.codex.generated_formula_desc", formatProfilePath(profile.archetype().name()), formatProfilePath(profile.sourcePath()), formatProfilePath(profile.intentPath())), CONTENT_X, 54, CONTENT_WIDTH, 44, RunicGuiTheme.CODEX_INK, 0.78F, false);

        int y = 106;
        addText(page, Component.translatable("runic_ascension.runic.codex.formula_runes", formatRuneList(runeIds)), CONTENT_X, y, CONTENT_WIDTH, 42, RunicGuiTheme.CODEX_INK_MUTED, 0.76F, false);
        y += 48;

        if (runicRealm >= 2) {
            addText(page, Component.translatable(
                    "runic_ascension.runic.codex.formula_roles",
                    formatProfilePath(profile.sourcePath()),
                    formatProfilePath(profile.intentPath()),
                    formatProfilePath(profile.formPath())
            ), CONTENT_X, y, CONTENT_WIDTH, 28, RunicGuiTheme.CODEX_INK_MUTED, 0.76F, false);
            y += 34;
        }

        if (runicRealm >= 3) {
            addText(page, Component.translatable("runic_ascension.runic.codex.formula_summary", RunicGuiTheme.formatEnumName(profile.archetype().name()), formatProfilePath(profile.damageKind())), CONTENT_X, y, CONTENT_WIDTH, 28, RunicGuiTheme.CODEX_INK_MUTED, 0.76F, false);
            y += 34;
        }

        if (runicRealm >= 4) {
            int castCount = discoveredFormula == null ? 0 : discoveredFormula.castCount();
            RunicFormulaMasteryGrade grade = discoveredFormula == null ? RunicFormulaMasteryGrade.UNSTABLE : discoveredFormula.getMasteryGrade();
            addText(page, Component.translatable("runic_ascension.runic.codex.formula_mastery", formatMasteryGrade(grade), castCount), CONTENT_X, y, CONTENT_WIDTH, 18, RunicGuiTheme.CODEX_INK_MUTED, 0.76F, false);
            y += 24;
        }

        if (runicRealm >= 5) {
            addText(page, Component.translatable("runic_ascension.runic.codex.formula_profile_numbers", formatMultiplier(profile.damageMultiplier()), formatMultiplier(profile.rangeMultiplier()), formatSignedPercent(profile.stabilityModifier())), CONTENT_X, y, CONTENT_WIDTH, 28, RunicGuiTheme.CODEX_INK_MUTED, 0.76F, false);
            y += 34;
        }

        if (runicRealm >= 6) {
            addText(page, Component.translatable("runic_ascension.runic.codex.formula_flags", profile.flagsForDisplay()), CONTENT_X, y, CONTENT_WIDTH, 28, RunicGuiTheme.CODEX_INK_MUTED, 0.76F, false);
            y += 34;
        }

        addRealmVeilHint(page, y, 6);
    }

    private void addInscriptionDetails(RenderableElement page, ResourceLocation inscriptionId) {
        RunicInscription inscription = ModRunicInscriptions.get(inscriptionId);

        if (inscription == null) {
            addText(page, Component.literal(inscriptionId.toString()), CONTENT_X, 42, CONTENT_WIDTH, 24, RunicGuiTheme.CODEX_INK_MUTED, 0.82F, false);
            return;
        }

        int currentTier = getInscriptionTier(inscriptionId);
        RunicInscription.Tier current = inscription.getTier(currentTier);
        RunicInscription.Tier next = inscription.getNextTier(currentTier);

        addText(page, inscription.getDescription(), CONTENT_X, 34, CONTENT_WIDTH, 54, RunicGuiTheme.CODEX_INK, 0.76F, false);
        addText(page, Component.translatable("runic_ascension.runic.codex.inscription_current", getInscriptionTierText(inscription, currentTier)), CONTENT_X, 94, CONTENT_WIDTH, 20, RunicGuiTheme.CODEX_INK_MUTED, 0.78F, false);

        int y = 122;
        if (current != null) {
            addSubheading(page, Component.translatable("runic_ascension.runic.codex.inscription_effect"), y);
            addText(page, inscription.getTierDescription(currentTier), CONTENT_X, y + 18, CONTENT_WIDTH, 42, RunicGuiTheme.CODEX_INK_MUTED, 0.76F, false);
            y += 68;
        }

        page.addChild(new RunicDividerElement(getUIFrame(), CONTENT_X, y, CONTENT_WIDTH, 1, RunicGuiTheme.CODEX_LINE));
        y += 10;

        if (next == null) {
            addText(page, Component.translatable("runic_ascension.runic.codex.inscription_maxed"), CONTENT_X, y, CONTENT_WIDTH, 32, RunicGuiTheme.CODEX_INK_MUTED, 0.78F, true);
            return;
        }

        addSubheading(page, Component.translatable("runic_ascension.runic.codex.inscription_next", inscription.getTierName(next.tier())), y);
        y += 18;
        addText(page, nextRequirementText(next), CONTENT_X, y, CONTENT_WIDTH, 36, RunicGuiTheme.CODEX_INK_MUTED, 0.76F, false);
        y += 40;
        addText(page, inscription.getTierDescription(next.tier()), CONTENT_X, y, CONTENT_WIDTH, 42, RunicGuiTheme.CODEX_INK_MUTED, 0.76F, false);
    }

    private void addScrapDetails(RenderableElement page, String scrapKey) {
        RunicScrapLore.Entry entry = RunicScrapLore.get(scrapKey);

        if (entry == null) {
            addText(page, Component.literal(scrapKey), CONTENT_X, 42, CONTENT_WIDTH, 24, RunicGuiTheme.CODEX_INK_MUTED, 0.82F, false);
            return;
        }

        int y = 38;
        for (int i = 1; i <= entry.lineCount(); i++) {
            addText(page, Component.translatable(entry.translationBase() + ".line_" + i), CONTENT_X, y, CONTENT_WIDTH, 28, RunicGuiTheme.CODEX_INK, 0.78F, false);
            y += 31;
        }
    }

    private void addResourceList(
            RenderableElement page,
            List<ResourceLocation> entries,
            int pageIndex,
            ResourcePredicate selectedPredicate,
            ResourceConsumer clickConsumer,
            ResourceLabelProvider labelProvider
    ) {
        int start = pageIndex * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, entries.size());

        for (int i = start; i < end; i++) {
            ResourceLocation id = entries.get(i);
            int y = LIST_START_Y + (i - start) * (ENTRY_HEIGHT + ENTRY_GAP);
            page.addChild(new CodexEntryButton(getUIFrame(), CONTENT_X, y, CONTENT_WIDTH, ENTRY_HEIGHT, labelProvider.getLabel(id)) {
                @Override
                protected boolean isSelected() {
                    return selectedPredicate.test(id);
                }

                @Override
                public void onClick() {
                    clickConsumer.accept(id);
                }
            });
        }
    }

    private void addStringList(
            RenderableElement page,
            List<String> entries,
            int pageIndex,
            StringPredicate selectedPredicate,
            StringConsumer clickConsumer,
            StringLabelProvider labelProvider
    ) {
        int start = pageIndex * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, entries.size());

        for (int i = start; i < end; i++) {
            String value = entries.get(i);
            int y = LIST_START_Y + (i - start) * (ENTRY_HEIGHT + ENTRY_GAP);
            page.addChild(new CodexEntryButton(getUIFrame(), CONTENT_X, y, CONTENT_WIDTH, ENTRY_HEIGHT, labelProvider.getLabel(value)) {
                @Override
                protected boolean isSelected() {
                    return selectedPredicate.test(value);
                }

                @Override
                public void onClick() {
                    clickConsumer.accept(value);
                }
            });
        }
    }

    private void addPageControls(RenderableElement page, int entryCount, int currentPage, PageSetter setter) {
        int pageCount = getPageCount(entryCount, PAGE_SIZE);
        if (pageCount <= 1) {
            return;
        }

        page.addChild(new CodexEntryButton(getUIFrame(), CONTENT_X, PAGE_CONTROL_Y, 38, 16, Component.literal("<")) {
            @Override
            public void onClick() {
                if (currentPage > 0) {
                    setter.set(currentPage - 1);
                }
            }
        });

        page.addChild(new RunicLabel(
                getUIFrame(),
                Component.translatable("runic_ascension.runic.codex.page", currentPage + 1, pageCount),
                CONTENT_X + 42,
                PAGE_CONTROL_Y,
                CONTENT_WIDTH - 84,
                16,
                RunicGuiTheme.CODEX_INK_MUTED
        ).centered().scaled(0.78F));

        page.addChild(new CodexEntryButton(getUIFrame(), CONTENT_X + CONTENT_WIDTH - 38, PAGE_CONTROL_Y, 38, 16, Component.literal(">")) {
            @Override
            public void onClick() {
                if (currentPage + 1 < pageCount) {
                    setter.set(currentPage + 1);
                }
            }
        });
    }

    private void addPageTitle(RenderableElement page, Component title) {
        page.addChild(new RunicLabel(getUIFrame(), title, CONTENT_X, 4, CONTENT_WIDTH, 18, RunicGuiTheme.CODEX_INK).centered().scaled(0.92F));
        page.addChild(new RunicDividerElement(getUIFrame(), CONTENT_X, 28, CONTENT_WIDTH, 1, RunicGuiTheme.CODEX_LINE));
    }

    private void addSubheading(RenderableElement page, Component title, int y) {
        page.addChild(new RunicLabel(getUIFrame(), title, CONTENT_X, y, CONTENT_WIDTH, 14, RunicGuiTheme.CODEX_INK).centered().scaled(0.82F));
    }

    private void addText(RenderableElement page, Component text, int x, int y, int width, int height, int color, float scale, boolean centered) {
        page.addChild(new RunicTextBlockElement(getUIFrame(), text, x, y, width, height, color, scale, centered));
    }

    private void addRealmVeilHint(RenderableElement page, int y, int revealRealm) {
        if (runicRealm >= revealRealm || y > PAGE_HEIGHT - 32) {
            return;
        }

        addText(page, Component.translatable("runic_ascension.runic.codex.realm_veil", revealRealm), CONTENT_X, y, CONTENT_WIDTH, 30, RunicGuiTheme.CODEX_INK_MUTED, 0.72F, true);
    }

    private List<ResourceLocation> getSequenceRuneIds(ResourceLocation sequenceId) {
        if (isFormulaId(sequenceId)) {
            RunicDiscoveredFormula formula = discoveredFormulaData.get(sequenceId);
            return formula == null ? getFormulaRuneIds(sequenceId) : formula.runes();
        }

        IRunicSequence sequence = ModRunicSequences.get(sequenceId);
        return sequence == null ? List.of() : sequence.getRequiredRunes();
    }

    private List<ResourceLocation> getRegisteredInscriptionIds() {
        return ModRunicInscriptions.all().stream()
                .map(RunicInscription::getId)
                .sorted(Comparator.comparing(ResourceLocation::toString))
                .toList();
    }

    private Component getInscriptionListName(ResourceLocation inscriptionId) {
        RunicInscription inscription = ModRunicInscriptions.get(inscriptionId);
        Component name = getInscriptionName(inscriptionId);
        int tier = getInscriptionTier(inscriptionId);

        if (inscription == null || tier <= 0) {
            return Component.translatable("runic_ascension.runic.codex.inscription_list.none", name);
        }

        return Component.translatable("runic_ascension.runic.codex.inscription_list.tier", name, tier, inscription.getMaxTier());
    }

    private Component getInscriptionName(ResourceLocation inscriptionId) {
        RunicInscription inscription = ModRunicInscriptions.get(inscriptionId);
        return inscription == null ? Component.literal(inscriptionId.getPath()) : inscription.getName();
    }

    private int getInscriptionTier(ResourceLocation inscriptionId) {
        return inscriptionTiers.getOrDefault(inscriptionId, 0);
    }

    private Component getInscriptionTierText(RunicInscription inscription, int tier) {
        if (inscription == null || tier <= 0) {
            return Component.translatable("runic_ascension.runic.codex.inscription_none");
        }

        return Component.translatable("runic_ascension.runic.codex.inscription_tier", inscription.getTierName(tier), tier, inscription.getMaxTier());
    }

    private Component nextRequirementText(RunicInscription.Tier next) {
        List<ResourceLocation> missing = next.requiredRunes().stream()
                .filter(runeId -> !knownRunes.contains(runeId))
                .toList();

        Component runeText = Component.literal(formatRuneList(next.requiredRunes()));
        Component missingText = missing.isEmpty()
                ? Component.translatable("runic_ascension.runic.codex.none")
                : Component.literal(formatRuneList(missing));

        return Component.translatable(
                "runic_ascension.runic.codex.inscription_requirements",
                next.minimumRunicRealm() + 1,
                runeText,
                missingText
        );
    }

    private Component getSequenceName(ResourceLocation sequenceId) {
        if (isFormulaId(sequenceId)) {
            RunicDiscoveredFormula formula = discoveredFormulaData.get(sequenceId);
            return formula == null ? getFormulaName(sequenceId) : getFormulaName(formula.runes());
        }

        return Component.translatable("runic_ascension.runic.sequence." + sequenceId.getPath());
    }

    private static Component getRuneName(ResourceLocation runeId) {
        IRunicRune rune = ModRunicRunes.get(runeId);
        return rune == null ? Component.literal(runeId.getPath()) : rune.getName();
    }

    private static Component getScrapTitle(String scrapKey) {
        RunicScrapLore.Entry entry = RunicScrapLore.get(scrapKey);
        return entry == null ? Component.literal(scrapKey) : Component.translatable(entry.translationBase() + ".title");
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

    private static int getPageCount(int entryCount, int pageSize) {
        return entryCount <= 0 ? 0 : Math.ceilDiv(entryCount, pageSize);
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

    private enum CodexSection {
        RUNES,
        SEQUENCES,
        INSCRIPTIONS,
        SCRAPS
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
            int fill = selected ? 0xCC6F4DBA : hovered ? 0x66BDA3E6 : 0x22FFF3D3;
            int outline = selected ? RunicGuiTheme.ACCENT : hovered ? RunicGuiTheme.CODEX_LINE : RunicGuiTheme.PARCHMENT_EDGE;
            int textColor = selected ? RunicGuiTheme.TEXT : RunicGuiTheme.CODEX_INK;

            guiGraphics.fill(0, 0, getWidth(), getHeight(), fill);
            guiGraphics.renderOutline(0, 0, getWidth(), getHeight(), outline);

            Minecraft minecraft = Minecraft.getInstance();
            int textY = (getHeight() - minecraft.font.lineHeight) / 2 + 1;
            int textX = Math.max(5, (getWidth() - minecraft.font.width(label)) / 2);
            guiGraphics.drawString(minecraft.font, label, textX, textY, textColor, false);
        }
    }

    @FunctionalInterface
    private interface ResourcePredicate {
        boolean test(ResourceLocation id);
    }

    @FunctionalInterface
    private interface ResourceConsumer {
        void accept(ResourceLocation id);
    }

    @FunctionalInterface
    private interface ResourceLabelProvider {
        Component getLabel(ResourceLocation id);
    }

    @FunctionalInterface
    private interface StringPredicate {
        boolean test(String value);
    }

    @FunctionalInterface
    private interface StringConsumer {
        void accept(String value);
    }

    @FunctionalInterface
    private interface StringLabelProvider {
        Component getLabel(String value);
    }

    @FunctionalInterface
    private interface PageSetter {
        void set(int page);
    }
}
