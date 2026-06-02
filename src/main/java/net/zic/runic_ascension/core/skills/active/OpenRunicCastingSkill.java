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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.gui.elements.info_elements.DescriptionDisplayContainer;
import net.thejadeproject.ascension.refactor_packages.physiques.IPhysiqueData;
import net.thejadeproject.ascension.refactor_packages.skill_casting.casting.CastEndData;
import net.thejadeproject.ascension.refactor_packages.skill_casting.casting.CastResult;
import net.thejadeproject.ascension.refactor_packages.skills.IPersistentSkillData;
import net.thejadeproject.ascension.refactor_packages.skills.castable.CastType;
import net.thejadeproject.ascension.refactor_packages.skills.castable.ICastData;
import net.thejadeproject.ascension.refactor_packages.skills.castable.ICastableSkill;
import net.thejadeproject.ascension.refactor_packages.skills.castable.IPreCastData;
import net.zic.runic_ascension.RunicAscension;
import net.zic.runic_ascension.content.RunicPathHelper;
import net.zic.runic_ascension.core.skills.data.EmptyRunicCastData;
import net.zic.runic_ascension.core.skills.data.EmptyRunicPreCastData;
import net.zic.runic_ascension.network.client_bound.OpenRunicCastingScreenPayload;

public class OpenRunicCastingSkill implements ICastableSkill {

    private static final int COOLDOWN_TICKS = 20;

    @Override
    public CastResult canCast(Entity caster, IPreCastData preCastData) {
        if (!(caster instanceof ServerPlayer player)) {
            return new CastResult(CastResult.Type.FAILURE);
        }

        if (!player.hasData(ModAttachments.ENTITY_DATA)) {
            return new CastResult(
                    CastResult.Type.FAILURE,
                    Component.translatable("runic_ascension.runic.cast.no_entity_data")
            );
        }

        IEntityData entityData = player.getData(ModAttachments.ENTITY_DATA);

        if (!RunicPathHelper.hasEnteredRunicPath(entityData)) {
            return new CastResult(
                    CastResult.Type.FAILURE,
                    Component.translatable("runic_ascension.runic.cast.not_on_path")
            );
        }

        if (!(caster instanceof LivingEntity livingCaster)) {
            return new CastResult(
                    CastResult.Type.FAILURE,
                    Component.translatable("runic_ascension.runic.cast.invalid_caster")
            );
        }

        if (RunicPathHelper.getUsableKnownRunes(livingCaster).isEmpty()) {
            return new CastResult(
                    CastResult.Type.FAILURE,
                    Component.translatable("runic_ascension.runic.cast.no_usable_runes")
            );
        }

        return new CastResult(CastResult.Type.SUCCESS);
    }

    @Override
    public void initialCast(Entity caster, IPreCastData preCastData) {
        if (!(caster instanceof ServerPlayer player)) return;
        if (player.level().isClientSide()) return;

        OpenRunicCastingScreenPayload.sendTo(player);
    }

    @Override public void onEquip(IEntityData entityData) {}
    @Override public void onUnEquip(IEntityData entityData, IPreCastData preCastData) {}
    @Override public void finalCast(CastEndData reason, Entity caster, ICastData castData) {}
    @Override public boolean continueCasting(int ticksElapsed, Entity caster, ICastData castData) { return false; }
    @Override public int getCooldown(CastEndData castEndData) { return COOLDOWN_TICKS; }
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

    @Override public CastType getCastType() { return CastType.INSTANT; }

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

    @OnlyIn(Dist.CLIENT)
    @Override
    public ITextureData getIcon(IEntityData entityData) {
        return new TextureData(
                ResourceLocation.fromNamespaceAndPath(
                        RunicAscension.MOD_ID,
                        "textures/spells/icon/placeholder.png"
                ),
                16,
                16
        );
    }

    @Override
    public Component getTitle(IEntityData entityData) {
        return Component.translatable("runic_ascension.skill.open_runic_casting");
    }

    @Override
    public Component getDescription(IEntityData entityData) {
        return Component.translatable("runic_ascension.skill.open_runic_casting.description");
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public RenderableElement getInformationContainer(UIFrame frame, IEntityData entityData) {
        return new DescriptionDisplayContainer(frame, getTitle(entityData), getDescription(entityData));
    }
}