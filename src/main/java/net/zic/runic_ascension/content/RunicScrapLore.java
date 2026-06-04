package net.zic.runic_ascension.content;

import java.util.List;

public final class RunicScrapLore {
    public static final List<Entry> ENTRIES = List.of(
            new Entry("origin", 4),
            new Entry("intent", 4),
            new Entry("forms", 4),
            new Entry("modifiers", 4),
            new Entry("instability", 4),
            new Entry("suppression", 4),
            new Entry("first_formula", 5),
            new Entry("free_casting", 4)
    );

    private RunicScrapLore() {
    }

    public static Entry get(String key) {
        if (key == null) {
            return null;
        }

        for (Entry entry : ENTRIES) {
            if (entry.key().equals(key)) {
                return entry;
            }
        }

        return null;
    }

    public static String base(String key) {
        return "runic_ascension.runic.scrap." + key;
    }

    public record Entry(String key, int lineCount) {
        public String translationBase() {
            return base(key);
        }
    }
}
