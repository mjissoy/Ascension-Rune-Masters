package net.zic.runic_ascension.client.gui.elements;

import net.lucent.easygui.gui.UIFrame;
import net.lucent.easygui.gui.elements.built_in.EasyButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class RunicTextButton extends EasyButton {
    private Component text;

    public RunicTextButton(UIFrame frame, int x, int y, int width, int height, Component text) {
        super(frame, x, y);
        this.text = text;
        setWidth(width);
        setHeight(height);
    }

    public void setButtonText(Component text) {
        this.text = text;
    }

    protected boolean isSelected() {
        return false;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        boolean manuallyHovered = isPointBounded(mouseX, mouseY);

        int color = isSelected()
                ? RunicGuiTheme.ACCENT_SOFT
                : isPressed() ? 0xCC6F4DBA : manuallyHovered ? RunicGuiTheme.ACCENT_HOVER : RunicGuiTheme.ACCENT_DARK;
        int outline = isSelected() || manuallyHovered ? RunicGuiTheme.BORDER : RunicGuiTheme.BORDER_MUTED;
        int textColor = isSelected() ? RunicGuiTheme.TEXT : RunicGuiTheme.TEXT_TITLE;

        guiGraphics.fill(0, 0, getWidth(), getHeight(), color);
        guiGraphics.renderOutline(0, 0, getWidth(), getHeight(), outline);

        Minecraft minecraft = Minecraft.getInstance();
        int textY = (getHeight() - minecraft.font.lineHeight) / 2 + 1;

        guiGraphics.drawCenteredString(
                minecraft.font,
                text,
                getWidth() / 2,
                textY,
                textColor
        );
    }
}
