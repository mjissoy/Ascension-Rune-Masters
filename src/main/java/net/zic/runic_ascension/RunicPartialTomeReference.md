# Runic Partial Tome Reference

This file documents the current partial formula tome system for **Ascension: Rune Masters**.

The old category tomes have been removed:

- Base elemental tome
- Deep elemental tome
- Intent tome
- Form tome
- Modifier tome

Only the complete archive tome remains as a broad/testing item:

- `runic_tome`

All normal progression tomes should now be formula-family fragments instead of category unlocks.

---

## Design Goal

Partial formula tomes are meant to teach pieces of real formula traditions rather than handing players an entire rune category.

A player may find several copies of the same formula tome across the world. Each physical copy reveals only a few strokes from that formula family's rune pool.

Example:

```text
Storm-Piercing Script pool:
Lightning, Cut, Pierce, Pulse, Bolt, Violent
```

One copy might reveal:

```text
Lightning, Pulse
```

Another copy might reveal:

```text
Bolt, Cut, Violent
```

Another copy might reveal:

```text
Lightning, Cut
```

If the player already knows a revealed rune, that rune does nothing. Partial tomes are still consumed after being studied because the physical copy only contains a few fragmented strokes.

This creates a treasure-hunt grammar loop:

```text
Find scraps -> learn grammar rules
Find partial tomes -> learn formula fragments
Try formulas -> discover working scripts
Use Runic Sight -> expand beyond copied traditions
Refine formulas -> build personal runic language
```

---

## Current Tome Behaviour

Implemented in:

```text
net.zic.runic_ascension.core.items.RunicTomeItem
```

The item now supports two modes.

### Complete Archive Mode

Used by:

```text
runic_tome
```

Behaviour:

- Teaches every currently registered basic rune.
- Mainly useful for creative testing, dev work, or special teacher rewards.
- If it teaches nothing new, it is not consumed.

### Partial Formula Mode

Used by all formula-family tomes.

Behaviour:

- Contains a fixed rune pool.
- Reveals a random subset when studied.
- Current grant range is usually 2-3 runes.
- Already-known runes do not add new knowledge.
- The tome is consumed after study, even if it reveals only known signs.

This means duplicate finds can still be useful, but are not guaranteed to progress the player.

---

## Current Formula Tome Families

### Fragmented Tome: Ember Bolt

Registry name:

```text
runic_tome_ember_bolt
```

Family key:

```text
ember_bolt
```

Rune pool:

```text
Flame, Cut, Bolt, Line, Violent, Stabilise
```

Intended lessons:

- Flame can become an offensive source.
- Cut gives damaging intent.
- Bolt and Line teach delivery shapes.
- Violent increases power at a stability cost.
- Stabilise teaches control.

Starter formulas suggested by this family:

```text
Flame > Cut > Bolt
Flame > Cut > Line
Flame > Cut > Bolt > Violent
Flame > Cut > Line > Stabilise
```

---

### Fragmented Tome: Stone Ward

Registry name:

```text
runic_tome_stone_ward
```

Family key:

```text
stone_ward
```

Rune pool:

```text
Earth, Guard, Wall, Circle, Heavy, Stabilise
```

Intended lessons:

- Earth is stable and defensive.
- Guard creates protection.
- Wall and Circle teach defensive forms.
- Heavy strengthens but slows/burdens formulae.
- Stabilise reduces backlash.

Starter formulas suggested by this family:

```text
Earth > Guard > Wall
Earth > Guard > Circle
Earth > Guard > Wall > Stabilise
Earth > Guard > Wall > Heavy
```

---

### Fragmented Tome: Gentle Renewal

Registry name:

```text
runic_tome_gentle_renewal
```

Family key:

```text
gentle_renewal
```

Rune pool:

```text
Life, Water, Heal, Circle, Veil, Stabilise
```

Intended lessons:

- Life and Water are restoration-friendly sources.
- Heal gives recovery intent.
- Circle supports area healing.
- Veil supports protective or soft delivery.
- Stabilise keeps healing formulae safer.

Starter formulas suggested by this family:

```text
Life > Heal > Circle
Water > Heal > Veil
Life > Heal > Circle > Stabilise
Water > Heal > Circle > Stabilise
```

---

### Fragmented Tome: Wind Step

Registry name:

```text
runic_tome_wind_step
```

Family key:

```text
wind_step
```

Rune pool:

```text
Wind, Push, Pull, Pulse, Veil, Quicken
```

Intended lessons:

- Wind supports movement, range, and speed.
- Push and Pull teach directional intent.
- Pulse creates burst movement or radial force.
- Veil hints at self-shaping or defensive movement.
- Quicken increases speed while reducing stability.

Starter formulas suggested by this family:

```text
Wind > Push > Pulse
Wind > Pull > Pulse
Wind > Push > Veil
Wind > Push > Pulse > Quicken
```

---

### Fragmented Tome: Frost Bind

Registry name:

```text
runic_tome_frost_bind
```

Family key:

```text
frost_bind
```

Rune pool:

```text
Frost, Water, Bind, Circle, Mark, Heavy
```

Intended lessons:

- Frost and Water support slowing/control effects.
- Bind restrains targets.
- Circle creates area control.
- Mark suggests targeted lingering effects.
- Heavy increases weight/control pressure.

Starter formulas suggested by this family:

```text
Frost > Bind > Circle
Frost > Bind > Mark
Water > Bind > Circle
Frost > Bind > Circle > Heavy
```

---

### Fragmented Tome: Storm Pierce

Registry name:

```text
runic_tome_storm_pierce
```

Family key:

```text
storm_pierce
```

Rune pool:

```text
Lightning, Cut, Pierce, Pulse, Bolt, Violent
```

Intended lessons:

- Lightning is a higher-pressure offensive source.
- Cut and Pierce push formulae toward sharp damage.
- Pulse and Bolt offer fast delivery.
- Violent makes the formula dangerous and unstable.

Starter formulas suggested by this family:

```text
Lightning > Cut > Bolt
Lightning > Pierce > Bolt
Lightning > Cut > Pulse
Lightning > Cut > Pierce > Pulse > Bolt > Violent
```

This family is more advanced than the simpler starter tomes and should usually be rarer.

---

## Starter Discovery Loot

A small data-only loot injection has been added using NeoForge global loot modifiers.

Files:

```text
src/main/resources/data/neoforge/loot_modifiers/global_loot_modifiers.json
src/main/resources/data/runic_ascension/loot_modifiers/add_runic_starter_discovery_to_common_chests.json
src/main/resources/data/runic_ascension/loot_modifiers/add_runic_starter_discovery_to_library_chests.json
src/main/resources/data/runic_ascension/loot_table/inject/starter_discovery_common.json
src/main/resources/data/runic_ascension/loot_table/inject/starter_discovery_library.json
```

Current injected vanilla chest groups:

```text
Common chests:
- minecraft:chests/simple_dungeon
- minecraft:chests/abandoned_mineshaft
- minecraft:chests/desert_pyramid
- minecraft:chests/jungle_temple

Library/deeper chests:
- minecraft:chests/stronghold_library
- minecraft:chests/ancient_city
```

The complete archive tome is intentionally not included in natural loot.

---

## Future Improvements

### Per-copy stored fragments

At the moment, a partial tome chooses its random revealed runes when studied.

A future version could store the selected fragments on the item stack itself when the item is created as loot. That would make each copy feel more like a physical manuscript with fixed contents.

### Rarity tiers

Possible tome tiers:

```text
Torn Formula Scrap: 1-2 runes
Fragmented Tome: 2-3 runes
Incomplete Manual: 3-4 runes
True Formula Inheritance: exact full formula plus mastery bonus
Sect Archive: multiple formula families
```

### Structure-specific formula traditions

Future structures can have their own formula cultures.

Examples:

```text
Burnt tower loot -> Ember Bolt, Violent Flame, Ash Veil
Frozen ruins -> Frost Bind, Ice Wall, Still Water
Ancient battlefield -> Stone Ward, Metal Pierce, Blood Mark
Sky shrine -> Wind Step, Light Veil, Thunder Cut
```

### Ascension structure loot

When Ascension's custom structure loot table IDs are known, add them to the loot modifiers or create dedicated Rune Masters modifier files for them.

