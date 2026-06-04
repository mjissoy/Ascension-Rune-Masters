package net.zic.runic_ascension.client.gui.elements;

import net.lucent.easygui.gui.RenderableElement;
import net.lucent.easygui.gui.UIFrame;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public class RunicTextBlockElement extends RenderableElement {
    private Component text = Component.empty();
    private int textColor;
    private float textScale;
    private boolean centered;

    public RunicTextBlockElement(UIFrame frame, Component text, int x, int y, int width, int height, int textColor, float textScale, boolean centered) {
        super(frame, x, y);
        this.text = text == null ? Component.empty() : text;
        this.textColor = textColor;
        this.textScale = Math.max(0.1F, textScale);
        this.centered = centered;
        setWidth(width);
        setHeight(height);
    }

    public void setText(Component text) {
        this.text = text == null ? Component.empty() : text;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public void setTextScale(float textScale) {
        this.textScale = Math.max(0.1F, textScale);
    }

    public void setCentered(boolean centered) {
        this.centered = centered;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (text.getString().isBlank()) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        int wrapWidth = Math.max(1, Math.round(getWidth() / textScale));
        List<FormattedCharSequence> lines = minecraft.font.split(text, wrapWidth);
        int scaledLineHeight = Math.max(1, Math.round((minecraft.font.lineHeight + 2) * textScale));
        int maxLines = Math.max(1, getHeight() / scaledLineHeight);
        int unscaledWidth = Math.round(getWidth() / textScale);

        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(textScale, textScale, 1.0F);

        int rendered = Math.min(maxLines, lines.size());
        for (int i = 0; i < rendered; i++) {
            FormattedCharSequence line = lines.get(i);
            int drawX = centered ? Math.max(0, (unscaledWidth - minecraft.font.width(line)) / 2) : 0;
            int drawY = Math.round((i * scaledLineHeight) / textScale);
            guiGraphics.drawString(minecraft.font, line, drawX, drawY, textColor, false);
        }

        guiGraphics.pose().popPose();
    }
}
