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
     * Sets your hunger to 15 and saturation to 0 to disable natural healing
     *
     * @param entityLivingBaseIn A living entity under the Astral Travel effect
     * @param amplifier          The level of the effect
     */
    @Override
    public void performEffect(LivingEntity entityLivingBaseIn, int amplifier) {
        if (!entityLivingBaseIn.getEntityWorld().isRemote() && entityLivingBaseIn.getHealth() < entityLivingBaseIn.getMaxHealth()) {
            if (entityLivingBaseIn instanceof PlayerEntity) {
                PlayerEntity playerEntity = (PlayerEntity) entityLivingBaseIn;
                playerEntity.getFoodStats().setFoodLevel(15);
                playerEntity.getFoodStats().setFoodSaturationLevel(0);
                if (playerEntity.experienceTotal > 0 && entityLivingBaseIn.getLastDamageSource() == null) {
                    entityLivingBaseIn.heal((amplifier * 0.5F) + 1.0F);
                    ((PlayerEntity) entityLivingBaseIn).giveExperiencePoints(-1);
                }
            }
        }
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return (duration % (60 - (amplifier * 5))) == 0;
    }
}
