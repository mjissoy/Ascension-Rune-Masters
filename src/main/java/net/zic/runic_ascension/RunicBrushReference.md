# Runic Brush Reference

Runic brushes are the first equipment layer for Rune Masters. A brush is held while opening and using the Runic Casting GUI. For the MVP, brushes do two things:

1. Add a small number of rune slots while held.
2. Bend flexible formula scaling toward a theme.

Hardcoded sequences still mostly behave as before, but the extra rune slot from a held brush applies to the casting slot check. The deeper brush effects are applied to flexible/free-cast formulae through `RunicCastingContext` and `RunicFormulaScaling`.

---

## Current Brush Families

### Basic Brush

**Item:** `basic_runic_brush`  
**Theme:** beginner stability  
**Affinity:** Stabilise  
**Slots:** +1

Effects:

- Slightly lowers qi cost.
- Slightly lowers backlash.
- Slightly improves stability.
- Intended as the neutral starter brush.

### Earth Brush

**Item:** `earth_runic_brush`  
**Theme:** grounding, endurance, defensive scripts  
**Affinity:** Earth  
**Slots:** +1

Effects:

- Increases duration.
- Strongly improves stability.
- Reduces backlash.
- Slightly lowers range and power.
- Extra benefit for defensive/barrier formula profiles.

Best used for:

- Wall formulae.
- Guard formulae.
- Stabilised defensive scripts.
- Longer-lasting controlled effects.

### Heaven Brush

**Item:** `heaven_runic_brush`  
**Theme:** precision, clarity, lower waste  
**Affinity:** Light  
**Slots:** +1

Effects:

- Lowers qi cost.
- Improves range.
- Improves stability slightly.
- Slightly improves duration and damage.
- Extra benefit for healing-focused formula profiles.

Best used for:

- Healing formulae.
- Support formulae.
- Precise ranged scripts.
- Clean utility scripts.

### Hell Brush

**Item:** `hell_runic_brush`  
**Theme:** force, hunger, violent inscriptions  
**Affinity:** Violent  
**Slots:** +1

Effects:

- Strongly improves power.
- Slightly increases qi cost.
- Greatly increases backlash risk.
- Reduces stability.
- Extra power/risk effect for projectile, line, and area formula profiles.

Best used for:

- Damage formulae.
- Violent modifiers.
- High-risk burst casting.
- Overwhelming offensive scripts.

---

## Implementation Notes

Brush state enters casting through:

- `RunicBrushType`
- `RunicBrushItem`
- `RunicPathHelper.getHeldRunicBrushType(...)`
- `RunicCastingContext.brushType()`
- `RunicFormulaScaling.applyBrush(...)`

The Casting GUI preview reads the current local player's held brush through the same formula estimate path. When a formula is selected, higher insight tiers can display the active brush beside the formula risk.

---

## Future Expansion Ideas

Possible later additions:

- Brush grades: cracked, mortal, spirit, earth, heaven, immortal.
- Durability or ink consumption.
- Brush materials that affect specific rune families.
- Datapack-defined brush profiles.
- Brush inscriptions and evolutions.
- Heaven/Hell/Earth brush progression lines with three tiers each.
- Blood-ink brushes that trade health for missing self/body runes.
- Soulhair brushes that improve soul formulae.
- Brush compatibility with talismans and permanent inscriptions.

For now, the goal is simply to prove that held tools can shape formula identity without hardcoding every spell.
