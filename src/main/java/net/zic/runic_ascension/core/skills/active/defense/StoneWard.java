package net.zic.runic_ascension.core.skills.active.defense;

import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.block.Blocks;
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

public class StoneWard extends AbstractRunicActiveSkill {

    private static final double START_QI_COST = 8.0D;
    private static final double QI_COST_PER_SECOND = 4.0D;
    private static final int COOLDOWN_TICKS = 60;

    private static final double BASE_RADIUS = 2.4D;

    @Override
    protected String translationKey() {
        return "runic_ascension.skill.stone_ward";
    }

    @Override
    public CastResult canCast(Entity caster, IPreCastData preCastData) {
        return requireRunicCaster(
                caster,
                START_QI_COST,
                ModRunicRunes.EARTH_RUNE.getId(),
                ModRunicRunes.GUARD_RUNE.getId()
        );
    }

    @Override
    public void initialCast(Entity caster, IPreCastData preCastData) {
        if (!(caster instanceof ServerPlayer player)) return;
        if (player.level().isClientSide()) return;

        if (!tryConsumeQi(player, START_QI_COST)) return;

        spawnStoneRing(player, 0);
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

        tickWard(player, ticksElapsed);
        return true;
    }

    private void tickWard(ServerPlayer player, int ticksElapsed) {
        ServerLevel level = player.serverLevel();
        int realm = runicRealm(player);
        double radius = BASE_RADIUS + realm * 0.15D + RunicInscriptionHelper.stoneWardRadiusBonus(player);

        rootPlayer(player);

        if (ticksElapsed % 4 == 0) {
            spawnStoneRing(player, ticksElapsed);
        }

        if (ticksElapsed % 2 == 0) {
            AABB area = player.getBoundingBox().inflate(radius);

            for (LivingEntity entity : level.getEntitiesOfClass(
                    LivingEntity.class,
                    area,
                    entity -> entity != player && entity.isAlive()
            )) {
                Vec3 away = entity.position().subtract(player.position());

                if (away.lengthSqr() < 0.001D) {
                    continue;
                }

                double pushPower = (0.32D + realm * 0.015D) * RunicInscriptionHelper.stoneWardPushMultiplier(player);
                Vec3 push = away.normalize().scale(pushPower);
                entity.push(push.x, 0.06D, push.z);
            }

            AABB projectileArea = player.getBoundingBox().inflate(radius + 1.2D);

            for (Projectile projectile : level.getEntitiesOfClass(
                    Projectile.class,
                    projectileArea,
                    projectile -> projectile.isAlive() && projectile.getOwner() != player
            )) {
                Vec3 away = projectile.position().subtract(player.position());

                if (away.lengthSqr() < 0.001D) {
                    projectile.discard();
                    continue;
                }

                Vec3 awayNormal = away.normalize();
                Vec3 currentMotion = projectile.getDeltaMovement();

                double speed = Math.max(1.15D, currentMotion.length());
                projectile.setDeltaMovement(awayNormal.scale(speed * 1.25D).add(0.0D, 0.08D, 0.0D));
                projectile.setOwner(player);
                projectile.hasImpulse = true;
                projectile.hurtMarked = true;

                level.sendParticles(
                        ParticleTypes.CRIT,
                        projectile.getX(),
                        projectile.getY(),
                        projectile.getZ(),
                        5,
                        0.08D,
                        0.08D,
                        0.08D,
                        0.02D
                );
            }
        }
    }

    private void rootPlayer(ServerPlayer player) {
        Vec3 movement = player.getDeltaMovement();
        player.setDeltaMovement(0.0D, Math.min(movement.y, 0.0D), 0.0D);
        player.hurtMarked = true;
    }

    private void spawnStoneRing(ServerPlayer player, int ticksElapsed) {
        ServerLevel level = player.serverLevel();
        int realm = runicRealm(player);
        double radius = BASE_RADIUS + realm * 0.15D + RunicInscriptionHelper.stoneWardRadiusBonus(player);
        int points = 18;

        for (int i = 0; i < points; i++) {
            double angle = ((Math.PI * 2.0D) / points) * i + ticksElapsed * 0.035D;
            double x = player.getX() + Math.cos(angle) * radius;
            double z = player.getZ() + Math.sin(angle) * radius;

            level.sendParticles(
                    new BlockParticleOption(ParticleTypes.BLOCK, Blocks.STONE.defaultBlockState()),
                    x,
                    player.getY() + 0.1D,
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