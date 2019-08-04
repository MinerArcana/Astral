package com.alan199921.astral.potions;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class Travelling extends Effect {
    protected Travelling(EffectType typeIn, int liquidColorIn) {
        super(typeIn, liquidColorIn);
    }

    @Override
    public void performEffect(LivingEntity entityLivingBaseIn, int amplifier) {
        super.performEffect(entityLivingBaseIn, amplifier);
        entityLivingBaseIn.setInvisible(true);
        entityLivingBaseIn.noClip = true;
        if (entityLivingBaseIn instanceof PlayerEntity){
            PlayerEntity p = (PlayerEntity) entityLivingBaseIn;
            p.abilities.allowFlying = true;
        }
    }
}
