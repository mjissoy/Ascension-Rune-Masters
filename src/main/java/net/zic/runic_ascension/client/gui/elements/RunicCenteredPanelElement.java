package net.zic.runic_ascension.client.gui.elements;

import net.lucent.easygui.gui.UIFrame;
import net.lucent.easygui.gui.layout.positioning.rules.PositioningRules;

/**
 * A panel that anchors itself to the middle of the screen.
 * Useful for modal casting screens where each paradigm wants its own contents,
 * but the same centered frame behavior.
 */
public class RunicCenteredPanelElement extends RunicPanelElement {
    public RunicCenteredPanelElement(UIFrame frame, int width, int height) {
        this(frame, width, height, true, false);
    }

    public RunicCenteredPanelElement(UIFrame frame, int width, int height, boolean titleBar, boolean divider) {
        super(frame, 0, 0, width, height, titleBar, divider);
        getPositioning().setPositioningRule(PositioningRules.CENTER);
        getPositioning().setX(-width / 2);
        getPositioning().setY(-height / 2);
    }
}
