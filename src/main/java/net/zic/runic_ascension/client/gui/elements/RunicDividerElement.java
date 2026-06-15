package net.zic.runic_ascension.client.gui.elements;

import net.lucent.easygui.gui.RenderableElement;
import net.lucent.easygui.gui.UIFrame;
import net.minecraft.client.gui.GuiGraphics;

/** Lightweight divider element for building denser custom panels without hard-coding lines in screens. */
public class RunicDividerElement extends RenderableElement {
    private final int color;

    public RunicDividerElement(UIFrame frame, int x, int y, int width, int height) {
        this(frame, x, y, width, height, RunicGuiTheme.BORDER_DARK);
    }

    public RunicDividerElement(UIFrame frame, int x, int y, int width, int height, int color) {
        super(frame, x, y);
        this.color = color;
        setWidth(width);
        setHeight(height);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.fill(0, 0, getWidth(), getHeight(), color);
    }
}
