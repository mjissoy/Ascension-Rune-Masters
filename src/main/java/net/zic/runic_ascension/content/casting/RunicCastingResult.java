package net.zic.runic_ascension.content.casting;

import net.minecraft.resources.ResourceLocation;

public class RunicCastingResult {

    private final boolean success;
    private final ResourceLocation sequenceId;
    private final String failureReason;
    private final boolean backlashHandled;

    private RunicCastingResult(boolean success, ResourceLocation sequenceId, String failureReason, boolean backlashHandled) {
        this.success = success;
        this.sequenceId = sequenceId;
        this.failureReason = failureReason;
        this.backlashHandled = backlashHandled;
    }

    public static RunicCastingResult success(ResourceLocation sequenceId) {
        return new RunicCastingResult(true, sequenceId, "", false);
    }

    public static RunicCastingResult failure(String failureReason) {
        return new RunicCastingResult(false, null, failureReason, false);
    }

    public static RunicCastingResult failure(String failureReason, boolean backlashHandled) {
        return new RunicCastingResult(false, null, failureReason, backlashHandled);
    }

    public boolean isSuccess() {
        return success;
    }

    public ResourceLocation getSequenceId() {
        return sequenceId;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public boolean hasHandledBacklash() {
        return backlashHandled;
    }
}
