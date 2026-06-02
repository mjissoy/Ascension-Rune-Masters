package net.zic.runic_ascension.content.casting;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.zic.runic_ascension.content.RunicPathHelper;
import net.zic.runic_ascension.content.RunicPlayerData;
import net.zic.runic_ascension.content.sequences.IRunicSequence;
import net.zic.runic_ascension.content.sequences.ModRunicSequences;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public final class RunicSequenceMatcher {

    private RunicSequenceMatcher() {
    }

    public static RunicCastingResult tryCast(LivingEntity caster, List<ResourceLocation> inputRunes) {
        return tryCast(caster, inputRunes, 0);
    }

    public static RunicCastingResult tryCast(LivingEntity caster, List<ResourceLocation> inputRunes, int selectedSuppressionRealm) {
        return tryCast(caster, inputRunes, ModRunicSequences.values(), selectedSuppressionRealm);
    }

    public static RunicCastingResult tryCast(
            LivingEntity caster,
            List<ResourceLocation> inputRunes,
            Collection<IRunicSequence> availableSequences
    ) {
        return tryCast(caster, inputRunes, availableSequences, 0);
    }

    public static RunicCastingResult tryCast(
            LivingEntity caster,
            List<ResourceLocation> inputRunes,
            Collection<IRunicSequence> availableSequences,
            int selectedSuppressionRealm
    ) {
        if (caster == null) {
            return RunicCastingResult.failure("missing_caster");
        }

        if (inputRunes == null || inputRunes.isEmpty()) {
            return RunicCastingResult.failure("empty_sequence");
        }

        if (!caster.hasData(ModAttachments.ENTITY_DATA)) {
            return RunicCastingResult.failure("missing_entity_data");
        }

        IEntityData entityData = caster.getData(ModAttachments.ENTITY_DATA);

        if (!RunicPathHelper.hasEnteredRunicPath(entityData)) {
            return RunicCastingResult.failure("not_on_runic_path");
        }

        int slotCount = RunicPathHelper.getRuneSlotCount(caster, true);

        if (inputRunes.size() > slotCount) {
            return RunicCastingResult.failure("formula_too_complex");
        }

        RunicPlayerData runicData = RunicPathHelper.getRunicData(caster);

        for (ResourceLocation runeId : inputRunes) {
            if (!runicData.knowsRune(runeId)) {
                return RunicCastingResult.failure("rune_beyond_comprehension");
            }

            if (!RunicPathHelper.canUseRune(entityData, runeId)) {
                return RunicCastingResult.failure("rune_beyond_comprehension");
            }
        }

        IRunicSequence sequence = findMatchingSequence(inputRunes, availableSequences);

        if (sequence == null) {
            RunicCastingResult formulaResult = RunicFormulaCaster.tryCast(caster, inputRunes, selectedSuppressionRealm);

            if (formulaResult.isSuccess()) {
                runicData.recordFormulaCast(formulaResult.getSequenceId(), inputRunes);
                RunicPathHelper.saveRunicData(caster, runicData);
                return formulaResult;
            }

            return formulaResult;
        }

        int majorRealm = RunicPathHelper.getRunicMajorRealm(entityData);

        if (majorRealm < sequence.getMinimumRunicRealm()) {
            return RunicCastingResult.failure("sequence_locked:" + sequence.getId());
        }

        if (!sequence.canCast(caster)) {
            return RunicCastingResult.failure("sequence_cannot_cast:" + sequence.getId());
        }

        if (!tryConsumeSequenceQi(caster, sequence.getQiCost())) {
            return RunicCastingResult.failure("not_enough_qi");
        }

        sequence.cast(caster);

        runicData.addDiscoveredSequence(sequence.getId());
        RunicPathHelper.saveRunicData(caster, runicData);

        return RunicCastingResult.success(sequence.getId());
    }

    private static boolean tryConsumeSequenceQi(LivingEntity caster, double qiCost) {
        if (caster == null || !caster.hasData(ModAttachments.ENTITY_DATA)) {
            return false;
        }

        IEntityData entityData = caster.getData(ModAttachments.ENTITY_DATA);
        return entityData.getQiContainer().tryConsumeQi(qiCost);
    }

    public static IRunicSequence findMatchingSequence(
            List<ResourceLocation> inputRunes,
            Collection<IRunicSequence> availableSequences
    ) {
        if (inputRunes == null || availableSequences == null) {
            return null;
        }

        return availableSequences.stream()
                .filter(sequence -> matches(inputRunes, sequence))
                .max(Comparator.comparingInt(sequence -> sequence.getRequiredRunes().size()))
                .orElse(null);
    }

    public static boolean matches(List<ResourceLocation> inputRunes, IRunicSequence sequence) {
        List<ResourceLocation> requiredRunes = sequence.getRequiredRunes();

        if (inputRunes.size() != requiredRunes.size()) {
            return false;
        }

        if (sequence.isOrderSensitive()) {
            return inputRunes.equals(requiredRunes);
        }

        List<ResourceLocation> inputCopy = new ArrayList<>(inputRunes);
        List<ResourceLocation> requiredCopy = new ArrayList<>(requiredRunes);

        inputCopy.sort(Comparator.comparing(ResourceLocation::toString));
        requiredCopy.sort(Comparator.comparing(ResourceLocation::toString));

        return inputCopy.equals(requiredCopy);
    }
}