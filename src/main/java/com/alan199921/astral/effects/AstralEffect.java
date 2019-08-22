package com.alan199921.astral.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class AstralEffect extends Effect {
    public AstralEffect() {
        super(EffectType.NEUTRAL, 13158600);
        setRegistryName("astral_effect");
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
