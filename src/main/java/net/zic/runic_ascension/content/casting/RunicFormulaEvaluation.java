package net.zic.runic_ascension.content.casting;

public record RunicFormulaEvaluation(
        RunicFormula formula,
        RunicCastingContext context,
        RunicFormulaStats stats,
        RunicCastingState state,
        String failureReason,
        double qiCost,
        float stability
) {
    public boolean canCast() {
        return state == RunicCastingState.VALID || state == RunicCastingState.UNSTABLE;
    }

    public boolean shouldBacklashImmediately() {
        return state == RunicCastingState.OVERREACHED;
    }

    public boolean isUnstable() {
        return state == RunicCastingState.UNSTABLE;
    }
}
