# Runic Scraps / Tutorial Notes Reference

This document tracks the first tutorial-note MVP for **Ascension: Rune Masters**.

Runic scraps are small readable items. They do **not** teach runes directly. Instead, they teach the grammar of runic formulae through short in-world lessons.

The current implementation uses `RunicScrapItem`:

- registered in `RunicItems`
- stack size: 16
- hover text: short hint + right-click instruction
- right-click: prints the scrap title and lore lines to chat
- item model: shared `runic_scrap` texture
- lang entries: `runic_ascension.runic.scrap.<scrap_key>.*`

## Current Scraps

### Runic Scrap: Origin

Purpose: teaches that source runes are the starting breath of a formula.

Key lesson:

- Source/origin runes provide the core power.
- Examples include Flame, Wind, Earth, Life, and similar source runes.
- A source rune alone is incomplete.
- A formula needs intent to move.

### Runic Scrap: Intent

Purpose: teaches that intent runes tell the source what to do.

Key lesson:

- Intent is the will or desire of a formula.
- Cut, Guard, Bind, and Heal are examples of intent behaviour.
- Source without intent is dormant power.
- Intent gives the formula direction.

### Runic Scrap: Forms

Purpose: teaches that form runes shape delivery.

Key lesson:

- Form gives a formula a vessel or shape.
- Bolt, Line, Wall, and Circle alter how the effect manifests.
- The same source and intent can feel very different with a different form.

### Runic Scrap: Modifiers

Purpose: teaches that modifier runes refine or destabilise formulae.

Key lesson:

- Modifiers are lesser strokes, but still powerful.
- Quicken, Stabilise, Heavy, Violent, and Hidden alter cost, speed, power, stability, and side behaviour.
- Opening a formula with a modifier is unstable.
- Root runes should usually come before refinement runes.

### Runic Scrap: Instability

Purpose: warns players about backlash.

Key lesson:

- Some formulae can cast while unstable.
- Violent, haste, depth, overreach, and bad ordering can increase backlash risk.
- Stabilising effects reduce risk but may soften the result.

### Runic Scrap: Suppression

Purpose: explains suppression after the suppression MVP.

Key lesson:

- High-realm formulae can be deliberately cast at lower effective realm.
- Suppression lowers qi cost and reduces scale.
- Suppression is useful for controlled, cheap versions of familiar formulae.
- Mastery may later improve suppression efficiency.

### Runic Scrap: First Formula

Purpose: hints at a beginner offensive formula without making the system feel like a recipe book.

Key lesson:

- Flame + Cut + Bolt creates a simple fire attack.
- Replacing Bolt with Line changes the shape.
- Adding Violent increases power but lowers stability.
- The example is a door, not a full answer key.

### Runic Scrap: Free Casting

Purpose: introduces the difference between memorised sequences and flexible formulae.

Key lesson:

- Recorded sequences are safe, known roads.
- Flexible formulae are written by the player from known grammar.
- Once players know enough runes, the Codex should become a map rather than a cage.

## Future Tome Direction

The current old tome structure still teaches broad rune groups. A future upgrade could turn many tomes into partial-sequence tomes.

Example long-term idea:

- A high-tier lightning sequence might use: Lightning, Cut, Pierce, Pulse, Bolt, Violent.
- One tome variant might teach Lightning + Pulse.
- Another copy of the same thematic tome might teach Bolt + Cut.
- Another might repeat Lightning, which teaches nothing new, plus Violent.

This would make early rune acquisition feel like reconstructing ancient formula fragments rather than simply unlocking categories.

Suggested future classes/systems:

- `RunicFormulaFragmentTomeItem`
- weighted rune fragment pools
- loot-table driven variant selection
- optional display of hinted formula family
- no guaranteed full sequence from a single tome unless explicitly intended

## Design Notes

Runic scraps should teach patterns, not spoil the entire system.

Good scraps say things like:

- Origins wake power.
- Intent gives desire.
- Forms give body.
- Modifiers refine or destabilise.

Avoid scraps that simply say:

- Press these exact runes to win.
- This is the best formula.
- Always use this modifier.

The goal is to make the player feel like they are studying a living symbolic language.
