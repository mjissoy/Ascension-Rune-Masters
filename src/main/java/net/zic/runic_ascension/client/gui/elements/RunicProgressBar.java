package net.zic.runic_ascension.client.gui.elements;

import net.lucent.easygui.gui.RenderableElement;
import net.lucent.easygui.gui.UIFrame;
import net.minecraft.client.gui.GuiGraphics;

import java.util.function.DoubleSupplier;

public class RunicProgressBar extends RenderableElement {
    private final DoubleSupplier progressSupplier;

    public RunicProgressBar(UIFrame frame, int x, int y, int width, int height, DoubleSupplier progressSupplier) {
        super(frame, x, y);
        this.progressSupplier = progressSupplier;
        setWidth(width);
        setHeight(height);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        double progress = Math.max(0.0D, Math.min(1.0D, progressSupplier.getAsDouble()));
        int fillWidth = (int) Math.round(getWidth() * progress);

        guiGraphics.fill(0, 0, getWidth(), getHeight(), RunicGuiTheme.PANEL_SOFT);
        guiGraphics.fill(0, 0, fillWidth, getHeight(), RunicGuiTheme.ACCENT_SOFT);
        guiGraphics.renderOutline(0, 0, getWidth(), getHeight(), RunicGuiTheme.BORDER_MUTED);
    }
}
