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
        // Path
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

        // MISC
        add("itemGroup.runic_ascension", "Ascension: Rune Masters");

        // Runic Path Things
        add("item.runic_ascension.runic_tome", "Runic Tome");
        add("item.runic_ascension.runic_codex", "Runic Codex");
        add("item.runic_ascension.basic_runic_brush", "Basic Runic Brush");

        add("item.runic_ascension.runic_scrap_origin", "Runic Scrap: Origin");
        add("item.runic_ascension.runic_scrap_intent", "Runic Scrap: Intent");
        add("item.runic_ascension.runic_scrap_forms", "Runic Scrap: Forms");
        add("item.runic_ascension.runic_scrap_modifiers", "Runic Scrap: Modifiers");
        add("item.runic_ascension.runic_scrap_instability", "Runic Scrap: Instability");
        add("item.runic_ascension.runic_scrap_suppression", "Runic Scrap: Suppression");
        add("item.runic_ascension.runic_scrap_first_formula", "Runic Scrap: First Formula");
        add("item.runic_ascension.runic_scrap_free_casting", "Runic Scrap: Free Casting");
        add("runic_ascension.runic.scrap.tooltip.read", "Right-click to let the words settle in your mind.");

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
        add("runic_ascension.physiques.runic_testing", "Runic Testing Physique");
        add("runic_ascension.physiques.runic_testing.description.short", "A physique that awakens basic runic affinity.");
        add("runic_ascension.physiques.runic_testing.description", "A simple testing physique used to unlock and validate the Runic Path.");

        add("item.runic_ascension.runic_tome_base_elements", "Runic Tome: Base Elemental");
        add("item.runic_ascension.runic_tome_deep_elements", "Runic Tome: Deep Elements");
        add("item.runic_ascension.runic_tome_intents", "Runic Tome: Intents");
        add("item.runic_ascension.runic_tome_forms", "Runic Tome: Forms");
        add("item.runic_ascension.runic_tome_modifiers", "Runic Tome: Modifiers");

        add("runic_ascension.runic.tome.teaches", "Teaches:");

        add("runic_ascension.runic.casting.hover", "%s | %s | %s");

        add("runic_ascension.runic.codex.title", "Runic Codex");
        add("runic_ascension.runic.codex.runes", "Known Runes");
        add("runic_ascension.runic.codex.sequences", "Discovered Sequences");
        add("runic_ascension.runic.codex.no_runes", "No known runes.");
        add("runic_ascension.runic.codex.no_sequences", "No discovered sequences.");
        add("runic_ascension.runic.codex.select_entry", "Select a rune or sequence to inspect it.");
        add("runic_ascension.runic.codex.page", "%s / %s");
        add("runic_ascension.runic.codex.rune_details", "Type: %s | Depth: %s | Use Realm: %s");
        add("runic_ascension.runic.codex.rune_id", "Rune ID: %s");
        add("runic_ascension.runic.codex.sequence_details", "Tier: %s | Min Realm: %s | Qi: %s");
        add("runic_ascension.runic.codex.sequence_formula", "Formula: %s");

        add("runic_ascension.runic.codex.not_on_path", "The codex remains shut. You have not entered the Runic Path.");
        add("runic_ascension.runic.codex.unknown", "Unknown");
        add("runic_ascension.runic.codex.unknown_formula", "Unknown Formula");
        add("runic_ascension.runic.codex.generated_formula_name", "%s Formula");
        add("runic_ascension.runic.codex.generated_formula_desc", "A %s formula shaped from %s and guided by %s.");
        add("runic_ascension.runic.codex.formula_type", "Type: Discovered Formula");
        add("runic_ascension.runic.codex.formula_mastery", "Mastery: %s | Casts: %s");
        add("runic_ascension.runic.codex.formula_runes", "Formula: %s");

        add("runic_ascension.runic.tome.not_on_path", "The runes remain silent. You have not entered the Runic Path.");
        add("runic_ascension.runic.tome.no_new_runes", "This tome contains no new runes.");
        add("runic_ascension.runic.tome.learned", "You learned %s runes.");
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
        add("runic_ascension.runic.casting.suppression", "Suppression: Realm %s / %s");
        add("runic_ascension.runic.casting.suppression.none", "Suppression unavailable.");
        add("runic_ascension.runic.casting.selected.empty", "Selected: none");
        add("runic_ascension.runic.casting.preview.empty", "Profile: waiting for a source and intent.");
        add("runic_ascension.runic.casting.preview", "Profile: %s | %s | %s");
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

        add("runic_ascension.bloodline.runic_tester_bloodline", "Runic Tester Bloodline");
        add("runic_ascension.bloodline.runic_tester_bloodline.short", "A testing bloodline with minor runic circulation benefits.");
        add("runic_ascension.bloodline.runic_tester_bloodline.desc", "A temporary testing bloodline used while developing runic bloodline affinity and mastery bonuses.");

        add("runic_ascension.skill.runic_sight", "Runic Sight");
        add("runic_ascension.skill.runic_sight.description", "Opens the inner eye to glimpse runes hidden in nearby blocks and traces.");

        add("runic_ascension.runic.casting.empty.title", "The script is silent.");
        add("runic_ascension.runic.casting.empty.line_1", "You do not know any usable runes yet.");
        add("runic_ascension.runic.casting.empty.line_2", "Study a Runic Tome to awaken your first script.");
        add("runic_ascension.runic.casting.selected.no_usable", "No usable runes known.");
        add("runic_ascension.runic.casting.close", "Close");


    }
}
