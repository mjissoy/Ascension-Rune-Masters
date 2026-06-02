package net.zic.runic_ascension.content.casting;

import net.minecraft.resources.ResourceLocation;
import net.zic.runic_ascension.content.runes.IRunicRune;
import net.zic.runic_ascension.content.runes.ModRunicRunes;
import net.zic.runic_ascension.content.runes.RunicRuneType;

import java.util.ArrayList;
import java.util.List;

public final class RunicFormulaParser {

    private RunicFormulaParser() {
    }

    public static RunicFormula parse(List<ResourceLocation> inputRunes) {
        List<IRunicRune> sources = new ArrayList<>();
        List<IRunicRune> intents = new ArrayList<>();
        List<IRunicRune> forms = new ArrayList<>();
        List<IRunicRune> modifiers = new ArrayList<>();

        if (inputRunes == null) {
            return new RunicFormula(sources, intents, forms, modifiers, List.of());
        }

        for (ResourceLocation runeId : inputRunes) {
            IRunicRune rune = ModRunicRunes.get(runeId);

            if (rune == null) {
                continue;
            }

            if (rune.getType() == RunicRuneType.SOURCE) {
                sources.add(rune);
            } else if (rune.getType() == RunicRuneType.INTENT) {
                intents.add(rune);
            } else if (rune.getType() == RunicRuneType.FORM) {
                forms.add(rune);
            } else if (rune.getType() == RunicRuneType.MODIFIER) {
                modifiers.add(rune);
            }
        }

        return new RunicFormula(sources, intents, forms, modifiers, List.copyOf(inputRunes));
    }
}
