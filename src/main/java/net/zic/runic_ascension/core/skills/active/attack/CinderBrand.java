package net.zic.runic_ascension.core.skills.active.attack;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.thejadeproject.ascension.refactor_packages.skill_casting.casting.CastEndData;
import net.thejadeproject.ascension.refactor_packages.skill_casting.casting.CastResult;
import net.thejadeproject.ascension.refactor_packages.skills.castable.CastType;
import net.thejadeproject.ascension.refactor_packages.skills.castable.ICastData;
import net.thejadeproject.ascension.refactor_packages.skills.castable.IPreCastData;
import net.zic.runic_ascension.content.runes.ModRunicRunes;
import net.zic.runic_ascension.core.skills.active.AbstractRunicActiveSkill;

public class CinderBrand extends AbstractRunicActiveSkill {

    private static final double QI_COST = 18.0D;
    private static final double RANGE = 9.0D;
    private static final int BASE_FIRE_TICKS = 80;
    private static final int COOLDOWN_TICKS = 80;

    @Override
    protected String translationKey() {
        return "runic_ascension.skill.cinder_brand";
    }

    @Override
    public CastResult canCast(Entity caster, IPreCastData preCastData) {
        CastResult result = requireRunicCaster(
                caster,
                QI_COST,
                ModRunicRunes.FLAME_RUNE.getId(),
                ModRunicRunes.MARK_RUNE.getId()
        );

        if (!result.isSuccess()) {
            return result;
        }

        if (!(caster instanceof ServerPlayer player)) {
            return new CastResult(CastResult.Type.FAILURE);
        }

        LivingEntity target = findSoftLookTarget(player, RANGE, 1.35D);

        return target == null
                ? new CastResult(CastResult.Type.FAILURE)
                : new CastResult(CastResult.Type.SUCCESS);
    }

    @Override
    public void initialCast(Entity caster, IPreCastData preCastData) {
        if (!(caster instanceof ServerPlayer player)) return;
        if (player.level().isClientSide()) return;

        LivingEntity target = findSoftLookTarget(player, RANGE, 1.35D);
        if (target == null) return;

        if (!tryConsumeQi(player, QI_COST)) return;

        int realm = runicRealm(player);
        int fireTicks = BASE_FIRE_TICKS + realm * 20;

        float damage = calculateRunicAttackDamage(
                player,
                12.0F,
                1.45F,
                7.0F
        );

        target.hurt(runicDamageSource(player), damage);
        target.setRemainingFireTicks(Math.max(target.getRemainingFireTicks(), fireTicks));

        ServerLevel level = player.serverLevel();

        level.sendParticles(
                ParticleTypes.FLAME,
                target.getX(),
                target.getY() + target.getBbHeight() * 0.55D,
                target.getZ(),
                18,
                0.25D,
                0.35D,
                0.25D,
                0.03D
        );

        level.sendParticles(
                ParticleTypes.SMOKE,
                target.getX(),
                target.getY() + target.getBbHeight() * 0.55D,
                target.getZ(),
                8,
                0.2D,
                0.25D,
                0.2D,
                0.02D
        );
    }

    @Override public void finalCast(CastEndData reason, Entity caster, ICastData castData) {}
    @Override public boolean continueCasting(int ticksElapsed, Entity caster, ICastData castData) { return false; }
    @Override public int getCooldown(CastEndData castEndData) { return COOLDOWN_TICKS; }
    @Override public CastType getCastType() { return CastType.INSTANT; }
}