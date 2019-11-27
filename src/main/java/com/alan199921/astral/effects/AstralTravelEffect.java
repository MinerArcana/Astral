package com.alan199921.astral.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class AstralTravelEffect extends Effect {
    public AstralTravelEffect() {
        super(EffectType.NEUTRAL, 13158600);
    }

    /**
     * Astral travel heals players using their XP when they are out of combat
     *
     * @param entityLivingBaseIn A living entity under the Astral Travel effect
     * @param amplifier          The level of the effect
     */
    @Override
    public void performEffect(LivingEntity entityLivingBaseIn, int amplifier) {
        if (!entityLivingBaseIn.getEntityWorld().isRemote() && entityLivingBaseIn.getHealth() < entityLivingBaseIn.getMaxHealth() && entityLivingBaseIn.getLastDamageSource() == null) {
            if (entityLivingBaseIn instanceof PlayerEntity) {
                PlayerEntity playerEntity = (PlayerEntity) entityLivingBaseIn;
                if (playerEntity.experienceTotal > 0) {
                    entityLivingBaseIn.heal(amplifier + 1.0F);
                    ((PlayerEntity) entityLivingBaseIn).giveExperiencePoints(-1);
                }
            }
        }
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return duration % (10 - amplifier) == 0;
    }
}
