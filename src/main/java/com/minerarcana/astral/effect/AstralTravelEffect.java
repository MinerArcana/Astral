package com.minerarcana.astral.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import javax.annotation.Nonnull;

public class AstralTravelEffect extends MobEffect {
    protected AstralTravelEffect() {
        super(MobEffectCategory.NEUTRAL, 13158600);
    }

    @Override
    public double getAttributeModifierValue(int amplifier, @Nonnull AttributeModifier modifier) {
        if (modifier.getId().toString().equals("8ac2f7b4-70ae-434b-ac1d-26de7bfcc494")) {
            return 3 * (double) (amplifier + 1);
        } else {
            return super.getAttributeModifierValue(amplifier, modifier);
        }
    }
}
