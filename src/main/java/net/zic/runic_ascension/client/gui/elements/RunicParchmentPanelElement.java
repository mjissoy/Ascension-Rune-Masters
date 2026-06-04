package net.zic.runic_ascension.client.gui.elements;

import net.lucent.easygui.gui.RenderableElement;
import net.lucent.easygui.gui.UIFrame;
import net.minecraft.client.gui.GuiGraphics;

public class RunicParchmentPanelElement extends RenderableElement {
    private final boolean titleBar;
    private final boolean centerSeam;

    public RunicParchmentPanelElement(UIFrame frame, int x, int y, int width, int height) {
        this(frame, x, y, width, height, false, false);
    }

    public RunicParchmentPanelElement(UIFrame frame, int x, int y, int width, int height, boolean titleBar, boolean centerSeam) {
        super(frame, x, y);
        this.titleBar = titleBar;
        this.centerSeam = centerSeam;
        setWidth(width);
        setHeight(height);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int pageTop = titleBar ? 22 : 3;

        guiGraphics.fill(0, 0, getWidth(), getHeight(), RunicGuiTheme.CODEX_BINDING);
        guiGraphics.fill(1, 1, getWidth() - 1, getHeight() - 1, RunicGuiTheme.CODEX_BINDING_SOFT);
        guiGraphics.renderOutline(0, 0, getWidth(), getHeight(), RunicGuiTheme.BORDER);

        if (titleBar) {
            guiGraphics.fill(2, 2, getWidth() - 2, 21, RunicGuiTheme.CODEX_BINDING);
            guiGraphics.fill(2, 20, getWidth() - 2, 21, RunicGuiTheme.ACCENT);
        }

        guiGraphics.fill(4, pageTop, getWidth() - 4, getHeight() - 4, RunicGuiTheme.PARCHMENT_SHADOW);
        guiGraphics.fill(6, pageTop + 2, getWidth() - 6, getHeight() - 6, RunicGuiTheme.PARCHMENT);
        guiGraphics.renderOutline(5, pageTop + 1, getWidth() - 10, getHeight() - pageTop - 7, RunicGuiTheme.PARCHMENT_EDGE);

        if (centerSeam) {
            int seamX = getWidth() / 2;
            guiGraphics.fill(seamX - 2, pageTop + 8, seamX - 1, getHeight() - 10, RunicGuiTheme.PARCHMENT_EDGE);
            guiGraphics.fill(seamX, pageTop + 8, seamX + 1, getHeight() - 10, 0x55FFFFFF);
            guiGraphics.fill(seamX + 1, pageTop + 8, seamX + 2, getHeight() - 10, 0x332A1634);
        }
    }
}
