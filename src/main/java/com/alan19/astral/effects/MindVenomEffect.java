package com.alan19.astral.effects;

import com.alan19.astral.util.ExperienceHelper;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nonnull;

public class MindVenomEffect extends MobEffect {
    protected MindVenomEffect() {
        super(MobEffectCategory.HARMFUL, 7486647);
    }

    @Override
    public void applyEffectTick(@Nonnull LivingEntity entityLivingBaseIn, int amplifier) {
        if (entityLivingBaseIn instanceof Player && ExperienceHelper.getPlayerXP((Player) entityLivingBaseIn) > 0) {
            final Player playerEntity = (Player) entityLivingBaseIn;
            if (playerEntity.experienceLevel <= 30) {
                playerEntity.giveExperiencePoints(-5);
            }
            else {
                playerEntity.giveExperiencePoints((int) Math.max(playerEntity.totalExperience * .01, 1) * -1);
            }
        }
        else if (entityLivingBaseIn instanceof Player && entityLivingBaseIn.getHealth() > 1.0F && ExperienceHelper.getPlayerXP((Player) entityLivingBaseIn) <= 0) {
            entityLivingBaseIn.hurt(DamageSource.MAGIC, 1.0F);
        }

    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        int interval = 25 >> amplifier;
        return interval <= 0 || duration % interval == 0;

    }
}
