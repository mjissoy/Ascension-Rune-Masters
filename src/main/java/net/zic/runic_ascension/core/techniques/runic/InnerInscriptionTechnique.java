package net.zic.runic_ascension.core.techniques.runic;

import net.zic.runic_ascension.core.skills.RunicSkills;
import net.zic.runic_ascension.core.techniques.AbstractRunicTechnique;
import net.zic.runic_ascension.core.techniques.RunicStatHandlers;

import java.util.Set;

public class InnerInscriptionTechnique extends AbstractRunicTechnique {

    public InnerInscriptionTechnique() {
        super(
                "runic_ascension.technique.inner_inscription_method",
                4.5D,
                RunicStatHandlers.INNER_INSCRIPTION_HANDLER,
                Set.of(
                        RunicSkills.STONE_WARD.getId(),
                        RunicSkills.WATER_MANTLE.getId()
                )
        );
    }
}