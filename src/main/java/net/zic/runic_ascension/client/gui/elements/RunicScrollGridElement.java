package net.zic.runic_ascension.client.gui.elements;

import net.lucent.easygui.gui.RenderableElement;
import net.lucent.easygui.gui.UIFrame;
import net.minecraft.client.gui.GuiGraphics;
import net.thejadeproject.ascension.refactor_packages.gui.elements.general.ScrollBox;

/**
 * Scrollable grid container for rune palettes, memorised formula lists, gesture palettes, and later paradigms.
 */
public class RunicScrollGridElement extends ScrollBox {
    private final int buttonWidth;
    private final int buttonHeight;
    private final int gap;
    private final int columns;
    private final int rowHeight;

    public RunicScrollGridElement(UIFrame frame, int x, int y, int width, int height, int buttonWidth, int buttonHeight, int gap, int columns) {
        super(frame, Math.max(1, buttonHeight + gap));
        this.buttonWidth = Math.max(1, buttonWidth);
        this.buttonHeight = Math.max(1, buttonHeight);
        this.gap = Math.max(0, gap);
        this.columns = Math.max(1, columns);
        this.rowHeight = this.buttonHeight + this.gap;

        setWidth(width);
        setHeight(height);
        getPositioning().setX(x);
        getPositioning().setY(y);
        useCustomChildAdditionLogic = true;
    }

    @Override
    public void addChild(RenderableElement element) {
        super.addChild(element);
        updateVisibility(element);
    }

    @Override
    public void updatePos(RenderableElement element) {
        int index = getChildren().indexOf(element);
        if (index < 0) {
            index = getChildren().size();
        }
        int col = index % columns;
        int row = index / columns;

        element.getPositioning().setFromRawX(col * (buttonWidth + gap));
        element.getPositioning().setFromRawY(row * rowHeight);
    }

    @Override
    public int getMaxYScroll() {
        int rows = Math.ceilDiv(getChildren().size(), columns);
        return Math.max(0, rows * rowHeight - getHeight());
    }

    @Override
    public void updateVisibility(RenderableElement element) {
        boolean visible = element.getPositioning().getY() + element.getHeight() > 0
                && element.getPositioning().getY() < getHeight();
        element.setActive(visible);
        element.setVisible(visible);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.fill(0, 0, getWidth(), getHeight(), RunicGuiTheme.PANEL_SOFT);
        guiGraphics.renderOutline(0, 0, getWidth(), getHeight(), RunicGuiTheme.BORDER_DARK);
    }
}
