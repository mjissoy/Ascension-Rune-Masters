package net.zic.runic_ascension.content.casting;

import net.minecraft.world.entity.LivingEntity;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.stats.Stat;
import net.thejadeproject.ascension.refactor_packages.stats.custom.ModStats;
import net.zic.runic_ascension.content.RunicPathHelper;
import net.zic.runic_ascension.content.RunicPlayerData;

public final class RunicFormulaScaling {

    private RunicFormulaScaling() {
    }

    public static RunicFormulaStats calculate(RunicFormula formula, LivingEntity caster, int runicRealm) {
        float intelligence = getStat(caster, ModStats.INTELLIGENCE.get());
        float strength = getStat(caster, ModStats.STRENGTH.get());
        float agility = getStat(caster, ModStats.AGILITY.get());
        float vitality = getStat(caster, ModStats.VITALITY.get());

        float damage = 1.0F + (runicRealm * 0.35F);
        float duration = 1.0F + (runicRealm * 0.15F);
        float range = 1.0F + (runicRealm * 0.10F);
        float qiCost = 1.0F + (formula.inputRunes().size() * 0.22F);
        float backlash = 1.0F;

        damage += intelligence * 0.025F;

        // Same-category clauses are the start of the wide grammar goal:
        // extra sources blend concepts, extra intents layer behaviour, extra forms shape delivery.
        damage += Math.max(0, formula.sourceCount() - 1) * 0.22F;
        damage += Math.max(0, formula.intentCount() - 1) * 0.16F;
        duration += Math.max(0, formula.formCount() - 1) * 0.12F;
        range += Math.max(0, formula.formCount() - 1) * 0.10F;

        qiCost += Math.max(0, formula.sourceCount() - 1) * 0.28F;
        qiCost += Math.max(0, formula.intentCount() - 1) * 0.22F;
        qiCost += Math.max(0, formula.formCount() - 1) * 0.18F;
        backlash += Math.max(0, formula.grammaticalWeight() - 4) * 0.06F;

        if (formula.hasIntent("cut") || formula.hasIntent("pierce")) {
            damage += strength * 0.012F;
        }

        if (formula.hasSource("wind")
                || formula.hasSource("lightning")
                || formula.hasModifier("quicken")) {
            damage += agility * 0.008F;
            range += agility * 0.003F;
        }

        if (formula.hasIntent("guard")
                || formula.hasIntent("heal")
                || formula.hasForm("veil")) {
            duration += vitality * 0.008F;
        }

        // Basic source synergies. These are intentionally small; actual identity comes from rune order and selected form.
        if (formula.hasSource("flame") && formula.hasSource("wind")) {
            damage += 0.20F;
            range += 0.08F;
        }

        if (formula.hasSource("water") && formula.hasSource("frost")) {
            duration += 0.20F;
            backlash -= 0.05F;
        }

        if (formula.hasSource("earth") && formula.hasSource("metal")) {
            duration += 0.18F;
            damage += 0.08F;
        }

        if (formula.hasSource("life") && formula.hasSource("wood")) {
            duration += 0.25F;
            qiCost -= 0.10F;
        }

        if (formula.hasSource("light") && formula.hasSource("shadow")) {
            damage += 0.25F;
            backlash += 0.20F;
        }

        if (formula.hasModifier("violent")) {
            damage += 0.45F;
            backlash += 0.7F;
            qiCost += 0.35F;
        }

        if (formula.hasModifier("heavy")) {
            duration += 0.25F;
            qiCost += 0.2F;
        }

        if (formula.hasModifier("quicken")) {
            duration -= 0.2F;
            qiCost += 0.1F;
        }

        if (formula.hasModifier("stabilise")) {
            backlash -= 0.4F;
            damage -= 0.15F;
        }

        if (formula.hasModifier("hidden")) {
            damage -= 0.1F;
            qiCost += 0.15F;
        }

        RunicFormulaStats stats = new RunicFormulaStats(
                Math.max(0.5F, damage),
                Math.max(0.5F, duration),
                Math.max(0.5F, range),
                Math.max(0.5F, qiCost),
                Math.max(0.2F, backlash)
        );

        return applyMastery(formula, caster, stats);
    }

    private static RunicFormulaStats applyMastery(RunicFormula formula, LivingEntity caster, RunicFormulaStats stats) {
        if (formula == null || caster == null || !caster.hasData(ModAttachments.ENTITY_DATA)) {
            return stats;
        }

        RunicPlayerData runicData = RunicPathHelper.getRunicData(caster);
        RunicDiscoveredFormula discoveredFormula = runicData.getDiscoveredFormula(formula.getFormulaId());

        if (discoveredFormula == null) {
            return stats;
        }

        return discoveredFormula.getMasteryGrade().applyTo(stats);
    }

    private static float getStat(LivingEntity entity, Stat stat) {
        if (entity == null || !entity.hasData(ModAttachments.ENTITY_DATA)) {
            return 0.0F;
        }

        IEntityData entityData = entity.getData(ModAttachments.ENTITY_DATA);

        if (entityData.getActiveFormData() == null || entityData.getActiveFormData().getStatSheet() == null) {
            return 0.0F;
        }

        var statInstance = entityData.getActiveFormData().getStatSheet().getStatInstance(stat);

        if (statInstance == null) {
            return 0.0F;
        }

        return (float) statInstance.getValue();
    }
}
