package net.zic.runic_ascension.client.gui.elements;

import net.lucent.easygui.gui.RenderableElement;
import net.lucent.easygui.gui.UIFrame;
import net.minecraft.client.gui.GuiGraphics;

public class RunicPanelElement extends RenderableElement {
    private final boolean titleBar;
    private final boolean divider;

    public RunicPanelElement(UIFrame frame, int x, int y, int width, int height) {
        this(frame, x, y, width, height, false, false);
    }

    public RunicPanelElement(UIFrame frame, int x, int y, int width, int height, boolean titleBar, boolean divider) {
        super(frame, x, y);
        this.titleBar = titleBar;
        this.divider = divider;
        setWidth(width);
        setHeight(height);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.fill(0, 0, getWidth(), getHeight(), RunicGuiTheme.PANEL_SOFT);
        guiGraphics.fill(1, 1, getWidth() - 1, getHeight() - 1, RunicGuiTheme.PANEL_DEEP);
        guiGraphics.renderOutline(0, 0, getWidth(), getHeight(), RunicGuiTheme.BORDER_MUTED);

        if (titleBar) {
            guiGraphics.fill(0, 0, getWidth(), 18, RunicGuiTheme.ACCENT_DARK);
            guiGraphics.fill(0, 17, getWidth(), 18, RunicGuiTheme.ACCENT);
        }

        if (divider) {
            guiGraphics.fill(getWidth() / 2 - 1, 20, getWidth() / 2 + 1, getHeight() - 4, RunicGuiTheme.BORDER_DARK);
        }
    }
}
