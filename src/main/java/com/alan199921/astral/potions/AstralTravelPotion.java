package com.alan199921.astral.potions;

import com.alan199921.astral.effects.ModEffects;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;

public class AstralTravelPotion extends Potion {
    public AstralTravelPotion(){
        super(new EffectInstance(ModEffects.astralEffect, 1200, 0));
        setRegistryName("astral_travel_potion");
    }
}
