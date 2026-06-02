package net.zic.runic_ascension.content.casting;

public final class RunicFormulaGrammar {

    private RunicFormulaGrammar() {
    }

    public static RunicCastingResult validate(RunicFormula formula, int runicRealm, int availableSlots) {
        if (formula == null || formula.inputRunes().isEmpty()) {
            return RunicCastingResult.failure("empty_sequence");
        }

        if (formula.inputRunes().size() > availableSlots) {
            return RunicCastingResult.failure("formula_too_complex");
        }

        if (!formula.isValid()) {
            return RunicCastingResult.failure("missing_core_runes");
        }

        int maxSources = Math.min(4, 1 + Math.max(0, runicRealm) / 3);
        int maxIntents = Math.min(4, 1 + Math.max(0, runicRealm) / 3);
        int maxForms = Math.min(3, 1 + Math.max(0, runicRealm) / 4);
        int maxModifiers = Math.min(5, 1 + Math.max(0, runicRealm) / 2);

        if (formula.sourceCount() > maxSources) {
            return RunicCastingResult.failure("too_many_sources");
        }

        if (formula.intentCount() > maxIntents) {
            return RunicCastingResult.failure("too_many_intents");
        }

        if (formula.formCount() > maxForms) {
            return RunicCastingResult.failure("too_many_forms");
        }

        if (formula.modifierCount() > maxModifiers) {
            return RunicCastingResult.failure("unstable_modifiers");
        }

        if (formula.startsWithModifier() && runicRealm < 2) {
            return RunicCastingResult.failure("unstable_opening_modifier");
        }

        int safeWeight = availableSlots + 2 + Math.max(0, runicRealm / 2);

        if (formula.grammaticalWeight() > safeWeight * 2) {
            return RunicCastingResult.failure("formula_too_complex");
        }

        return RunicCastingResult.success(formula.getFormulaId());
    }
}
