package net.zic.runic_ascension.core.skills.active.attack;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.thejadeproject.ascension.refactor_packages.skill_casting.casting.CastEndData;
import net.thejadeproject.ascension.refactor_packages.skill_casting.casting.CastResult;
import net.thejadeproject.ascension.refactor_packages.skills.castable.CastType;
import net.thejadeproject.ascension.refactor_packages.skills.castable.ICastData;
import net.thejadeproject.ascension.refactor_packages.skills.castable.IPreCastData;
import net.zic.runic_ascension.content.runes.ModRunicRunes;
import net.zic.runic_ascension.core.skills.active.AbstractRunicActiveSkill;

public class ScriptSever extends AbstractRunicActiveSkill {

    private static final double QI_COST = 12.0D;
    private static final double BASE_RANGE = 8.0D;
    private static final int COOLDOWN_TICKS = 35;

    @Override
    protected String translationKey() {
        return "runic_ascension.skill.script_sever";
    }

    @Override
    public CastResult canCast(Entity caster, IPreCastData preCastData) {
        CastResult result = requireRunicCaster(
                caster,
                QI_COST,
                ModRunicRunes.CUT_RUNE.getId(),
                ModRunicRunes.LINE_RUNE.getId()
        );

        if (!result.isSuccess()) {
            return result;
        }

        if (!(caster instanceof ServerPlayer player)) {
            return new CastResult(CastResult.Type.FAILURE);
        }

        double range = range(player);
        LivingEntity target = findSoftLookTarget(player, range, 1.2D);

        return target == null
                ? new CastResult(CastResult.Type.FAILURE)
                : new CastResult(CastResult.Type.SUCCESS);
    }

    @Override
    public void initialCast(Entity caster, IPreCastData preCastData) {
        if (!(caster instanceof ServerPlayer player)) return;
        if (player.level().isClientSide()) return;

        double range = range(player);
        LivingEntity target = findSoftLookTarget(player, range, 1.2D);
        if (target == null) return;

        if (!tryConsumeQi(player, QI_COST)) return;

        float damage = calculateRunicAttackDamage(player, 18.0F, 2.15F, 12.0F);
        target.hurt(runicDamageSource(player), damage);

        ServerLevel level = player.serverLevel();
        Vec3 start = player.getEyePosition();
        Vec3 end = target.position().add(0.0D, target.getBbHeight() * 0.55D, 0.0D);

        spawnSeverLine(level, start, end);

        level.sendParticles(
                ParticleTypes.SWEEP_ATTACK,
                end.x,
                end.y,
                end.z,
                3,
                0.15D,
                0.15D,
                0.15D,
                0.0D
        );
    }

    private double range(ServerPlayer player) {
        return BASE_RANGE + runicRealm(player) * 0.75D;
    }

    private void spawnSeverLine(ServerLevel level, Vec3 start, Vec3 end) {
        Vec3 difference = end.subtract(start);
        int points = Math.max(6, (int) (difference.length() * 5.0D));

        for (int i = 0; i <= points; i++) {
            double progress = i / (double) points;
            Vec3 point = start.add(difference.scale(progress));

            level.sendParticles(
                    ParticleTypes.CRIT,
                    point.x,
                    point.y,
                    point.z,
                    1,
                    0.01D,
                    0.01D,
                    0.01D,
                    0.0D
            );
        }
    }

    @Override public void finalCast(CastEndData reason, Entity caster, ICastData castData) {}
    @Override public boolean continueCasting(int ticksElapsed, Entity caster, ICastData castData) { return false; }
    @Override public int getCooldown(CastEndData castEndData) { return COOLDOWN_TICKS; }
    @Override public CastType getCastType() { return CastType.INSTANT; }
}