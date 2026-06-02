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
        add("runic_ascension.runic.casting.selected.empty", "Selected: none");
        add("runic_ascension.runic.casting.backspace", "Back");
        add("runic_ascension.runic.casting.clear", "Clear");
        add("runic_ascension.runic.casting.cast", "Cast");
        add("runic_ascension.runic.cast.success", "The sequence takes form: %s");
        add("runic_ascension.runic.cast.no_entity_data", "No ascension data found.");
        add("runic_ascension.runic.cast.not_on_path", "You have not entered the Runic Path.");
        add("runic_ascension.runic.cast.no_usable_runes", "You do not know any usable runes.");
        add("runic_ascension.runic.cast.invalid_caster", "Invalid runic caster.");

        add("runic_ascension.runic.cast.formula_too_complex", "The formula exceeds your current script capacity.");
        add("runic_ascension.runic.cast.rune_beyond_comprehension", "One or more runes slip beyond your comprehension.");
        add("runic_ascension.runic.cast.unstable_modifiers", "The modifiers destabilise the formula.");
        add("runic_ascension.runic.cast.not_enough_qi", "You lack the qi to complete the formula.");
        add("runic_ascension.runic.cast.runes_do_not_align", "The runes do not align.");
        add("runic_ascension.runic.cast.failure", "The runes do not align.");
        add("runic_ascension.runic.cast.empty_sequence", "The script collapses before a formula can form.");
        add("runic_ascension.runic.cast.missing_core_runes", "A formula needs at least a source and an intent.");
        add("runic_ascension.runic.cast.too_many_sources", "Too many sources fight for dominance.");
        add("runic_ascension.runic.cast.too_many_intents", "Too many intents pull the formula apart.");
        add("runic_ascension.runic.cast.too_many_forms", "Too many forms distort the script.");
        add("runic_ascension.runic.cast.unstable_opening_modifier", "Opening with a modifier destabilises the formula.");

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
