package net.zic.runic_ascension.client.gui.elements;

public final class RunicGuiTheme {
    public static final int BACKDROP = 0xE8090410;
    public static final int PANEL = 0xE60D0718;
    public static final int PANEL_SOFT = 0x9912091F;
    public static final int PANEL_DEEP = 0xCC08040F;
    public static final int PANEL_GLOW = 0x663B1E63;

    public static final int BORDER = 0xFFE8D8FF;
    public static final int BORDER_MUTED = 0xAA7A5ACF;
    public static final int BORDER_DARK = 0x884F3A7A;
    public static final int ACCENT = 0xFF8A63FF;
    public static final int ACCENT_SOFT = 0xCC6F4DBA;
    public static final int ACCENT_DARK = 0xAA211733;
    public static final int ACCENT_HOVER = 0xAA4F3A7A;

    public static final int TEXT = 0xFFFFFFFF;
    public static final int TEXT_TITLE = 0xFFE8D8FF;
    public static final int TEXT_MUTED = 0xFFBEB4D7;
    public static final int TEXT_DIM = 0xFF9D8BC7;
    public static final int TEXT_DARK = 0xFF1A0E21;

    private RunicGuiTheme() {
    }

    public static String formatEnumName(String name) {
        if (name == null || name.isBlank()) {
            return "";
        }

        String lower = name.toLowerCase().replace('_', ' ');
        return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
    }
}
