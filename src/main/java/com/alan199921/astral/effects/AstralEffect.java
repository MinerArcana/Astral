package com.alan199921.astral.effects;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class AstralEffect extends Effect {
    public AstralEffect() {
        super(EffectType.NEUTRAL, 13158600);
        setRegistryName("astral_travel");
    }
}
