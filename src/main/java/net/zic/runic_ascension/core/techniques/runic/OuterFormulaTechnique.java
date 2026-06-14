package net.zic.runic_ascension.core.techniques.runic;

import net.zic.runic_ascension.core.skills.RunicSkills;
import net.zic.runic_ascension.core.techniques.AbstractRunicTechnique;
import net.zic.runic_ascension.core.techniques.RunicStatHandlers;

import java.util.Set;

public class OuterFormulaTechnique extends AbstractRunicTechnique {

    public OuterFormulaTechnique() {
        super(
                "runic_ascension.technique.outer_formula_method",
                4.5D,
                RunicStatHandlers.OUTER_FORMULA_HANDLER,
                Set.of(
                        RunicSkills.SCRIPT_SEVER.getId(),
                        RunicSkills.CINDER_BRAND.getId()
                )
        );
    }
}