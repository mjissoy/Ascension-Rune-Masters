package net.zic.runic_ascension.client.gui.elements;

import net.lucent.easygui.gui.UIFrame;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Reusable slot strip for casting-style UIs.
 * It deliberately knows nothing about formulas, backlash, or rune grammar.
 */
public class RunicCastingSlotBarElement extends RunicPanelElement {
    private final RunicLabel statusLabel;
    private final List<RunicSlotElement> slots = new ArrayList<>();

    public RunicCastingSlotBarElement(
            UIFrame frame,
            int x,
            int y,
            int width,
            int height,
            int slotCount,
            int slotWidth,
            int slotHeight,
            int slotGap
    ) {
        super(frame, x, y, width, height);

        statusLabel = new RunicLabel(frame, Component.empty(), 8, 5, width - 16, 10, RunicGuiTheme.TEXT_MUTED)
                .centered()
                .scaled(0.76F);
        addChild(statusLabel);

        int safeSlotCount = Math.max(0, slotCount);
        int totalWidth = safeSlotCount * slotWidth + Math.max(0, safeSlotCount - 1) * slotGap;
        int startX = Math.max(8, (width - totalWidth) / 2);
        int slotY = Math.max(17, height - slotHeight - 8);

        for (int i = 0; i < safeSlotCount; i++) {
            RunicSlotElement slot = new RunicSlotElement(
                    frame,
                    startX + i * (slotWidth + slotGap),
                    slotY,
                    slotWidth,
                    slotHeight,
                    i
            );
            slots.add(slot);
            addChild(slot);
        }
    }

    public void setStatusText(Component text) {
        statusLabel.setText(text == null ? Component.empty() : text);
    }

    public void setSlotNames(List<Component> runeNames) {
        List<Component> safeNames = runeNames == null ? List.of() : runeNames;

        for (int i = 0; i < slots.size(); i++) {
            if (i < safeNames.size()) {
                slots.get(i).setRuneName(safeNames.get(i));
            } else {
                slots.get(i).setRuneName(null);
            }
        }
    }
}
