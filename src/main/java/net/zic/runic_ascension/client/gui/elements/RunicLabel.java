package net.zic.runic_ascension.client.gui.elements;

import net.lucent.easygui.gui.UIFrame;
import net.lucent.easygui.gui.elements.built_in.EasyLabel;
import net.minecraft.network.chat.Component;

public class RunicLabel extends EasyLabel {
    public RunicLabel(UIFrame frame, Component text, int x, int y, int width, int height, int color) {
        super(frame);
        setText(text);
        setTextColor(color);
        setWidth(width);
        setHeight(height);
        getPositioning().setX(x);
        getPositioning().setY(y);
        setScaleToFit(true);
        setTextPositioningY(TextPositionRule.CENTER);
    }

    public RunicLabel centered() {
        setTextPositioningX(TextPositionRule.CENTER);
        return this;
    }

    public RunicLabel scaled(float scale) {
        setTextScale(scale);
        return this;
    }
}
