package net.zic.runic_ascension.core.techniques.runic;

import net.zic.runic_ascension.core.techniques.AbstractRunicTechnique;
import net.zic.runic_ascension.core.techniques.RunicStatHandlers;

import java.util.Set;

public class BasicRunicTechnique extends AbstractRunicTechnique {

    public BasicRunicTechnique() {
        super(
                "runic_ascension.technique.runic_apprentice",
                5.0D,
                RunicStatHandlers.BASIC_RUNIC_HANDLER,
                Set.of()
        );
    }

}