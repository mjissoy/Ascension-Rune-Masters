package net.zic.runic_ascension.client.gui.elements;

public final class RunicGuiTheme {
    public static final int BACKDROP = 0xE0080410;
    public static final int PANEL = 0xE60B0616;
    public static final int PANEL_SOFT = 0x8F130923;
    public static final int PANEL_DEEP = 0xD7080410;
    public static final int PANEL_GLOW = 0x553B1E63;

    public static final int BORDER = 0xFFE8D8FF;
    public static final int BORDER_MUTED = 0xAA8B6BE3;
    public static final int BORDER_DARK = 0x77563D8F;
    public static final int ACCENT = 0xFF9A74FF;
    public static final int ACCENT_SOFT = 0xCC7653C7;
    public static final int ACCENT_DARK = 0xAA241637;
    public static final int ACCENT_HOVER = 0xB25D4394;

    public static final int TEXT = 0xFFFFFFFF;
    public static final int TEXT_TITLE = 0xFFF1E8FF;
    public static final int TEXT_MUTED = 0xFFD8CCF0;
    public static final int TEXT_DIM = 0xFFC0AEDF;
    public static final int TEXT_DARK = 0xFF1A0E21;

    public static final int PARCHMENT = 0xFFEAD8B4;
    public static final int PARCHMENT_SOFT = 0xFFF4E6C9;
    public static final int PARCHMENT_SHADOW = 0xFFD2B789;
    public static final int PARCHMENT_EDGE = 0xFF8E6F49;
    public static final int CODEX_BINDING = 0xFF2A173A;
    public static final int CODEX_BINDING_SOFT = 0xFF3C2456;
    public static final int CODEX_INK = 0xFF211227;
    public static final int CODEX_INK_MUTED = 0xFF5B3A64;
    public static final int CODEX_LINE = 0xAA7B5C83;

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
