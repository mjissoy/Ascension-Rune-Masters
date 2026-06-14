package net.zic.runic_ascension.core.skills.active.utility;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.thejadeproject.ascension.refactor_packages.skill_casting.casting.CastEndData;
import net.thejadeproject.ascension.refactor_packages.skill_casting.casting.CastResult;
import net.thejadeproject.ascension.refactor_packages.skills.castable.CastType;
import net.thejadeproject.ascension.refactor_packages.skills.castable.ICastData;
import net.thejadeproject.ascension.refactor_packages.skills.castable.IPreCastData;
import net.zic.runic_ascension.content.runes.ModRunicRunes;
import net.zic.runic_ascension.core.skills.active.AbstractRunicActiveSkill;
import net.zic.runic_ascension.util.RunicInscriptionHelper;

public class TraceStep extends AbstractRunicActiveSkill {

    private static final double QI_COST = 10.0D;
    private static final double BASE_RANGE = 4.0D;
    private static final double RANGE_PER_REALM = 1.0D;
    private static final double STEP_SIZE = 0.25D;
    private static final int COOLDOWN_TICKS = 35;

    @Override
    protected String translationKey() {
        return "runic_ascension.skill.trace_step";
    }

    @Override
    public CastResult canCast(Entity caster, IPreCastData preCastData) {
        return requireRunicCaster(
                caster,
                QI_COST,
                ModRunicRunes.WIND_RUNE.getId(),
                ModRunicRunes.VEIL_RUNE.getId()
        );
    }

    @Override
    public void initialCast(Entity caster, IPreCastData preCastData) {
        if (!(caster instanceof ServerPlayer player)) return;
        if (player.level().isClientSide()) return;

        if (!tryConsumeQi(player, QI_COST)) return;

        Vec3 look = player.getLookAngle();
        Vec3 direction = new Vec3(look.x, 0.0D, look.z);

        if (direction.lengthSqr() < 0.001D) {
            direction = Vec3.directionFromRotation(0.0F, player.getYRot());
        }

        direction = direction.normalize();

        Vec3 start = player.position();
        Vec3 destination = resolveSmartDestination(player, direction, range(player));

        spawnStepParticles(player.serverLevel(), start, player.getBbHeight());
        player.teleportTo(destination.x, destination.y, destination.z);
        spawnStepParticles(player.serverLevel(), destination, player.getBbHeight());

        player.resetFallDistance();

        player.level().playSound(
                null,
                destination.x,
                destination.y,
                destination.z,
                SoundEvents.ENDERMAN_TELEPORT,
                SoundSource.PLAYERS,
                0.35F,
                1.75F
        );
        RunicInscriptionHelper.afterTraceStep(player);
    }

    private double range(ServerPlayer player) {
        return BASE_RANGE
                + runicRealm(player) * RANGE_PER_REALM
                + RunicInscriptionHelper.traceStepRangeBonus(player);
    }

    private Vec3 resolveSmartDestination(ServerPlayer player, Vec3 direction, double range) {
        Vec3 origin = player.position();
        Vec3 best = origin;

        int steps = (int) Math.ceil(range / STEP_SIZE);

        for (int i = 1; i <= steps; i++) {
            double distance = Math.min(i * STEP_SIZE, range);
            Vec3 base = origin.add(direction.scale(distance));

            Vec3 safe = findNearbySafeStep(player, base, origin.y);

            if (safe == null) {
                break;
            }

            best = safe;
        }

        return best;
    }

    private Vec3 findNearbySafeStep(ServerPlayer player, Vec3 base, double originY) {
        double[] yOffsets = {0.0D, 0.25D, 0.5D, 0.75D, 1.0D, 1.25D, 1.5D, 2.0D, -0.25D, -0.5D, -0.75D, -1.0D};

        for (double yOffset : yOffsets) {
            Vec3 candidate = new Vec3(base.x, originY + yOffset, base.z);

            if (isSafeStandingPosition(player, candidate)) {
                return candidate;
            }
        }

        return null;
    }

    private boolean isSafeStandingPosition(ServerPlayer player, Vec3 position) {
        double halfWidth = player.getBbWidth() * 0.5D;
        double height = player.getBbHeight();

        AABB bodyBox = new AABB(
                position.x - halfWidth,
                position.y,
                position.z - halfWidth,
                position.x + halfWidth,
                position.y + height,
                position.z + halfWidth
        );

        if (!player.level().noCollision(player, bodyBox)) {
            return false;
        }

        AABB floorCheck = bodyBox.move(0.0D, -0.08D, 0.0D);

        return !player.level().noCollision(player, floorCheck);
    }

    private void spawnStepParticles(ServerLevel level, Vec3 position, float height) {
        level.sendParticles(
                ParticleTypes.POOF,
                position.x,
                position.y + height * 0.5D,
                position.z,
                18,
                0.25D,
                0.35D,
                0.25D,
                0.02D
        );

        level.sendParticles(
                ParticleTypes.PORTAL,
                position.x,
                position.y + height * 0.5D,
                position.z,
                12,
                0.2D,
                0.25D,
                0.2D,
                0.04D
        );
    }

    @Override public void finalCast(CastEndData reason, Entity caster, ICastData castData) {}
    @Override public boolean continueCasting(int ticksElapsed, Entity caster, ICastData castData) {return false;}
    @Override public int getCooldown(CastEndData castEndData) {return COOLDOWN_TICKS;}
    @Override public CastType getCastType() {return CastType.INSTANT;}
}