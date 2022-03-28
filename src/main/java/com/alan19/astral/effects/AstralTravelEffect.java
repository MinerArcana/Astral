package com.alan19.astral.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

import javax.annotation.Nonnull;

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
    public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier) {
        if (!entityLivingBaseIn.getCommandSenderWorld().isClientSide() && entityLivingBaseIn.getHealth() < entityLivingBaseIn.getMaxHealth() && !entityLivingBaseIn.hasEffect(AstralEffects.MIND_VENOM.get())) {
            if (entityLivingBaseIn instanceof PlayerEntity) {
                PlayerEntity playerEntity = (PlayerEntity) entityLivingBaseIn;
                if (playerEntity.totalExperience > 0 && entityLivingBaseIn.getLastDamageSource() == null) {
                    entityLivingBaseIn.heal((amplifier * 0.5F) + 1.0F);
                    ((PlayerEntity) entityLivingBaseIn).giveExperiencePoints(-1);
                }
                playerEntity.getFoodData().setFoodLevel(15);
                playerEntity.getFoodData().saturationLevel = 0;
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return (duration % (60 - (amplifier * 5))) == 0;
    }

    @Override
    public double getAttributeModifierValue(int amplifier, @Nonnull AttributeModifier modifier) {
        return 3 * (double) (amplifier + 1);
    }
}
