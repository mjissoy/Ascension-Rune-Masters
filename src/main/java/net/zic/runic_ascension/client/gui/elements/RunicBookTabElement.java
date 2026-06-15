package net.zic.runic_ascension.client.gui.elements;

import net.lucent.easygui.gui.UIFrame;
import net.lucent.easygui.gui.elements.built_in.EasyButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.function.BooleanSupplier;

/**
 * A side tab styled like a small page marker for book-like screens such as the Runic Codex.
 */
public class RunicBookTabElement extends EasyButton {
    private final Component label;
    private final Runnable clickAction;
    private final BooleanSupplier selectedSupplier;

    public RunicBookTabElement(
            UIFrame frame,
            int x,
            int y,
            int width,
            int height,
            Component label,
            BooleanSupplier selectedSupplier,
            Runnable clickAction
    ) {
        super(frame, x, y);
        this.label = label == null ? Component.empty() : label;
        this.selectedSupplier = selectedSupplier == null ? () -> false : selectedSupplier;
        this.clickAction = clickAction == null ? () -> { } : clickAction;
        setWidth(width);
        setHeight(height);
    }

    @Override
    public void onClick() {
        clickAction.run();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        boolean selected = selectedSupplier.getAsBoolean();
        boolean hovered = isPointBounded(mouseX, mouseY);

        int fill = selected ? RunicGuiTheme.PARCHMENT : hovered ? RunicGuiTheme.PARCHMENT_SOFT : RunicGuiTheme.CODEX_BINDING_SOFT;
        int outline = selected ? RunicGuiTheme.PARCHMENT_EDGE : hovered ? RunicGuiTheme.BORDER_MUTED : RunicGuiTheme.BORDER_DARK;
        int textColor = selected ? RunicGuiTheme.CODEX_INK : RunicGuiTheme.TEXT_MUTED;

        guiGraphics.fill(0, 0, getWidth(), getHeight(), RunicGuiTheme.CODEX_BINDING);
        guiGraphics.fill(2, 1, getWidth(), getHeight() - 1, fill);
        guiGraphics.renderOutline(1, 0, getWidth() - 1, getHeight(), outline);

        Minecraft minecraft = Minecraft.getInstance();
        int textY = (getHeight() - minecraft.font.lineHeight) / 2 + 1;
        guiGraphics.drawCenteredString(minecraft.font, label, getWidth() / 2 + 1, textY, textColor);
    }
}
