package net.zic.runic_ascension.common.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.zic.runic_ascension.RunicAscension;

public class RunicLangProvider extends LanguageProvider {

    public RunicLangProvider(PackOutput output, String locale) {
        super(output, RunicAscension.MOD_ID, locale);
    }

    @Override
    protected void addTranslations() {

        add("itemGroup.runic_ascension", "Ascension: Rune Masters");



        // PATH
        add("runic_ascension.path.runic", "Runic Path");
        add("runic_ascension.path.runic.description", "A path of symbols, scripts, and living formulae that reveals the hidden language of reality.");

        add("runic_ascension.path.runic.trace_sensing", "Trace Sensing");
        add("runic_ascension.path.runic.rune_knowing", "Rune Knowing");
        add("runic_ascension.path.runic.script_visualisation", "Script Visualisation");
        add("runic_ascension.path.runic.pattern_enlightenment", "Pattern Enlightenment");
        add("runic_ascension.path.runic.rune_breath", "Rune Breath");
        add("runic_ascension.path.runic.soul_inscription", "Soul Inscription");
        add("runic_ascension.path.runic.living_script", "Living Script");
        add("runic_ascension.path.runic.dao_seed", "Dao Seed");
        add("runic_ascension.path.runic.origin_spark", "Origin Spark");
        add("runic_ascension.path.runic.infinite_script", "Infinite Script");



        // RUNIC PATH THINGS
        add("item.runic_ascension.runic_tome", "Runic Tome: Complete Archive");
        add("item.runic_ascension.runic_tome_ember_bolt", "Fragmented Tome: Ember Bolt");
        add("item.runic_ascension.runic_tome_stone_ward", "Fragmented Tome: Stone Ward");
        add("item.runic_ascension.runic_tome_gentle_renewal", "Fragmented Tome: Gentle Renewal");
        add("item.runic_ascension.runic_tome_wind_step", "Fragmented Tome: Wind Step");
        add("item.runic_ascension.runic_tome_frost_bind", "Fragmented Tome: Frost Bind");
        add("item.runic_ascension.runic_tome_storm_pierce", "Fragmented Tome: Storm Pierce");
        add("item.runic_ascension.runic_codex", "Runic Codex");
        add("item.runic_ascension.basic_runic_brush", "Basic Runic Brush");
        add("item.runic_ascension.earth_runic_brush", "Earth Script Brush");
        add("item.runic_ascension.heaven_runic_brush", "Heaven Script Brush");
        add("item.runic_ascension.hell_runic_brush", "Hell Script Brush");

        add("item.runic_ascension.runic_scrap_origin", "Runic Scrap: Origin");
        add("item.runic_ascension.runic_scrap_intent", "Runic Scrap: Intent");
        add("item.runic_ascension.runic_scrap_forms", "Runic Scrap: Forms");
        add("item.runic_ascension.runic_scrap_modifiers", "Runic Scrap: Modifiers");
        add("item.runic_ascension.runic_scrap_instability", "Runic Scrap: Instability");
        add("item.runic_ascension.runic_scrap_suppression", "Runic Scrap: Suppression");
        add("item.runic_ascension.runic_scrap_first_formula", "Runic Scrap: First Formula");
        add("item.runic_ascension.runic_scrap_free_casting", "Runic Scrap: Free Casting");

        add("item.runic_ascension.runic_inscription_seal", "Runic Inscription Seal");

        add("runic_ascension.inscription.seal.tooltip", "Used to carve known inscription patterns into the body.");
        add("runic_ascension.inscription.seal.tooltip.selected", "Selected inscription: %s");
        add("runic_ascension.inscription.seal.tooltip.cycle", "Sneak and right-click to cycle the selected inscription.");
        add("runic_ascension.inscription.seal.selected", "Selected inscription: %s");
        add("runic_ascension.inscription.seal.no_selection", "No inscription pattern is available.");
        add("runic_ascension.inscription.seal.missing_material", "Hold the required material in your other hand: %s.");

        add("runic_ascension.runic.scrap.tooltip.read", "Right-click to let the words settle in your mind.");
        add("runic_ascension.runic.scrap.recorded", "%s settles into your Codex.");
        add("runic_ascension.runic.scrap.already_recorded", "%s is already recorded in your Codex.");

        add("runic_ascension.runic.brush.type.basic", "Basic Brush");
        add("runic_ascension.runic.brush.type.earth", "Earth Brush");
        add("runic_ascension.runic.brush.type.heaven", "Heaven Brush");
        add("runic_ascension.runic.brush.type.hell", "Hell Brush");
        add("runic_ascension.runic.brush.tooltip.basic", "A steady beginner brush that slightly softens cost and backlash.");
        add("runic_ascension.runic.brush.tooltip.earth", "A grounded brush that steadies barriers and enduring scripts.");
        add("runic_ascension.runic.brush.tooltip.heaven", "A precise brush that thins qi cost and lengthens clean formulae.");
        add("runic_ascension.runic.brush.tooltip.hell", "A hungry brush that deepens power while inviting harsher backlash.");
        add("runic_ascension.runic.brush.tooltip.slots", "+%s rune slot while held.");

        add("runic_ascension.runic.scrap.origin.title", "A Brittle Scrap on Origins");
        add("runic_ascension.runic.scrap.origin.tooltip", "A torn lesson about where formulae begin.");
        add("runic_ascension.runic.scrap.origin.line_1", "A formula begins where power takes its first breath.");
        add("runic_ascension.runic.scrap.origin.line_2", "Flame, wind, earth, life and their kin are origins.");
        add("runic_ascension.runic.scrap.origin.line_3", "An origin alone only wakes. It does not yet choose.");
        add("runic_ascension.runic.scrap.origin.line_4", "Pair it with intent, and the script begins to move.");

        add("runic_ascension.runic.scrap.intent.title", "A Brittle Scrap on Intent");
        add("runic_ascension.runic.scrap.intent.tooltip", "A torn lesson about giving power a will.");
        add("runic_ascension.runic.scrap.intent.line_1", "Intent is the desire carved into a sleeping origin.");
        add("runic_ascension.runic.scrap.intent.line_2", "Cut divides, guard preserves, bind restrains, heal restores.");
        add("runic_ascension.runic.scrap.intent.line_3", "Without intent, power pools like rain in an unmarked bowl.");
        add("runic_ascension.runic.scrap.intent.line_4", "With intent, the rune remembers what it must become.");

        add("runic_ascension.runic.scrap.forms.title", "A Brittle Scrap on Forms");
        add("runic_ascension.runic.scrap.forms.tooltip", "A torn lesson about giving formulae a body.");
        add("runic_ascension.runic.scrap.forms.line_1", "Form gives a formula its vessel.");
        add("runic_ascension.runic.scrap.forms.line_2", "Bolt flies, line draws, wall stands, circle surrounds.");
        add("runic_ascension.runic.scrap.forms.line_3", "The same origin and intent may change greatly by shape.");
        add("runic_ascension.runic.scrap.forms.line_4", "A wise hand chooses the vessel before releasing the truth.");

        add("runic_ascension.runic.scrap.modifiers.title", "A Brittle Scrap on Lesser Strokes");
        add("runic_ascension.runic.scrap.modifiers.tooltip", "A torn lesson about modifiers and finishing strokes.");
        add("runic_ascension.runic.scrap.modifiers.line_1", "Modifiers are lesser strokes, but lesser does not mean weak.");
        add("runic_ascension.runic.scrap.modifiers.line_2", "Quicken hastens, stabilise steadies, heavy burdens, violent wounds.");
        add("runic_ascension.runic.scrap.modifiers.line_3", "A modifier before the root makes the script stumble.");
        add("runic_ascension.runic.scrap.modifiers.line_4", "Place the root first, then refine what it becomes.");

        add("runic_ascension.runic.scrap.instability.title", "A Singed Scrap on Instability");
        add("runic_ascension.runic.scrap.instability.tooltip", "A scorched warning about backlash.");
        add("runic_ascension.runic.scrap.instability.line_1", "Some formulae obey while trembling.");
        add("runic_ascension.runic.scrap.instability.line_2", "Violence, haste, depth and poor order all invite backlash.");
        add("runic_ascension.runic.scrap.instability.line_3", "Stabilising strokes can quiet the quarrel, but may soften the result.");
        add("runic_ascension.runic.scrap.instability.line_4", "A reckless rune master writes with blood when ink runs thin.");

        add("runic_ascension.runic.scrap.suppression.title", "A Folded Scrap on Suppression");
        add("runic_ascension.runic.scrap.suppression.tooltip", "A folded lesson about holding back a formula.");
        add("runic_ascension.runic.scrap.suppression.line_1", "A higher script need not always descend in full.");
        add("runic_ascension.runic.scrap.suppression.line_2", "Suppress the realm of a formula to spare qi and narrow its reach.");
        add("runic_ascension.runic.scrap.suppression.line_3", "A small flame may light a path where a great one burns the forest.");
        add("runic_ascension.runic.scrap.suppression.line_4", "Mastery teaches the hand how much truth to reveal.");

        add("runic_ascension.runic.scrap.first_formula.title", "A Scrap Bearing a Simple Formula");
        add("runic_ascension.runic.scrap.first_formula.tooltip", "A worn hint toward a beginner formula.");
        add("runic_ascension.runic.scrap.first_formula.line_1", "Flame awakens. Cut gives hunger. Bolt gives path.");
        add("runic_ascension.runic.scrap.first_formula.line_2", "Together they become a wound written in fire.");
        add("runic_ascension.runic.scrap.first_formula.line_3", "Change bolt to line, and the wound becomes a drawn edge.");
        add("runic_ascension.runic.scrap.first_formula.line_4", "Add violent, and power rises while stability falls.");
        add("runic_ascension.runic.scrap.first_formula.line_5", "A first formula is not the answer, only a door.");

        add("runic_ascension.runic.scrap.free_casting.title", "A Half-Remembered Scrap on Free Casting");
        add("runic_ascension.runic.scrap.free_casting.tooltip", "A torn lesson about writing beyond memorised sequences.");
        add("runic_ascension.runic.scrap.free_casting.line_1", "Recorded sequences are safe roads worn by older hands.");
        add("runic_ascension.runic.scrap.free_casting.line_2", "Free formulae are roads written as you walk.");
        add("runic_ascension.runic.scrap.free_casting.line_3", "Learn origins, wills, vessels and strokes, then test their grammar.");
        add("runic_ascension.runic.scrap.free_casting.line_4", "When enough runes answer you, the Codex becomes a map instead of a cage.");

        add("runic_ascension.runic.tome.complete_archive", "For §b§4[Admin]§r Usage Only");
        add("runic_ascension.runic.tome.fragment_family", "Fragmented formula family:");
        add("runic_ascension.runic.tome.reveals_range", "Each copy reveals %s-%s strokes when studied.");
        add("runic_ascension.runic.tome.family.complete_archive", "Complete Archive");
        add("runic_ascension.runic.tome.family.ember_bolt", "Ember Bolt Script");
        add("runic_ascension.runic.tome.family.stone_ward", "Stone Ward Script");
        add("runic_ascension.runic.tome.family.gentle_renewal", "Gentle Renewal Script");
        add("runic_ascension.runic.tome.family.wind_step", "Wind Step Script");
        add("runic_ascension.runic.tome.family.frost_bind", "Frost Binding Script");
        add("runic_ascension.runic.tome.family.storm_pierce", "Storm-Piercing Script");

        add("runic_ascension.runic.tome.learned_named", "The rune(s) awaken in your mind: %s.");
        add("runic_ascension.runic.tome.learned_named_with_veiled", "The rune(s) awaken: %s. %s deeper meaning(s) remain sealed.");
        add("runic_ascension.runic.tome.learned_all_veiled", "%s hidden meaning(s) sink into your spirit, still sealed from sight.");

        add("runic_ascension.runic.casting.hover", "%s | %s | %s");
        add("runic_ascension.runic.casting.hover.empty", "Hover a rune to read its trace.");

        add("runic_ascension.runic.codex.title", "Runic Codex");
        add("runic_ascension.runic.codex.runes", "Known Runes");
        add("runic_ascension.runic.codex.sequences", "Discovered Sequences");
        add("runic_ascension.runic.codex.no_runes", "No known runes.");
        add("runic_ascension.runic.codex.no_sequences", "No discovered sequences.");
        add("runic_ascension.runic.codex.select_entry", "Select a rune or sequence to inspect it.");
        add("runic_ascension.runic.codex.page", "%s / %s");
        add("runic_ascension.runic.codex.rune_details", "Type: %s | Depth: %s | Use Realm: %s");
        add("runic_ascension.runic.codex.rune_observe", "Observe Realm: %s");
        add("runic_ascension.runic.codex.rune_id", "Rune ID: %s");
        add("runic_ascension.runic.codex.sequence_details", "Tier: %s | Min Realm: %s | Qi: %s");
        add("runic_ascension.runic.codex.sequence_formula", "Formula: %s");

        add("runic_ascension.runic.codex.not_on_path", "The codex remains shut. You have not entered the Runic Path.");
        add("runic_ascension.runic.codex.unknown", "Unknown");
        add("runic_ascension.runic.codex.none", "None");
        add("runic_ascension.runic.codex.unknown_formula", "Unknown Formula");
        add("runic_ascension.runic.codex.scrap_hint.title", "Unread Scraps");
        add("runic_ascension.runic.codex.scrap_hint.line_1", "Right-click runic scraps to press their lessons into this Codex.");
        add("runic_ascension.runic.codex.scrap_hint.line_2", "When nothing is selected, this panel rotates through recorded notes.");
        add("runic_ascension.runic.codex.scrap_hint.rune_side", "Rune notes will help explain origins, intent, forms, and modifiers.");
        add("runic_ascension.runic.codex.scrap_hint.sequence_side", "Sequence notes will help explain formula safety, mastery, and suppression.");
        add("runic_ascension.runic.codex.generated_formula_name", "%s Formula");
        add("runic_ascension.runic.codex.generated_formula_desc", "A %s formula shaped from %s and guided by %s.");
        add("runic_ascension.runic.codex.formula_type", "Type: Discovered Formula");
        add("runic_ascension.runic.codex.formula_summary", "Archetype: %s | Nature: %s");
        add("runic_ascension.runic.codex.formula_roles", "Source: %s | Intent: %s | Form: %s");
        add("runic_ascension.runic.codex.formula_mastery", "Mastery: %s | Casts: %s");
        add("runic_ascension.runic.codex.formula_profile_numbers", "Power: %s | Range: %s | Stability: %s");
        add("runic_ascension.runic.codex.formula_flags", "Effects: %s");
        add("runic_ascension.runic.codex.formula_runes", "Formula: %s");

        add("runic_ascension.runic.tome.not_on_path", "The script remains inert. You have not entered the Runic Path.");
        add("runic_ascension.runic.tome.no_new_runes", "The tome echoes only signs you already know.");
        add("runic_ascension.runic.tome.learned", "Knowledge solidifies around %s new rune(s).");
        add("runic_ascension.runic.sequence.cast", "You cast the sequence: %s");

        add("runic_ascension.runic.rune.flame", "Flame");
        add("runic_ascension.runic.rune.water", "Water");
        add("runic_ascension.runic.rune.wind", "Wind");
        add("runic_ascension.runic.rune.earth", "Earth");
        add("runic_ascension.runic.rune.wood", "Wood");
        add("runic_ascension.runic.rune.metal", "Metal");
        add("runic_ascension.runic.rune.lightning", "Lightning");
        add("runic_ascension.runic.rune.frost", "Frost");
        add("runic_ascension.runic.rune.bind", "Bind");
        add("runic_ascension.runic.rune.push", "Push");
        add("runic_ascension.runic.rune.pull", "Pull");
        add("runic_ascension.runic.rune.guard", "Guard");
        add("runic_ascension.runic.rune.cut", "Cut");
        add("runic_ascension.runic.rune.heal", "Heal");
        add("runic_ascension.runic.rune.bolt", "Bolt");
        add("runic_ascension.runic.rune.veil", "Veil");
        add("runic_ascension.runic.rune.circle", "Circle");
        add("runic_ascension.runic.rune.mark", "Mark");
        add("runic_ascension.runic.rune.quicken", "Quicken");
        add("runic_ascension.runic.rune.stabilise", "Stabilise");
        add("runic_ascension.runic.rune.gather", "Gather");
        add("runic_ascension.runic.rune.release", "Release");
        add("runic_ascension.runic.rune.pierce", "Pierce");
        add("runic_ascension.runic.rune.compress", "Compress");
        add("runic_ascension.runic.rune.wall", "Wall");
        add("runic_ascension.runic.rune.pulse", "Pulse");
        add("runic_ascension.runic.rune.heavy", "Heavy");
        add("runic_ascension.runic.rune.violent", "Violent");
        add("runic_ascension.runic.rune.hidden", "Hidden");
        add("runic_ascension.runic.rune.light", "Light");
        add("runic_ascension.runic.rune.shadow", "Shadow");
        add("runic_ascension.runic.rune.life", "Life");
        add("runic_ascension.runic.rune.decay", "Decay");
        add("runic_ascension.runic.rune.line", "Line");
        add("runic_ascension.runic.rune.sphere", "Sphere");

        add("runic_ascension.runic.sequence.ember_mark", "Ember Mark");
        add("runic_ascension.runic.sequence.ember_mark.desc", "Condenses flame into a brief aggressive mark.");
        add("runic_ascension.runic.sequence.clear_water_mend", "Clear Water Mend");
        add("runic_ascension.runic.sequence.clear_water_mend.desc", "Guides clear water through the body to mend minor wounds.");
        add("runic_ascension.runic.sequence.stone_ward", "Stone Ward");
        add("runic_ascension.runic.sequence.stone_ward.desc", "Raises a short-lived ward of compacted earth and stone.");
        add("runic_ascension.runic.sequence.wind_push", "Wind Push");
        add("runic_ascension.runic.sequence.wind_push.desc", "Releases a compressed gust that pushes enemies away.");
        add("runic_ascension.runic.sequence.frost_bind", "Frost Bind");
        add("runic_ascension.runic.sequence.frost_bind.desc", "Chains cold intent around a target, slowing their movement.");
        add("runic_ascension.runic.sequence.wind_step", "Wind Step");
        add("runic_ascension.runic.sequence.wind_step.desc", "Lightens the caster’s step with a quick breath of wind.");
        add("runic_ascension.runic.sequence.thunder_cut_bolt", "Thunder-Cut Bolt");
        add("runic_ascension.runic.sequence.thunder_cut_bolt.desc", "Shapes lightning through a cutting script and releases it forward.");
        add("runic_ascension.runic.sequence.veiled_step", "Veiled Step");
        add("runic_ascension.runic.sequence.veiled_step.desc", "Wraps the body in flowing wind and passing haze.");
        add("runic_ascension.runic.sequence.earthen_wall", "Earthen Wall");
        add("runic_ascension.runic.sequence.earthen_wall.desc", "Hardens the body beneath a shell of dense earth essence.");
        add("runic_ascension.runic.sequence.gathering_breath", "Gathering Breath");
        add("runic_ascension.runic.sequence.gathering_breath.desc", "Draws surrounding currents inward to steady the body and spirit.");
        add("runic_ascension.runic.sequence.piercing_flame_bolt", "Piercing Flame Bolt");
        add("runic_ascension.runic.sequence.piercing_flame_bolt.desc", "Condenses flame into a narrow bolt capable of piercing flesh and armor.");
        add("runic_ascension.runic.sequence.violent_wind_pulse", "Violent Wind Pulse");
        add("runic_ascension.runic.sequence.violent_wind_pulse.desc", "Detonates a violent ring of compressed wind around the caster.");

        add("runic_ascension.technique.basic_runic_script", "Basic Runic Script");
        add("runic_ascension.technique.basic_runic_script.description.short", "A simple script for tracing and casting basic runic sequences.");
        add("runic_ascension.technique.basic_runic_script.description", "A beginner Runic Path technique that teaches the cultivator how to arrange known runes into simple castable sequences.");

        add("runic_ascension.technique.runic_apprentice", "Runic Apprentice");
        add("runic_ascension.technique.runic_apprentice.description.short", "A beginner technique for sensing and shaping runic traces.");
        add("runic_ascension.technique.runic_apprentice.description", "A foundational runic technique that teaches the cultivator to perceive traces, arrange symbols, and begin shaping formulae through the Runic Path.");

        add("runic_ascension.skill.open_runic_casting", "Runic Casting");
        add("runic_ascension.skill.open_runic_casting.description"  , "Open the Runic Casting interface and weave known runes into a sequence.");
        add("runic_ascension.runic.casting.title", "Runic Casting");
        add("runic_ascension.runic.casting.timer", "Time left: %ss");
        add("runic_ascension.runic.casting.info", "Slots: %s | Time: %ss");
        add("runic_ascension.runic.casting.sequence_panel.title", "Sequence Reading");
        add("runic_ascension.runic.casting.realm_info", "Realm %s | Insight %s");
        add("runic_ascension.runic.casting.suppression", "Suppression: Realm %s / %s");
        add("runic_ascension.runic.casting.suppression.short", "Realm %s / %s");
        add("runic_ascension.runic.casting.suppression.none", "Suppression unavailable.");
        add("runic_ascension.runic.casting.selected.empty", "Selected: none");
        add("runic_ascension.runic.casting.selected.count", "Selected: %s / %s");
        add("runic_ascension.runic.casting.preview.empty", "Profile: waiting for a source and intent.");
        add("runic_ascension.runic.casting.preview.hint", "Choose runes to let the Codex murmur an estimate.");
        add("runic_ascension.runic.casting.preview.title", "%s | %s");
        add("runic_ascension.runic.casting.preview.profile", "Effects: %s");
        add("runic_ascension.runic.casting.preview.stats", "Qi: %s | Stability: %s | Range: %s");
        add("runic_ascension.runic.casting.preview.risk", "Risk: %s | Marks: %s");
        add("runic_ascension.runic.casting.preview.risk_brush", "Risk: %s | Brush: %s | Marks: %s");
        add("runic_ascension.runic.casting.preview.risk.unknown", "Risk: unreadable.");
        add("runic_ascension.runic.casting.preview.state.valid", "Stable");
        add("runic_ascension.runic.casting.preview.state.unstable", "Unstable");
        add("runic_ascension.runic.casting.preview.state.overreached", "Overreached");
        add("runic_ascension.runic.casting.preview.state.invalid", "Invalid: %s");
        add("runic_ascension.runic.casting.preview.insight.low.title", "A %s-shape whispers through the mist.");
        add("runic_ascension.runic.casting.preview.insight.low.stats", "Only the broad shape can be felt: %s.");
        add("runic_ascension.runic.casting.preview.insight.low.risk", "The deeper measures remain veiled.");
        add("runic_ascension.runic.casting.preview.insight.low.risk_brush", "A %s rests in your hand; the deeper measures remain veiled.");
        add("runic_ascension.runic.casting.preview.insight.medium.stats", "Source: %s | Intent: %s | Form: %s");
        add("runic_ascension.runic.casting.preview.insight.medium.risk", "The script's temper reads as: %s.");
        add("runic_ascension.runic.casting.preview.insight.medium.risk_brush", "Temper: %s | Brush: %s");
        add("runic_ascension.runic.casting.preview.insight.high.stats", "Qi: %s | Stability: %s | Range: %s");
        add("runic_ascension.runic.casting.backspace", "Back");
        add("runic_ascension.runic.casting.clear", "Clear");
        add("runic_ascension.runic.casting.cast", "Cast");
        add("runic_ascension.runic.cast.success", "The sequence takes form: %s");
        add("runic_ascension.runic.cast.no_entity_data", "No ascension data found.");
        add("runic_ascension.runic.cast.not_on_path", "You have not entered the Runic Path.");
        add("runic_ascension.runic.cast.no_usable_runes", "You do not know any usable runes.");
        add("runic_ascension.runic.cast.invalid_caster", "Invalid runic caster.");

        add("runic_ascension.runic.cast.formula_too_complex", "The script frays beyond the measure of your inner sea.");
        add("runic_ascension.runic.cast.rune_beyond_comprehension", "A rune slips past your grasp, its meaning still veiled.");
        add("runic_ascension.runic.cast.unstable_modifiers", "The lesser strokes quarrel, and the formula begins to shudder.");
        add("runic_ascension.runic.cast.not_enough_qi", "Your qi thins before the final stroke can be sealed.");
        add("runic_ascension.runic.cast.runes_do_not_align", "The runes refuse to share the same truth.");
        add("runic_ascension.runic.cast.unstable_formula", "The trembling script breaks, and backlash answers.");
        add("runic_ascension.runic.cast.unstable_formula_cast", "The formula trembles beneath your will, but obeys.");
        add("runic_ascension.runic.cast.failure", "The runes scatter like forgotten names.");
        add("runic_ascension.runic.cast.empty_sequence", "No truth is written, and the empty script collapses.");
        add("runic_ascension.runic.cast.missing_core_runes", "A source must breathe, and an intent must guide it.");
        add("runic_ascension.runic.cast.too_many_sources", "Too many origins awaken, each claiming the first breath.");
        add("runic_ascension.runic.cast.too_many_intents", "Too many wills pull the script into contradiction.");
        add("runic_ascension.runic.cast.too_many_forms", "Too many vessels twist the formula from shape into ruin.");
        add("runic_ascension.runic.cast.unstable_opening_modifier", "A stray stroke opens the rite, and the script loses its root.");

        add("runic_ascension.skill.runic_sight", "Runic Sight");
        add("runic_ascension.skill.runic_sight.description", "Opens the inner eye to glimpse runes hidden in nearby blocks and traces.");
        add("runic_ascension.runic.sight.no_traces", "No runic traces answer your sight.");
        add("runic_ascension.runic.sight.no_readable_traces", "The traces blur before their meaning can settle.");
        add("runic_ascension.runic.sight.too_deep", "%s deeper stroke(s) resist your sight.");
        add("runic_ascension.runic.sight.already_clear", "%s is already clear within your inner sight.");
        add("runic_ascension.runic.sight.glimpsed", "%s sharpens within your mind: %s%%.");
        add("runic_ascension.runic.sight.glimpsed_extra", "%s sharpens within your mind: %s%%. %s other trace(s) stir.");
        add("runic_ascension.runic.sight.glimpsed_with_veiled", "%s sharpens: %s%%. %s other trace(s) stir; %s deeper stroke(s) remain veiled.");
        add("runic_ascension.runic.sight.observed", "%s becomes clear within your inner sight.");
        add("runic_ascension.runic.sight.observed_extra", "%s becomes clear. %s other rune(s) settle beside it.");
        add("runic_ascension.runic.sight.observed_with_veiled", "%s becomes clear. %s other rune(s) settle; %s deeper stroke(s) remain veiled.");
        add("runic_ascension.runic.sight.toast.title", "Rune Observed");
        add("runic_ascension.runic.sight.toast.description", "%s has become clear.");

        add("runic_ascension.runic.casting.empty.title", "The script is silent.");
        add("runic_ascension.runic.casting.empty.line_1", "You do not know any usable runes yet.");
        add("runic_ascension.runic.casting.empty.line_2", "Study a Runic Tome to awaken your first script.");
        add("runic_ascension.runic.casting.empty.line_3", "Scraps now settle into the Codex instead of flooding chat.");
        add("runic_ascension.runic.casting.selected.no_usable", "No usable runes known.");
        add("runic_ascension.runic.casting.close", "Close");



        // SKILLS
        add("runic_ascension.skill.script_sever", "Script Sever");
        add("runic_ascension.skill.script_sever.description", "Draws a thin cutting line through a target. Requires the Cut and Line runes.");
        add("runic_ascension.skill.cinder_brand", "Cinder Brand");
        add("runic_ascension.skill.cinder_brand.description", "Marks a target with burning script. Requires the Flame, Mark, and Release runes.");
        add("runic_ascension.skill.stone_ward", "Stone Ward");
        add("runic_ascension.skill.stone_ward.description", "Sustains a defensive stone ward while held, consuming qi over time. The ward pushes nearby enemies back and disrupts incoming projectiles. Requires the Earth and Guard runes.");
        add("runic_ascension.skill.water_mantle", "Water Mantle");
        add("runic_ascension.skill.water_mantle.description", "Wraps the caster in flowing script while held, consuming qi over time. The mantle extinguishes flame, mends wounds, and dampens incoming force. Requires the Water and Heal runes.");
        add("runic_ascension.skill.trace_step", "Trace Step");
        add("runic_ascension.skill.trace_step.description", "Steps through a thin wind-veiled trace. Range increases with Runic realm. Requires the Wind and Veil runes.");



        // PHYSIQUES
        add("runic_ascension.physiques.runic_subject", "Runic Subject");
        add("runic_ascension.physiques.runic_subject.description.short", "A faintly marked physique with basic runic affinity.");
        add("runic_ascension.physiques.runic_subject.description", "The body bears shallow traces of script, as if reality began writing and stopped before the final stroke. This physique grants a minor affinity for the Runic Path and may one day evolve toward soul, body, or meridian inscription.");
        add("runic_ascension.physiques.runic_eyes", "Runic Eyes");
        add("runic_ascension.physiques.runic_eyes.description.short", "Eyes that read traces hidden beneath the surface of the world.");
        add("runic_ascension.physiques.runic_eyes.description", "The pupils hold thin rings of living script, allowing the cultivator to perceive hidden strokes with unusual clarity. This physique greatly favours the Runic Path and is especially suited to discovery, observation, and formula study.");
        add("runic_ascension.physiques.essence_rune_physique", "Essence Rune Physique");
        add("runic_ascension.physiques.essence_rune_physique.description.short", "A physique where runic traces mingle with essence circulation.");
        add("runic_ascension.physiques.essence_rune_physique.description", "Runic marks settle into the flow of essence, guiding qi through deliberate patterns. This physique supports both the Essence Path and the Runic Path, making it suitable for cultivators who shape formulae through breath, flow, and inner refinement.");
        add("runic_ascension.physiques.body_rune_physique", "Body Rune Physique");
        add("runic_ascension.physiques.body_rune_physique.description.short", "A physique where the flesh remembers protective script.");
        add("runic_ascension.physiques.body_rune_physique.description", "The skin, bones, and muscles respond naturally to inscription, as if the body were waiting to become a living talisman. This physique supports both the Body Path and the Runic Path, favouring defensive formulae, bodily inscriptions, and close-range endurance.");
        add("runic_ascension.physiques.soul_rune_physique", "Soul Rune Physique");
        add("runic_ascension.physiques.soul_rune_physique.description.short", "A physique where the soul echoes with half-formed glyphs.");
        add("runic_ascension.physiques.soul_rune_physique.description", "Runic traces drift through the soul like lanterns under dark water, sharpening perception and deepening symbolic control. This physique supports both the Soul Path and the Runic Path, favouring observation, invocation, and inner script manipulation.");
        add("runic_ascension.physiques.invoking_soul", "Invoking Soul");
        add("runic_ascension.physiques.invoking_soul.description.short", "A sealed evolution focused on soul-bound runic invocation.");
        add("runic_ascension.physiques.invoking_soul.description", "The soul becomes a chamber of spoken glyphs, capable of calling script through intent alone. This evolution is aligned with the Runic and Soul Paths. Its deeper potential is currently sealed.");
        add("runic_ascension.physiques.inscribing_body", "Inscribing Body");
        add("runic_ascension.physiques.inscribing_body.description.short", "A sealed evolution focused on body-bound runic inscription.");
        add("runic_ascension.physiques.inscribing_body.description", "The body accepts runes as naturally as breath, turning flesh, bone, and meridian into a living scripture. This evolution is aligned with the Runic and Body Paths. Its deeper potential is currently sealed.");
        add("runic_ascension.physiques.etching_meridians", "Etching Meridians");
        add("runic_ascension.physiques.etching_meridians.description.short", "A sealed evolution focused on runic essence circulation.");
        add("runic_ascension.physiques.etching_meridians.description", "The meridians harden into channels of deliberate script, refining qi as it moves through the body. This evolution is aligned with the Runic and Essence Paths. Its deeper potential is currently sealed.");
        add("runic_ascension.physiques.runic_perfection", "Runic Perfection");
        add("runic_ascension.physiques.runic_perfection.description.short", "A sealed physique approaching pure runic harmony.");
        add("runic_ascension.physiques.runic_perfection.description", "Every breath, thought, and movement approaches the rhythm of a completed formula. The body no longer carries runes as markings; it begins to resemble a script written by reality itself. This evolution is currently sealed.");



        // BLOODLINES
        add("runic_ascension.bloodline.ink_blood_lineage", "Ink-Blood Lineage");
        add("runic_ascension.bloodline.ink_blood_lineage.short", "A bloodline that carries qi like living ink.");
        add("runic_ascension.bloodline.ink_blood_lineage.desc", "The blood runs dark with faint script, carrying qi through the body like ink across prepared paper. This lineage increases maximum qi and qi regeneration, making it well suited to repeated formula casting and long study of the Runic Path.");
        add("runic_ascension.bloodline.burning_blood_runes", "Burning Blood Runes");
        add("runic_ascension.bloodline.burning_blood_runes.short", "A fierce bloodline marked by hot, aggressive script.");
        add("runic_ascension.bloodline.burning_blood_runes.desc", "The blood carries ember-bright runes that flare under strain, strengthening the body through heat and violence. This lineage greatly increases maximum health and hints at future synergy with flame, violent, and backlash-heavy formulae.");



        // RUNIC INSCRIPTIONS
        add("runic_ascension.inscription.unlock.invalid", "The inscription cannot be read.");
        add("runic_ascension.inscription.unlock.not_on_path", "The script refuses your body. You have not entered the Runic Path.");
        add("runic_ascension.inscription.unlock.maxed", "%s has already reached its current limit.");
        add("runic_ascension.inscription.unlock.realm_too_low", "Your Runic realm is too low. Requires Runic Realm %s.");
        add("runic_ascension.inscription.unlock.missing_runes", "The pattern is incomplete. You are missing %s required rune(s).");
        add("runic_ascension.inscription.unlock.success", "%s settles into your body.");
        add("runic_ascension.inscription.flameguard", "Flameguard Inscription");
        add("runic_ascension.inscription.flameguard.description.short", "A defensive inscription that teaches the body to endure flame.");
        add("runic_ascension.inscription.flameguard.description", "Flame and guard are carved into the body as a sealed ember-script. The inscription dulls fire and later steadies flame formulae against backlash.");
        add("runic_ascension.inscription.flameguard.tier.1", "Minor Flameguard");
        add("runic_ascension.inscription.flameguard.tier.1.description", "Slightly reduces fire damage.");
        add("runic_ascension.inscription.flameguard.tier.2", "Stabilised Flameguard");
        add("runic_ascension.inscription.flameguard.tier.2.description", "Further reduces fire damage and slightly softens flame backlash.");
        add("runic_ascension.inscription.flameguard.tier.3", "Perfected Flameguard");
        add("runic_ascension.inscription.flameguard.tier.3.description", "Greatly reduces fire damage and makes flame formulae easier to endure.");
        add("runic_ascension.inscription.stonehide", "Stonehide Inscription");
        add("runic_ascension.inscription.stonehide.description.short", "A body inscription that roots defense into flesh and bone.");
        add("runic_ascension.inscription.stonehide.description", "Earth and guard are pressed into the body as a dense protective script. The inscription strengthens Stone Ward and improves grounded defensive patterns.");
        add("runic_ascension.inscription.stonehide.tier.1", "Minor Stonehide");
        add("runic_ascension.inscription.stonehide.tier.1.description", "Slightly empowers Stone Ward.");
        add("runic_ascension.inscription.stonehide.tier.2", "Heavy Stonehide");
        add("runic_ascension.inscription.stonehide.tier.2.description", "Further empowers Stone Ward and improves its force.");
        add("runic_ascension.inscription.stonehide.tier.3", "Bastion Stonehide");
        add("runic_ascension.inscription.stonehide.tier.3.description", "Greatly empowers Stone Ward and strengthens defensive earth scripts.");
        add("runic_ascension.inscription.clearflow", "Clearflow Inscription");
        add("runic_ascension.inscription.clearflow.description.short", "A healing inscription that keeps the inner current clear.");
        add("runic_ascension.inscription.clearflow.description", "Water and heal are threaded through the body as a flowing recovery pattern. The inscription improves Water Mantle and later deepens restorative formulae.");
        add("runic_ascension.inscription.clearflow.tier.1", "Minor Clearflow");
        add("runic_ascension.inscription.clearflow.tier.1.description", "Slightly improves Water Mantle healing.");
        add("runic_ascension.inscription.clearflow.tier.2", "Circling Clearflow");
        add("runic_ascension.inscription.clearflow.tier.2.description", "Further improves Water Mantle healing and grants a brief recovery pulse.");
        add("runic_ascension.inscription.clearflow.tier.3", "Living Clearflow");
        add("runic_ascension.inscription.clearflow.tier.3.description", "Greatly improves Water Mantle healing and strengthens restorative scripts.");
        add("runic_ascension.inscription.windstep", "Windstep Inscription");
        add("runic_ascension.inscription.windstep.description.short", "A movement inscription that thins the body into a moving trace.");
        add("runic_ascension.inscription.windstep.description", "Wind and quicken are carved along the limbs, letting movement follow runic traces more easily. The inscription improves Trace Step and later adds brief speed after stepping.");
        add("runic_ascension.inscription.windstep.tier.1", "Minor Windstep");
        add("runic_ascension.inscription.windstep.tier.1.description", "Slightly increases Trace Step range.");
        add("runic_ascension.inscription.windstep.tier.2", "Veiled Windstep");
        add("runic_ascension.inscription.windstep.tier.2.description", "Increases Trace Step range and grants brief speed after stepping.");
        add("runic_ascension.inscription.windstep.tier.3", "Hidden Windstep");
        add("runic_ascension.inscription.windstep.tier.3.description", "Greatly improves Trace Step and leaves only a faint trace behind.");
        add("runic_ascension.inscription.still_script", "Still-Script Inscription");
        add("runic_ascension.inscription.still_script.description.short", "A stabilising inscription that quiets quarrelling formulae.");
        add("runic_ascension.inscription.still_script.description", "Stabilise and circle are carved as a closed pattern, forcing unstable strokes to move in harmony. The inscription reduces backlash and improves formula stability.");
        add("runic_ascension.inscription.still_script.tier.1", "Minor Still-Script");
        add("runic_ascension.inscription.still_script.tier.1.description", "Slightly reduces backlash.");
        add("runic_ascension.inscription.still_script.tier.2", "Gathered Still-Script");
        add("runic_ascension.inscription.still_script.tier.2.description", "Reduces backlash and improves formula stability.");
        add("runic_ascension.inscription.still_script.tier.3", "Hidden Still-Script");
        add("runic_ascension.inscription.still_script.tier.3.description", "Greatly reduces backlash and steadies unstable formulae.");
        add("runic_ascension.inscription.mirror_mark", "Mirror-Marked Inscription");
        add("runic_ascension.inscription.mirror_mark.description.short", "A strange defensive inscription that remembers hostile intent.");
        add("runic_ascension.inscription.mirror_mark.description", "Mark, guard, and veil are arranged into a reactive script that watches for danger. The inscription subtly improves wards and may later reflect hostile traces.");
        add("runic_ascension.inscription.mirror_mark.tier.1", "Minor Mirror-Mark");
        add("runic_ascension.inscription.mirror_mark.tier.1.description", "Slightly improves ward radius.");
        add("runic_ascension.inscription.mirror_mark.tier.2", "Pulsing Mirror-Mark");
        add("runic_ascension.inscription.mirror_mark.tier.2.description", "Improves ward radius and prepares future reactive effects.");
        add("runic_ascension.inscription.mirror_mark.tier.3", "Luminous Mirror-Mark");
        add("runic_ascension.inscription.mirror_mark.tier.3.description", "Greatly improves ward control and prepares future reflection effects.");
    }
}
