package net.zic.runic_ascension.content.casting;

import net.minecraft.resources.ResourceLocation;
import net.zic.runic_ascension.RunicAscension;
import net.zic.runic_ascension.content.runes.IRunicRune;

import java.util.ArrayList;
import java.util.List;

public record RunicFormula(
        List<IRunicRune> sources,
        List<IRunicRune> intents,
        List<IRunicRune> forms,
        List<IRunicRune> modifiers,
        List<ResourceLocation> inputRunes
) {
    public RunicFormula {
        sources = sources == null ? List.of() : List.copyOf(sources);
        intents = intents == null ? List.of() : List.copyOf(intents);
        forms = forms == null ? List.of() : List.copyOf(forms);
        modifiers = modifiers == null ? List.of() : List.copyOf(modifiers);
        inputRunes = inputRunes == null ? List.of() : List.copyOf(inputRunes);
    }

    /**
     * The first rune of each category remains the primary grammatical role.
     * Additional same-category runes are secondary clauses/amplifiers that affect
     * scaling, stability, cost, and future advanced effects.
     */
    public IRunicRune source() {
        return sources.isEmpty() ? null : sources.get(0);
    }

    public IRunicRune intent() {
        return intents.isEmpty() ? null : intents.get(0);
    }

    public IRunicRune form() {
        return forms.isEmpty() ? null : forms.get(0);
    }

    public boolean isValid() {
        return source() != null && intent() != null;
    }

    public int sourceCount() {
        return sources.size();
    }

    public int intentCount() {
        return intents.size();
    }

    public int formCount() {
        return forms.size();
    }

    public int modifierCount() {
        return modifiers.size();
    }

    public int grammaticalWeight() {
        return sourceCount() * 2 + intentCount() * 2 + formCount() + modifierCount();
    }

    public String sourcePath() {
        return source() == null ? "" : source().getId().getPath();
    }

    public String intentPath() {
        return intent() == null ? "" : intent().getId().getPath();
    }

    public String formPath() {
        return form() == null ? "bolt" : form().getId().getPath();
    }

    public boolean hasSource(String path) {
        return hasRunePath(sources, path);
    }

    public boolean hasIntent(String path) {
        return hasRunePath(intents, path);
    }

    public boolean hasForm(String path) {
        return hasRunePath(forms, path);
    }

    public boolean hasModifier(String path) {
        return hasRunePath(modifiers, path);
    }

    public List<String> sourcePaths() {
        return pathsOf(sources);
    }

    public List<String> intentPaths() {
        return pathsOf(intents);
    }

    public List<String> formPaths() {
        return pathsOf(forms);
    }

    public List<String> modifierPaths() {
        return pathsOf(modifiers);
    }

    public boolean startsWithModifier() {
        if (inputRunes.isEmpty()) {
            return false;
        }

        ResourceLocation first = inputRunes.get(0);

        for (IRunicRune modifier : modifiers) {
            if (modifier.getId().equals(first)) {
                return true;
            }
        }

        return false;
    }

    private static boolean hasRunePath(List<IRunicRune> runes, String path) {
        for (IRunicRune rune : runes) {
            if (rune.getId().getPath().equals(path)) {
                return true;
            }
        }

        return false;
    }

    private static List<String> pathsOf(List<IRunicRune> runes) {
        List<String> paths = new ArrayList<>();

        for (IRunicRune rune : runes) {
            paths.add(rune.getId().getPath());
        }

        return List.copyOf(paths);
    }

    public ResourceLocation getFormulaId() {
        String canonical = inputRunes.stream()
                .map(ResourceLocation::toString)
                .reduce((left, right) -> left + "|" + right)
                .orElse("empty");

        String hash = Integer.toUnsignedString(canonical.hashCode(), 16);
        String readablePrefix = sanitizeFormulaPathPart(sourcePath()) + "_" + sanitizeFormulaPathPart(intentPath());

        return ResourceLocation.fromNamespaceAndPath(
                RunicAscension.MOD_ID,
                "formula/" + readablePrefix + "_" + hash
        );
    }

    private static String sanitizeFormulaPathPart(String value) {
        if (value == null || value.isBlank()) {
            return "unknown";
        }

        return value.toLowerCase().replaceAll("[^a-z0-9_/.-]", "_");
    }
}
