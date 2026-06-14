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

    private static final double QI_COST = 8.0D;
    private static final double BASE_RANGE = 8.0D;
    private static final double RANGE_PER_REALM = 2.0D;
    private static final double STEP_SIZE = 0.25D;
    private static final int COOLDOWN_TICKS = 18;

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

        Vec3 look = player.getLookAngle();
        Vec3 direction = new Vec3(look.x, 0.0D, look.z);

        if (direction.lengthSqr() < 0.001D) {
            direction = Vec3.directionFromRotation(0.0F, player.getYRot());
        }

        direction = direction.normalize();

        Vec3 start = player.position();
        Vec3 destination = resolveSmartDestination(player, direction, range(player));

        if (destination.distanceToSqr(start) < 0.25D) {
            return;
        }

        if (!tryConsumeQi(player, QI_COST)) return;

        ServerLevel level = player.serverLevel();

        spawnStepParticles(level, start, player.getBbHeight());
        spawnTraceLine(level, start, destination, player.getBbHeight());

        player.teleportTo(destination.x, destination.y, destination.z);

        spawnStepParticles(level, destination, player.getBbHeight());

        player.resetFallDistance();

        player.level().playSound(
                null,
                destination.x,
                destination.y,
                destination.z,
                SoundEvents.AMETHYST_BLOCK_CHIME,
                SoundSource.PLAYERS,
                0.45F,
                1.65F
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
        int consecutiveFailures = 0;

        for (int i = 1; i <= steps; i++) {
            double distance = Math.min(i * STEP_SIZE, range);
            Vec3 base = origin.add(direction.scale(distance));

            Vec3 safe = findNearbySafeStep(player, base, direction, origin.y);

            if (safe == null) {
                consecutiveFailures++;
                if (consecutiveFailures >= 6 && best.distanceToSqr(origin) > 0.25D) {
                    break;
                }

                continue;
            }

            consecutiveFailures = 0;
            best = safe;
        }

        return best;
    }

    private Vec3 findNearbySafeStep(ServerPlayer player, Vec3 base, Vec3 direction, double originY) {
        double[] yOffsets = {0.0D, 0.05D, 0.25D, 0.5D, 0.75D, 1.0D, 1.25D, 1.5D, 2.0D, -0.05D, -0.25D, -0.5D, -0.75D, -1.0D};

        Vec3 side = new Vec3(-direction.z, 0.0D, direction.x);

        if (side.lengthSqr() > 0.001D) {
            side = side.normalize();
        }

        double[] sideOffsets = {
                0.0D,
                0.18D,
                -0.18D,
                0.36D,
                -0.36D
        };

        for (double yOffset : yOffsets) {
            for (double sideOffset : sideOffsets) {
                Vec3 shiftedBase = base.add(side.scale(sideOffset));
                Vec3 candidate = new Vec3(shiftedBase.x, originY + yOffset, shiftedBase.z);

                if (isSafeStandingPosition(player, candidate)) {
                    return candidate;
                }
            }
        }

        return null;
    }

    private boolean isSafeStandingPosition(ServerPlayer player, Vec3 position) {
        double halfWidth = Math.max(0.1D, player.getBbWidth() * 0.5D - 0.04D);
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

        double footWidth = Math.max(0.08D, halfWidth * 0.8D);

        AABB floorCheck = new AABB(
                position.x - footWidth,
                position.y - 0.16D,
                position.z - footWidth,
                position.x + footWidth,
                position.y - 0.02D,
                position.z + footWidth
        );

        return !player.level().noCollision(player, floorCheck);
    }

    private void spawnStepParticles(ServerLevel level, Vec3 position, float height) {
        level.sendParticles(
                ParticleTypes.POOF,
                position.x,
                position.y + height * 0.5D,
                position.z,
                10,
                0.18D,
                0.24D,
                0.18D,
                0.01D
        );

        level.sendParticles(
                ParticleTypes.ENCHANT,
                position.x,
                position.y + height * 0.5D,
                position.z,
                18,
                0.35D,
                0.25D,
                0.35D,
                0.08D
        );
    }

    private void spawnTraceLine(ServerLevel level, Vec3 start, Vec3 end, float height) {
        Vec3 from = start.add(0.0D, height * 0.45D, 0.0D);
        Vec3 to = end.add(0.0D, height * 0.45D, 0.0D);
        Vec3 difference = to.subtract(from);

        int points = Math.max(8, (int) (difference.length() * 3.0D));

        for (int i = 0; i <= points; i++) {
            double progress = i / (double) points;
            Vec3 point = from.add(difference.scale(progress));

            level.sendParticles(
                    ParticleTypes.ENCHANT,
                    point.x,
                    point.y,
                    point.z,
                    1,
                    0.02D,
                    0.02D,
                    0.02D,
                    0.0D
            );
        }
    }

    @Override public void finalCast(CastEndData reason, Entity caster, ICastData castData) {}
    @Override public boolean continueCasting(int ticksElapsed, Entity caster, ICastData castData) {return false;}
    @Override public int getCooldown(CastEndData castEndData) {return COOLDOWN_TICKS;}
    @Override public CastType getCastType() {return CastType.INSTANT;}
}