package com.alan19.astral.effects;

import com.alan19.astral.util.ExperienceHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.DamageSource;

import javax.annotation.Nonnull;

public class MindVenomEffect extends Effect {
    protected MindVenomEffect() {
        super(EffectType.HARMFUL, 11918123);
    }

    @Override
    public void performEffect(@Nonnull LivingEntity entityLivingBaseIn, int amplifier) {
        if (entityLivingBaseIn instanceof PlayerEntity && ExperienceHelper.getPlayerXP((PlayerEntity) entityLivingBaseIn) > 0) {
            final PlayerEntity playerEntity = (PlayerEntity) entityLivingBaseIn;
            playerEntity.giveExperiencePoints((int) Math.max(playerEntity.experienceTotal * .05, 1) * -1);
        }
        else if (entityLivingBaseIn instanceof PlayerEntity && entityLivingBaseIn.getHealth() > 1.0F && ExperienceHelper.getPlayerXP((PlayerEntity) entityLivingBaseIn) > 0) {
            entityLivingBaseIn.attackEntityFrom(DamageSource.MAGIC, 1.0F);
        }

    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        int interval = 25 >> amplifier;
        return interval <= 0 || duration % interval == 0;

    }
}
