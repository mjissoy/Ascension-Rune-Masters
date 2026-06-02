# Runic Rune Effect Reference

This file documents the current **Runic Formula Profile MVP** for Ascension: Rune Masters.

The profile system lives mainly in:

- `content.casting.RunicEffectProfile`
- `content.casting.RunicEffectArchetype`
- `content.casting.RunicFormulaInterpreter`
- `content.casting.RunicFormulaScaling`
- `content.casting.RunicFormulaCaster`

The goal of this system is to make runes behave like words in a symbolic language rather than fixed spell IDs.

## Formula Layers

A flexible formula is interpreted in four layers:

1. **Source Runes** decide the nature of the power.
2. **Intent Runes** decide what the power is trying to do.
3. **Form Runes** decide how the power is delivered.
4. **Modifier Runes** alter speed, weight, stability, backlash, stealth, and violence.

`RunicFormulaStats` answers: how strong, long, costly, stable, and risky is the formula?

`RunicEffectProfile` answers: what kind of effect is this formula trying to become?

## Effect Archetypes

Current archetypes:

| Archetype | Meaning |
|---|---|
| `PROJECTILE` | Single-target or aimed effect, usually from `Bolt`. |
| `LINE` | Forward line-shaped attack/effect. |
| `AREA` | Area effect around the caster or selected zone, currently used by `Circle`, `Pulse`, and `Sphere`. |
| `WALL` | Defensive or barrier-like formula. |
| `SELF` | Self-buff, veil, movement, healing, or internal effect. |
| `MARK` | Look-target formula with a marking/reveal behaviour. |
| `UNKNOWN` | Invalid or unformed formula. |

## Source Runes

Source runes define elemental or conceptual nature.

| Rune | Current Profile Effects | Current Side Effects |
|---|---|---|
| `flame` | More damage, fire damage kind, higher qi cost, lower stability, more backlash. | Adds ignition/fire flags. Targets may be ignited for extra seconds. Self effects may grant fire resistance. |
| `water` | Slightly less damage, more healing, longer duration, more stability. | Extinguish/cleansing flags. Can clear fire and support regeneration. |
| `wind` | More range, more area, lower qi cost, less impact damage, more knockback and mobility. | Knockback/swift flags. Can push targets and grant speed-like self effects. |
| `earth` | Longer duration, lower range, more stability, lower backlash. | Grounded/barrier flags. Helps defensive and resistance-style effects. |
| `wood` | More healing, longer duration, more stability. | Regenerative/rooting flags. Good for sustain, healing, and growth-flavoured effects. |
| `metal` | More damage, longer duration, slightly lower range, more stability. | Hardened and armor-piercing flags. Good for tough, sharp, or reinforced effects. |
| `lightning` | Much more damage and range, higher qi cost, lower stability, higher backlash. | Shock/weakening flags. Can apply weakness-style effects. |
| `frost` | Slightly more damage, longer duration, more stability. | Chill/slow flags. Can slow targets. |
| `light` | Slightly more damage, more healing, slightly higher cost. | Reveal/cleansing flags. Can make targets glow and help support/cleanse effects. |
| `shadow` | Slightly more damage/range/duration, lower stability, more backlash. | Blind/stealth flags. Can blind targets or help stealth effects. |
| `life` | Much more healing, much less damage, higher qi cost, slightly more stability. | Healing/regenerative/cleansing flags. Best support source. |
| `decay` | More damage, much less healing, higher qi cost, lower stability, higher backlash. | Wither/corruptive flags. Can apply withering effects. |

### Secondary Sources

Extra source runes after the first act as smaller blended clauses.

Examples:

- Secondary `flame` adds a small damage and ignition push.
- Secondary `wind` adds extra range and knockback.
- Secondary `earth` adds stability and duration.
- Secondary `life` adds healing but slightly increases cost.
- Secondary `decay` adds damage and risk.

These are intentionally weaker than primary sources so formulas can blend concepts without every mixed-source formula becoming absurd.

## Intent Runes

Intent runes define the action of the formula.

| Rune | Current Profile Effects | Current Side Effects |
|---|---|---|
| `bind` | Lower damage, longer duration, slightly more stability. | Restraint/slow flags. Good for control and fields. |
| `push` | Lower damage, more range, much stronger knockback. | Knockback flag. Pushes targets away. |
| `pull` | Lower damage, slightly more range, moderate force. | Pull flag. Pulls targets toward caster. |
| `guard` | Much lower damage, longer duration, more stability, lower backlash. | Defensive flag. Good for barriers and protection. |
| `cut` | More damage, slightly more cost. | Slash flag. Basic offensive shaping. |
| `heal` | Much lower damage, much more healing, higher qi cost, slightly more stability. | Healing/support flags. Main restorative intent. |
| `gather` | Lower damage, more healing, longer duration, lower qi cost, more stability. | Gathering/sustain flags. Useful for steady self-support and efficiency. |
| `release` | More damage, more range, higher cost, less stability. | Burst flag. Good for explosive or outward effects. |
| `pierce` | Much more damage, more range, higher cost, slightly lower stability. | Armor-piercing flag. Good for narrow attacks. |
| `compress` | More damage, longer duration, smaller area, higher cost, slightly lower stability. | Pressure/slow flags. Good for crushing or slowing effects. |

### Secondary Intents

Extra intent runes after the first add layered behaviour.

Examples:

- Secondary `guard` adds stability and lowers damage a little.
- Secondary `heal` increases healing and lowers damage a little.
- Secondary `pierce` adds damage and armor-piercing behaviour.
- Secondary `gather` improves cost and stability.
- Secondary `release` adds damage/range but reduces stability.

## Form Runes

Form runes define delivery shape.

| Rune | Archetype | Current Profile Effects |
|---|---|---|
| `bolt` | `PROJECTILE` | More damage and range, smaller area, single-target flag. |
| `line` | `LINE` | More range, slight damage increase, line flag. |
| `circle` | `AREA` | Larger area, lower damage, higher qi cost, area flag. |
| `pulse` | `AREA` | Larger area, slightly lower range, higher qi cost, burst flag. |
| `sphere` | `AREA` | Much larger area, lower damage, higher cost, slightly lower stability. |
| `wall` | `WALL` | Lower damage, longer duration, more stability, defensive/barrier flags. |
| `veil` | `SELF` | Much lower damage, more healing, longer duration, more stability, self-buff flag. |
| `mark` | `MARK` | Lower damage, longer duration, lower qi cost, mark flag. |

### Secondary Forms

Extra form runes after the first are smaller shaping clauses.

Examples:

- Secondary `circle`, `pulse`, or `sphere` expands area but increases cost.
- Secondary `wall` adds duration and stability.
- Secondary `line` adds range.
- Secondary `mark` adds duration and slightly lowers cost.

## Modifier Runes

Modifier runes bend the formula. They are powerful, but many of them destabilise the result.

| Rune | Current Profile Effects | Current Side Effects |
|---|---|---|
| `quicken` | Shorter duration, more range, higher cost, lower stability, more mobility. | Fast flag. Good for quick attacks or movement formulae. |
| `stabilise` | Lower damage, slightly lower cost, much more stability, much lower backlash. | Stable flag. Good for safe casting and controlled effects. |
| `heavy` | More damage, longer duration, lower range, higher cost, lower stability, more knockback. | Weight flag. Good for dense, crushing, defensive, or heavy projectile formulae. |
| `violent` | Much more damage, much higher cost, much lower stability, much higher backlash. | Violent flag. Good for risky high-output attacks. |
| `hidden` | Lower damage, slightly more range/duration, higher cost, lower stability. | Stealth/obscured flags. Good for shadow, mark, veil, and surprise effects. |

## Current Cast-Time Side Effects

The profile is currently used in two main places:

1. **Scaling** via `RunicFormulaScaling`.
2. **Side effects and delivery choice** via `RunicFormulaCaster`.

Current profile flags may cause:

- ignition
- fire clearing
- regeneration
- movement speed
- damage resistance
- invisibility
- slowness
- weakness
- blindness
- wither
- glowing/reveal
- knockback

## Current GUI Preview

The casting GUI now shows a small profile preview line:

```text
Profile: Flame Cut Line | Line | fire, ignition, slash, line
```

This preview is intentionally simple. It does not yet show exact qi cost, stability, damage, or backlash because those should eventually come from a synced server-side evaluation or a carefully mirrored client estimate.

## Design Notes

- Primary runes matter most.
- Secondary runes add weaker blended clauses.
- Modifiers should feel tempting but dangerous.
- Stability and backlash should matter enough that `Stabilise` is useful.
- `Violent` should be powerful but risky.
- `Earth`, `Water`, `Wood`, `Light`, and `Stabilise` should make safer formulae.
- `Flame`, `Lightning`, `Shadow`, `Decay`, and `Violent` should push higher output or stranger effects at higher risk.
- `Wind` should be the main range, speed, knockback, and mobility source.
- `Life` should be the strongest healing source, but expensive and weak offensively.

## Future Expansion Ideas

Possible later additions:

- Add a real `areaMultiplier` field to `RunicFormulaStats` instead of keeping area only in the profile.
- Add a real `healingMultiplier` to `RunicFormulaStats` if healing becomes common enough.
- Add profile-driven particles for mixed-source formulas.
- Add profile-driven damage types once Ascension supports richer damage categories.
- Let brushes modify the profile before final scaling.
- Let physiques/bloodlines add or remove profile flags.
- Let formula mastery improve stability, cost, and suppression efficiency for specific profiles.
- Show server-estimated cost/stability/risk in the GUI.
