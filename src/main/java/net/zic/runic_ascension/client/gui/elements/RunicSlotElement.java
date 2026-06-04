package net.zic.runic_ascension.client.gui.elements;

import net.lucent.easygui.gui.RenderableElement;
import net.lucent.easygui.gui.UIFrame;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class RunicSlotElement extends RenderableElement {
    private final int index;
    private Component runeName = Component.empty();
    private boolean filled;

    public RunicSlotElement(UIFrame frame, int x, int y, int size, int index) {
        super(frame, x, y);
        this.index = index;
        setWidth(size);
        setHeight(size);
    }

    public void setRuneName(Component runeName) {
        this.runeName = runeName == null ? Component.empty() : runeName;
        this.filled = runeName != null;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        boolean hovered = isPointBounded(mouseX, mouseY);
        int fill = filled ? 0xAA4F2D85 : 0x7712091F;
        int outline = hovered || filled ? RunicGuiTheme.BORDER : RunicGuiTheme.BORDER_MUTED;

        guiGraphics.fill(0, 0, getWidth(), getHeight(), fill);
        guiGraphics.renderOutline(0, 0, getWidth(), getHeight(), outline);

        Minecraft minecraft = Minecraft.getInstance();
        Component text = filled ? shortName(runeName) : Component.literal(String.valueOf(index + 1));
        int textY = (getHeight() - minecraft.font.lineHeight) / 2 + 1;

        guiGraphics.drawCenteredString(
                minecraft.font,
                text,
                getWidth() / 2,
                textY,
                filled ? RunicGuiTheme.TEXT : RunicGuiTheme.TEXT_DIM
        );
    }

    private static Component shortName(Component name) {
        String value = name.getString();
        if (value.length() <= 3) {
            return Component.literal(value);
        }

        return Component.literal(value.substring(0, 3));
    }
}
