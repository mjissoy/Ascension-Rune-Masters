package net.zic.runic_ascension.core.skills.active.defense;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.skill_casting.casting.CastEndData;
import net.thejadeproject.ascension.refactor_packages.skill_casting.casting.CastResult;
import net.thejadeproject.ascension.refactor_packages.skills.castable.CastType;
import net.thejadeproject.ascension.refactor_packages.skills.castable.ICastData;
import net.thejadeproject.ascension.refactor_packages.skills.castable.IPreCastData;
import net.zic.runic_ascension.content.runes.ModRunicRunes;
import net.zic.runic_ascension.core.skills.active.AbstractRunicActiveSkill;
import net.zic.runic_ascension.util.RunicInscriptionHelper;

public class WaterMantle extends AbstractRunicActiveSkill {

    private static final double START_QI_COST = 8.0D;
    private static final double QI_COST_PER_SECOND = 5.0D;
    private static final int COOLDOWN_TICKS = 70;

    private static final double BASE_RADIUS = 2.2D;

    @Override
    protected String translationKey() {
        return "runic_ascension.skill.water_mantle";
    }

    @Override
    public CastResult canCast(Entity caster, IPreCastData preCastData) {
        return requireRunicCaster(
                caster,
                START_QI_COST,
                ModRunicRunes.WATER_RUNE.getId(),
                ModRunicRunes.HEAL_RUNE.getId()
        );
    }

    @Override
    public void initialCast(Entity caster, IPreCastData preCastData) {
        if (!(caster instanceof ServerPlayer player)) return;
        if (player.level().isClientSide()) return;

        if (!tryConsumeQi(player, START_QI_COST)) return;

        player.setRemainingFireTicks(0);
        spawnMantle(player, 0);
    }

    @Override
    public boolean continueCasting(int ticksElapsed, Entity caster, ICastData castData) {
        if (!(caster instanceof ServerPlayer player)) return false;
        if (!player.hasData(ModAttachments.ENTITY_DATA)) return false;

        if (!player.getData(ModAttachments.INPUT_STATES).isHeld("skill_cast")) {
            return false;
        }

        if (ticksElapsed >= scaledDurationTicks(player, 120, 35)) {
            return false;
        }

        IEntityData entityData = player.getData(ModAttachments.ENTITY_DATA);

        if (ticksElapsed > 0 && ticksElapsed % 20 == 0) {
            if (!entityData.getQiContainer().tryConsumeQi(QI_COST_PER_SECOND)) {
                return false;
            }
        }

        tickMantle(player, ticksElapsed);
        return true;
    }

    private void tickMantle(ServerPlayer player, int ticksElapsed) {
        ServerLevel level = player.serverLevel();
        int realm = runicRealm(player);
        double radius = BASE_RADIUS + realm * 0.12D;

        Vec3 movement = player.getDeltaMovement();
        player.setDeltaMovement(movement.x * 0.55D, movement.y, movement.z * 0.55D);
        player.hurtMarked = true;

        player.setRemainingFireTicks(0);
        player.fallDistance = Math.min(player.fallDistance, 2.0F);

        if (ticksElapsed > 0 && ticksElapsed % 20 == 0) {
            float healing = (3.0F + realm * 1.5F) * RunicInscriptionHelper.waterMantleHealingMultiplier(player);
            player.heal(healing);
            RunicInscriptionHelper.afterWaterMantleHeal(player);
        }

        if (ticksElapsed % 4 == 0) {
            spawnMantle(player, ticksElapsed);
        }

        if (ticksElapsed % 5 == 0) {
            AABB area = player.getBoundingBox().inflate(radius);

            for (Projectile projectile : level.getEntitiesOfClass(
                    Projectile.class,
                    area,
                    projectile -> projectile.isAlive() && projectile.getOwner() != player
            )) {
                projectile.setDeltaMovement(projectile.getDeltaMovement().scale(0.45D));
            }
        }
    }

    private void spawnMantle(ServerPlayer player, int ticksElapsed) {
        ServerLevel level = player.serverLevel();
        int realm = runicRealm(player);
        double radius = BASE_RADIUS + realm * 0.12D;
        int points = 20;

        for (int i = 0; i < points; i++) {
            double angle = ((Math.PI * 2.0D) / points) * i - ticksElapsed * 0.045D;
            double x = player.getX() + Math.cos(angle) * radius;
            double z = player.getZ() + Math.sin(angle) * radius;
            double y = player.getY() + 0.55D + Math.sin(angle + ticksElapsed * 0.08D) * 0.25D;

            level.sendParticles(
                    ParticleTypes.SPLASH,
                    x,
                    y,
                    z,
                    1,
                    0.02D,
                    0.02D,
                    0.02D,
                    0.0D
            );
        }
    }

    @Override public void finalCast(CastEndData reason, Entity caster, ICastData castData) {}
    @Override public int getCooldown(CastEndData castEndData) { return COOLDOWN_TICKS; }
    @Override public CastType getCastType() { return CastType.LONG; }
}