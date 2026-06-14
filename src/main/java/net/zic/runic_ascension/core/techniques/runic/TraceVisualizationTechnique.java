package net.zic.runic_ascension.core.techniques.runic;

import net.zic.runic_ascension.core.skills.RunicSkills;
import net.zic.runic_ascension.core.techniques.AbstractRunicTechnique;
import net.zic.runic_ascension.core.techniques.RunicStatHandlers;

import java.util.Set;

public class TraceVisualizationTechnique extends AbstractRunicTechnique {

    public TraceVisualizationTechnique() {
        super(
                "runic_ascension.technique.trace_visualization_method",
                4.75D,
                RunicStatHandlers.TRACE_VISUALIZATION_HANDLER,
                Set.of(
                        RunicSkills.TRACE_STEP.getId()
                )
        );
    }
}