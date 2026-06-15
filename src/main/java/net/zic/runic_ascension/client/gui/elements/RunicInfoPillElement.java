package net.zic.runic_ascension.client.gui.elements;

import net.lucent.easygui.gui.RenderableElement;
import net.lucent.easygui.gui.UIFrame;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

/**
 * Compact badge for small casting-state values such as timer, qi cost, mode name, or charge level.
 */
public class RunicInfoPillElement extends RenderableElement {
    private Component text = Component.empty();
    private int fillColor = RunicGuiTheme.PANEL_SOFT;
    private int outlineColor = RunicGuiTheme.BORDER_MUTED;
    private int textColor = RunicGuiTheme.TEXT_MUTED;

    public RunicInfoPillElement(UIFrame frame, int x, int y, int width, int height, Component text) {
        super(frame, x, y);
        this.text = text == null ? Component.empty() : text;
        setWidth(width);
        setHeight(height);
    }

    public void setText(Component text) {
        this.text = text == null ? Component.empty() : text;
    }

    public RunicInfoPillElement withColors(int fillColor, int outlineColor, int textColor) {
        this.fillColor = fillColor;
        this.outlineColor = outlineColor;
        this.textColor = textColor;
        return this;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.fill(0, 0, getWidth(), getHeight(), fillColor);
        guiGraphics.fill(1, 1, getWidth() - 1, getHeight() - 1, RunicGuiTheme.PANEL_DEEP);
        guiGraphics.renderOutline(0, 0, getWidth(), getHeight(), outlineColor);

        Minecraft minecraft = Minecraft.getInstance();
        int textY = (getHeight() - minecraft.font.lineHeight) / 2 + 1;
        guiGraphics.drawCenteredString(minecraft.font, text, getWidth() / 2, textY, textColor);
    }
}
