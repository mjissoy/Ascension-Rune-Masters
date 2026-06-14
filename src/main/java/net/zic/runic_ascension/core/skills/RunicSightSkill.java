package net.zic.runic_ascension.core.skills;

import net.lucent.easygui.gui.RenderableElement;
import net.lucent.easygui.gui.UIFrame;
import net.lucent.easygui.gui.textures.ITextureData;
import net.lucent.easygui.gui.textures.TextureData;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.gui.elements.info_elements.DescriptionDisplayContainer;
import net.thejadeproject.ascension.refactor_packages.network.client_bound.toast.ShowAscensionToast;
import net.thejadeproject.ascension.refactor_packages.physiques.IPhysiqueData;
import net.thejadeproject.ascension.refactor_packages.skill_casting.casting.CastEndData;
import net.thejadeproject.ascension.refactor_packages.skill_casting.casting.CastResult;
import net.thejadeproject.ascension.refactor_packages.skills.IPersistentSkillData;
import net.thejadeproject.ascension.refactor_packages.skills.castable.CastType;
import net.thejadeproject.ascension.refactor_packages.skills.castable.ICastData;
import net.thejadeproject.ascension.refactor_packages.skills.castable.ICastableSkill;
import net.thejadeproject.ascension.refactor_packages.skills.castable.IPreCastData;
import net.zic.runic_ascension.RunicAscension;
import net.zic.runic_ascension.content.RunicLearningState;
import net.zic.runic_ascension.content.RunicPathHelper;
import net.zic.runic_ascension.content.RunicPlayerData;
import net.zic.runic_ascension.content.runes.IRunicRune;
import net.zic.runic_ascension.content.runes.ModRunicRunes;
import net.zic.runic_ascension.core.skills.data.EmptyRunicCastData;
import net.zic.runic_ascension.core.skills.data.EmptyRunicPreCastData;
import net.zic.runic_ascension.util.runic_sight.RunicSightResolver;
import net.zic.runic_ascension.util.runic_sight.RunicSightTrace;

import java.util.ArrayList;
import java.util.List;

public class RunicSightSkill implements ICastableSkill {

    private static final double QI_COST = 4.0D;
    private static final double REACH = 12.0D;
    private static final int COOLDOWN_TICKS = 40;
    private static final float BASE_OBSERVATION_GAIN = 0.20F;

    @Override
    public CastResult canCast(Entity caster, IPreCastData preCastData) {
        if (!(caster instanceof ServerPlayer player)) {
            return new CastResult(CastResult.Type.FAILURE);
        }

        if (!player.hasData(ModAttachments.ENTITY_DATA)) {
            return new CastResult(CastResult.Type.FAILURE);
        }

        IEntityData entityData = player.getData(ModAttachments.ENTITY_DATA);

        if (!RunicPathHelper.hasEnteredRunicPath(entityData)) {
            return new CastResult(CastResult.Type.FAILURE);
        }

        if (!entityData.getQiContainer().hasQi(QI_COST)) {
            return new CastResult(CastResult.Type.FAILURE);
        }

        return new CastResult(CastResult.Type.SUCCESS);
    }

    @Override
    public void initialCast(Entity caster, IPreCastData preCastData) {
        if (!(caster instanceof ServerPlayer player)) return;
        if (player.level().isClientSide()) return;
        if (!player.hasData(ModAttachments.ENTITY_DATA)) return;

        IEntityData entityData = player.getData(ModAttachments.ENTITY_DATA);

        if (!entityData.getQiContainer().tryConsumeQi(QI_COST)) {
            return;
        }

        RunicSightScan scan = findRunesInSight(player, entityData);

        if (scan.traces().isEmpty()) {
            player.displayClientMessage(
                    Component.translatable("runic_ascension.runic.sight.no_traces").withStyle(ChatFormatting.DARK_PURPLE),
                    true
            );
            return;
        }

        RunicPlayerData runicData = RunicPathHelper.getRunicData(player);
        List<ResourceLocation> newlyObserved = new ArrayList<>();
        List<ResourceLocation> glimpsed = new ArrayList<>();
        List<ResourceLocation> alreadyClear = new ArrayList<>();

        for (RunicSightTrace trace : scan.readableTraces()) {
            ResourceLocation runeId = trace.runeId();
            RunicLearningState state = runicData.getLearningState(runeId);

            if (state == RunicLearningState.KNOWN || state == RunicLearningState.OBSERVED) {
                alreadyClear.add(runeId);
                continue;
            }

            float oldProgress = runicData.getObservationProgress().getOrDefault(runeId, 0.0F);
            float gain = getObservationGain(entityData, trace);
            float newProgress = Math.min(1.0F, oldProgress + gain);

            runicData.addGlimpsedRune(runeId);
            runicData.setObservationProgress(runeId, newProgress);
            glimpsed.add(runeId);

            if (newProgress >= 1.0F) {
                runicData.addObservedRune(runeId);
                newlyObserved.add(runeId);
            }
        }

        if (!newlyObserved.isEmpty() || !glimpsed.isEmpty()) {
            RunicPathHelper.saveRunicData(player, runicData);
        }

        if (!newlyObserved.isEmpty()) {
            ResourceLocation first = newlyObserved.get(0);
            Component firstName = runeName(first);

            PacketDistributor.sendToPlayer(player, new ShowAscensionToast(
                    "Rune Observed",
                    readableRuneName(first) + " has become clear.",
                    ItemStack.EMPTY,
                    ResourceLocation.fromNamespaceAndPath("minecraft", "textures/gui/toasts.png")
            ));

            player.displayClientMessage(
                    observedMessage(firstName, newlyObserved.size(), scan.veiledCount()),
                    true
            );
            return;
        }

        if (!glimpsed.isEmpty()) {
            ResourceLocation first = glimpsed.get(0);
            float progress = runicData.getObservationProgress().getOrDefault(first, 0.0F);

            player.displayClientMessage(
                    glimpsedMessage(runeName(first), progress, glimpsed.size(), scan.veiledCount()),
                    true
            );
            return;
        }

        if (scan.veiledCount() > 0 && alreadyClear.isEmpty()) {
            player.displayClientMessage(
                    Component.translatable("runic_ascension.runic.sight.too_deep", scan.veiledCount()).withStyle(ChatFormatting.DARK_PURPLE),
                    true
            );
            return;
        }

        if (!alreadyClear.isEmpty()) {
            player.displayClientMessage(
                    Component.translatable("runic_ascension.runic.sight.already_clear", runeName(alreadyClear.get(0))).withStyle(ChatFormatting.LIGHT_PURPLE),
                    true
            );
            return;
        }

        player.displayClientMessage(
                Component.translatable("runic_ascension.runic.sight.no_readable_traces").withStyle(ChatFormatting.DARK_PURPLE),
                true
        );
    }

    private RunicSightScan findRunesInSight(ServerPlayer player, IEntityData entityData) {
        Vec3 eyePosition = player.getEyePosition();
        Vec3 viewVector = player.getViewVector(1.0F);
        Vec3 endPosition = eyePosition.add(viewVector.scale(REACH));

        HitResult blockHit = player.pick(REACH, 0.0F, false);

        AABB searchBox = player.getBoundingBox()
                .expandTowards(viewVector.scale(REACH))
                .inflate(1.0D);

        EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(
                player.level(),
                player,
                eyePosition,
                endPosition,
                searchBox,
                entity -> entity != player
                        && !entity.isSpectator()
                        && entity.isPickable()
        );

        if (entityHit != null) {
            boolean blockedByBlock = blockHit.getType() != HitResult.Type.MISS
                    && eyePosition.distanceToSqr(blockHit.getLocation()) < eyePosition.distanceToSqr(entityHit.getLocation());

            if (!blockedByBlock) {
                return buildScan(RunicSightResolver.getTracesForEntity(entityHit.getEntity()), entityData);
            }
        }

        if (blockHit instanceof BlockHitResult blockResult && blockHit.getType() != HitResult.Type.MISS) {
            BlockState state = player.level().getBlockState(blockResult.getBlockPos());
            return buildScan(RunicSightResolver.getTracesForBlock(state), entityData);
        }

        return RunicSightScan.empty();
    }

    private RunicSightScan buildScan(List<RunicSightTrace> traces, IEntityData entityData) {
        List<RunicSightTrace> readable = new ArrayList<>();
        int veiled = 0;

        for (RunicSightTrace trace : traces) {
            if (canReadTrace(entityData, trace)) {
                readable.add(trace);
            } else {
                veiled++;
            }
        }

        return new RunicSightScan(List.copyOf(traces), List.copyOf(readable), veiled);
    }

    private boolean canReadTrace(IEntityData entityData, RunicSightTrace trace) {
        if (!RunicPathHelper.canObserveRune(entityData, trace.runeId())) {
            return false;
        }

        int realm = RunicPathHelper.getRunicMajorRealm(entityData);
        int layerRequirement = switch (trace.layer()) {
            case SURFACE -> 0;
            case DEEP -> 1;
            case HIDDEN -> 3;
        };

        return realm >= layerRequirement;
    }

    private float getObservationGain(IEntityData entityData, RunicSightTrace trace) {
        int realm = RunicPathHelper.getRunicMajorRealm(entityData);
        IRunicRune rune = ModRunicRunes.get(trace.runeId());

        float gain = BASE_OBSERVATION_GAIN + (Math.max(0, realm) * 0.025F);

        if (rune != null) {
            gain *= switch (rune.getDepth()) {
                case SURFACE -> 1.0F;
                case DEEP -> 0.85F;
                case HIDDEN -> 0.65F;
            };
        }

        gain *= Math.max(0.25F, trace.focusGainMultiplier());
        return Math.max(0.08F, Math.min(0.50F, gain));
    }

    private Component observedMessage(Component firstName, int observedCount, int veiledCount) {
        int extraObserved = Math.max(0, observedCount - 1);

        if (veiledCount > 0) {
            return Component.translatable(
                    "runic_ascension.runic.sight.observed_with_veiled",
                    firstName,
                    extraObserved,
                    veiledCount
            ).withStyle(ChatFormatting.LIGHT_PURPLE);
        }

        if (extraObserved > 0) {
            return Component.translatable(
                    "runic_ascension.runic.sight.observed_extra",
                    firstName,
                    extraObserved
            ).withStyle(ChatFormatting.LIGHT_PURPLE);
        }

        return Component.translatable("runic_ascension.runic.sight.observed", firstName).withStyle(ChatFormatting.LIGHT_PURPLE);
    }

    private Component glimpsedMessage(Component firstName, float progress, int glimpsedCount, int veiledCount) {
        int progressPercent = Math.round(progress * 100.0F);
        int extraGlimpsed = Math.max(0, glimpsedCount - 1);

        if (veiledCount > 0) {
            return Component.translatable(
                    "runic_ascension.runic.sight.glimpsed_with_veiled",
                    firstName,
                    progressPercent,
                    extraGlimpsed,
                    veiledCount
            ).withStyle(ChatFormatting.LIGHT_PURPLE);
        }

        if (extraGlimpsed > 0) {
            return Component.translatable(
                    "runic_ascension.runic.sight.glimpsed_extra",
                    firstName,
                    progressPercent,
                    extraGlimpsed
            ).withStyle(ChatFormatting.LIGHT_PURPLE);
        }

        return Component.translatable("runic_ascension.runic.sight.glimpsed", firstName, progressPercent).withStyle(ChatFormatting.LIGHT_PURPLE);
    }

    private Component runeName(ResourceLocation runeId) {
        IRunicRune rune = ModRunicRunes.get(runeId);
        return rune == null ? Component.literal(readableRuneName(runeId)) : rune.getName();
    }

    private String readableRuneName(ResourceLocation runeId) {
        String path = runeId.getPath().replace("_", " ");

        if (path.isEmpty()) {
            return runeId.toString();
        }

        return path.substring(0, 1).toUpperCase() + path.substring(1);
    }

    @Override
    public int getCooldown(CastEndData castEndData) {
        return COOLDOWN_TICKS;
    }

    @Override
    public CastType getCastType() {
        return CastType.INSTANT;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ITextureData getIcon(IEntityData entityData) {
        return new TextureData(
                ResourceLocation.fromNamespaceAndPath(
                        RunicAscension.MOD_ID,
                        "textures/spells/icon/runic_sight.png"
                ),
                16,
                16
        );
    }

    @Override
    public Component getTitle(IEntityData entityData) {
        return Component.translatable("runic_ascension.skill.runic_sight");
    }

    @Override
    public Component getDescription(IEntityData entityData) {
        return Component.translatable("runic_ascension.skill.runic_sight.description");
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public RenderableElement getInformationContainer(UIFrame frame, IEntityData entityData) {
        return new DescriptionDisplayContainer(frame, getTitle(entityData), getDescription(entityData));
    }

    @Override public void finalCast(CastEndData reason, Entity caster, ICastData castData) {}
    @Override public boolean continueCasting(int ticksElapsed, Entity caster, ICastData castData) { return false; }
    @Override public void onEquip(IEntityData entityData) {}
    @Override public void onUnEquip(IEntityData entityData, IPreCastData preCastData) {}
    @Override public void selected(IEntityData entityData) {}
    @Override public void unselected(IEntityData entityData) {}

    @Override public IPreCastData freshPreCastData() { return new EmptyRunicPreCastData(); }
    @Override public IPreCastData preCastDataFromCompound(CompoundTag tag) { return new EmptyRunicPreCastData(); }
    @Override public IPreCastData preCastDataFromNetwork(RegistryFriendlyByteBuf buf) { return new EmptyRunicPreCastData(); }

    @Override public ICastData freshCastData() { return new EmptyRunicCastData(); }
    @Override public ICastData castDataFromCompound(CompoundTag tag) { return new EmptyRunicCastData(); }
    @Override public ICastData castDataFromNetwork(RegistryFriendlyByteBuf buf) { return new EmptyRunicCastData(); }

    @Override public IPersistentSkillData freshPersistentInstance() { return null; }
    @Override public IPersistentSkillData persistentInstanceFromCompound(CompoundTag tag) { return null; }
    @Override public IPersistentSkillData persistentInstanceFromNetwork(RegistryFriendlyByteBuf buf) { return null; }

    @OnlyIn(Dist.CLIENT)
    @Override public RenderableElement getCastElement(UIFrame frame) { return null; }

    @Override public void onAdded(IEntityData attachedEntityData) {}
    @Override public void onRemoved(IEntityData attachedEntityData, IPersistentSkillData persistentData) {}
    @Override public void onFormAdded(IEntityData heldEntity, ResourceLocation form, IPhysiqueData physiqueData) {}
    @Override public void onFormRemoved(IEntityData heldEntity, ResourceLocation form, IPhysiqueData physiqueData) {}
    @Override public void finishedCooldown(IEntityData attachedEntityData, String identifier) {}

    @Override public IPersistentSkillData freshPersistentData(IEntityData heldEntity) { return null; }
    @Override public IPersistentSkillData fromCompound(CompoundTag tag, IEntityData heldEntity) { return null; }
    @Override public IPersistentSkillData fromNetwork(RegistryFriendlyByteBuf buf) { return null; }

    private record RunicSightScan(
            List<RunicSightTrace> traces,
            List<RunicSightTrace> readableTraces,
            int veiledCount
    ) {
        private static RunicSightScan empty() {
            return new RunicSightScan(List.of(), List.of(), 0);
        }
    }
}
