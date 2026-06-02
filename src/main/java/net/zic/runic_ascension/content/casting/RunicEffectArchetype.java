package net.zic.runic_ascension.content.casting;

/**
 * Broad delivery categories used by the flexible formula interpreter.
 *
 * <p>Forms still decide the main shape of a formula, but the archetype gives
 * the caster and GUI a stable, readable classification to work with.</p>
 */
public enum RunicEffectArchetype {
    PROJECTILE,
    LINE,
    AREA,
    WALL,
    SELF,
    MARK,
    UNKNOWN
}
