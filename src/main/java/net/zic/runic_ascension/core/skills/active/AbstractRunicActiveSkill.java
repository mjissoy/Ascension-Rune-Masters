package net.zic.runic_ascension.core.skills.active;

import net.lucent.easygui.gui.RenderableElement;
import net.lucent.easygui.gui.UIFrame;
import net.lucent.easygui.gui.textures.ITextureData;
import net.lucent.easygui.gui.textures.TextureData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.gui.elements.info_elements.DescriptionDisplayContainer;
import net.thejadeproject.ascension.refactor_packages.handlers.AscensionDamageHandler;
import net.thejadeproject.ascension.refactor_packages.paths.data.IPathData;
import net.thejadeproject.ascension.refactor_packages.physiques.IPhysiqueData;
import net.thejadeproject.ascension.refactor_packages.skill_casting.casting.CastResult;
import net.thejadeproject.ascension.refactor_packages.skills.IPersistentSkillData;
import net.thejadeproject.ascension.refactor_packages.skills.castable.ICastData;
import net.thejadeproject.ascension.refactor_packages.skills.castable.ICastableSkill;
import net.thejadeproject.ascension.refactor_packages.skills.castable.IPreCastData;
import net.zic.runic_ascension.RunicAscension;
import net.zic.runic_ascension.content.RunicPathHelper;
import net.zic.runic_ascension.core.paths.RunicPaths;
import net.zic.runic_ascension.core.skills.data.EmptyRunicCastData;
import net.zic.runic_ascension.core.skills.data.EmptyRunicPreCastData;

import java.util.Comparator;
import java.util.HashSet;

public abstract class AbstractRunicActiveSkill implements ICastableSkill {
    protected abstract String translationKey();
    protected String iconPath() {
        return "textures/spells/icon/placeholder.png";
    }

    protected CastResult requireRunicCaster(Entity caster, double qiCost, ResourceLocation... requiredRunes) {
        if (!(caster instanceof ServerPlayer player)) {return new CastResult(CastResult.Type.FAILURE);}
        if (!player.hasData(ModAttachments.ENTITY_DATA)) {return new CastResult(CastResult.Type.FAILURE);}

        IEntityData entityData = player.getData(ModAttachments.ENTITY_DATA);

        if (!RunicPathHelper.hasEnteredRunicPath(entityData)) {return new CastResult(CastResult.Type.FAILURE);}
        if (!entityData.getQiContainer().hasQi(qiCost)) {return new CastResult(CastResult.Type.FAILURE);}

        for (ResourceLocation runeId : requiredRunes) {
            if (!RunicPathHelper.canUseRune(player, runeId)) {return new CastResult(CastResult.Type.FAILURE);}
        }

        return new CastResult(CastResult.Type.SUCCESS);
    }

    protected boolean tryConsumeQi(ServerPlayer player, double qiCost) {
        if (!player.hasData(ModAttachments.ENTITY_DATA)) {return false;}

        return player.getData(ModAttachments.ENTITY_DATA)
                .getQiContainer()
                .tryConsumeQi(qiCost);
    }

    protected int runicRealm(ServerPlayer player) {
        if (!player.hasData(ModAttachments.ENTITY_DATA)) {
            return 0;
        }

        return RunicPathHelper.getRunicMajorRealm(player.getData(ModAttachments.ENTITY_DATA));
    }

    protected AscensionDamageHandler.AscensionDamageSource runicDamageSource(ServerPlayer player) {
        return new AscensionDamageHandler.AscensionDamageSource(
                new HashSet<>() {{
                    add(RunicPaths.RUNIC.getId());
                }},
                player.damageSources().source(
                        player.damageSources().magic().typeHolder().getKey(),
                        player
                )
        );
    }

    protected float calculateRunicAttackDamage(ServerPlayer player, float baseDamage, float attackMultiplier, float realmFlatBonus) {
        if (!player.hasData(ModAttachments.ENTITY_DATA)) {
            return baseDamage;
        }

        IEntityData entityData = player.getData(ModAttachments.ENTITY_DATA);
        IPathData runicData = entityData.getPathData(RunicPaths.RUNIC.getId());

        int major = runicData != null ? runicData.getMajorRealm() : 0;
        int minor = runicData != null ? runicData.getMinorRealm() : 0;

        float attackDamage = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);

        float multiplier = attackMultiplier + major * 0.45F + minor * 0.045F;
        float flatRealmDamage = major * realmFlatBonus + minor * 0.8F;

        return baseDamage + attackDamage * multiplier + flatRealmDamage;
    }

    protected LivingEntity findSoftLookTarget(ServerPlayer player, double range, double radius) {
        Vec3 eye = player.getEyePosition();
        Vec3 look = player.getLookAngle().normalize();
        Vec3 end = eye.add(look.scale(range));

        AABB searchBox = player.getBoundingBox()
                .expandTowards(look.scale(range))
                .inflate(radius);

        return player.serverLevel().getEntitiesOfClass(
                        LivingEntity.class,
                        searchBox,
                        entity -> entity != player && entity.isAlive() && !entity.isSpectator()
                )
                .stream()
                .filter(entity -> {
                    Vec3 center = entity.getBoundingBox().getCenter();
                    Vec3 toTarget = center.subtract(eye);
                    double along = toTarget.dot(look);

                    if (along < 0.0D || along > range) {
                        return false;
                    }

                    Vec3 closestPoint = eye.add(look.scale(along));
                    double distanceToRay = center.distanceTo(closestPoint);

                    return distanceToRay <= radius + entity.getBbWidth() * 0.5D;
                })
                .min(Comparator.comparingDouble(entity -> entity.distanceToSqr(player)))
                .orElse(null);
    }

    protected int scaledDurationTicks(ServerPlayer player, int baseTicks, int ticksPerRealm) {
        return baseTicks + runicRealm(player) * ticksPerRealm;
    }

    @Override public IPreCastData freshPreCastData() { return new EmptyRunicPreCastData(); }
    @Override public IPreCastData preCastDataFromCompound(CompoundTag tag) { return new EmptyRunicPreCastData(); }
    @Override public IPreCastData preCastDataFromNetwork(RegistryFriendlyByteBuf buf) { return new EmptyRunicPreCastData(); }

    @Override public ICastData freshCastData() { return new EmptyRunicCastData(); }
    @Override public ICastData castDataFromCompound(CompoundTag tag) { return new EmptyRunicCastData(); }
    @Override public ICastData castDataFromNetwork(RegistryFriendlyByteBuf buf) { return new EmptyRunicCastData(); }

    @Override public IPersistentSkillData freshPersistentInstance() { return null; }
    @Override public IPersistentSkillData persistentInstanceFromCompound(CompoundTag tag) { return null; }
    @Override public IPersistentSkillData persistentInstanceFromNetwork(RegistryFriendlyByteBuf buf) { return null; }

    @Override public void onEquip(IEntityData entityData) {}
    @Override public void onUnEquip(IEntityData entityData, IPreCastData preCastData) {}
    @Override public void selected(IEntityData entityData) {}
    @Override public void unselected(IEntityData entityData) {}
    @Override public void onAdded(IEntityData attachedEntityData) {}
    @Override public void onRemoved(IEntityData attachedEntityData, IPersistentSkillData persistentData) {}
    @Override public void onFormAdded(IEntityData heldEntity, ResourceLocation form, IPhysiqueData physiqueData) {}
    @Override public void onFormRemoved(IEntityData heldEntity, ResourceLocation form, IPhysiqueData physiqueData) {}
    @Override public void finishedCooldown(IEntityData attachedEntityData, String identifier) {}
    @Override public IPersistentSkillData freshPersistentData(IEntityData heldEntity) { return null; }
    @Override public IPersistentSkillData fromCompound(CompoundTag tag, IEntityData heldEntity) { return null; }
    @Override public IPersistentSkillData fromNetwork(RegistryFriendlyByteBuf buf) { return null; }

    @OnlyIn(Dist.CLIENT)
    @Override public RenderableElement getCastElement(UIFrame frame) { return null; }

    @Override
    public Component getTitle(IEntityData entityData) {
        return Component.translatable(translationKey());
    }

    @Override
    public Component getDescription(IEntityData entityData) {
        return Component.translatable(translationKey() + ".description");
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ITextureData getIcon(IEntityData entityData) {
        return new TextureData(
                ResourceLocation.fromNamespaceAndPath(RunicAscension.MOD_ID, iconPath()),
                16,
                16
        );
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public RenderableElement getInformationContainer(UIFrame frame, IEntityData entityData) {
        return new DescriptionDisplayContainer(frame, getTitle(entityData), getDescription(entityData));
    }
}