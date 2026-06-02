package net.zic.runic_ascension.content.casting;

public enum RunicFormulaMasteryGrade {
    UNSTABLE(0, 1.00F, 1.00F, 1.00F, 1.00F, 1.00F),
    STABLE(5, 1.06F, 1.04F, 1.03F, 0.96F, 0.92F),
    REFINED(20, 1.14F, 1.10F, 1.07F, 0.90F, 0.78F),
    MASTERED(50, 1.24F, 1.18F, 1.12F, 0.82F, 0.62F),
    PERFECTED(100, 1.36F, 1.28F, 1.18F, 0.72F, 0.45F);

    private final int minimumCasts;
    private final float damageMultiplier;
    private final float durationMultiplier;
    private final float rangeMultiplier;
    private final float qiCostMultiplier;
    private final float backlashMultiplier;

    RunicFormulaMasteryGrade(
            int minimumCasts,
            float damageMultiplier,
            float durationMultiplier,
            float rangeMultiplier,
            float qiCostMultiplier,
            float backlashMultiplier
    ) {
        this.minimumCasts = minimumCasts;
        this.damageMultiplier = damageMultiplier;
        this.durationMultiplier = durationMultiplier;
        this.rangeMultiplier = rangeMultiplier;
        this.qiCostMultiplier = qiCostMultiplier;
        this.backlashMultiplier = backlashMultiplier;
    }

    public int getMinimumCasts() {
        return minimumCasts;
    }

    public RunicFormulaStats applyTo(RunicFormulaStats stats) {
        return new RunicFormulaStats(
                stats.damageMultiplier() * damageMultiplier,
                stats.durationMultiplier() * durationMultiplier,
                stats.rangeMultiplier() * rangeMultiplier,
                stats.qiCostMultiplier() * qiCostMultiplier,
                stats.backlashMultiplier() * backlashMultiplier
        );
    }

    public static RunicFormulaMasteryGrade fromCastCount(int castCount) {
        RunicFormulaMasteryGrade grade = UNSTABLE;

        for (RunicFormulaMasteryGrade candidate : values()) {
            if (castCount >= candidate.minimumCasts) {
                grade = candidate;
            }
        }

        return grade;
    }
}
