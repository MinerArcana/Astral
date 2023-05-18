package com.minerarcana.astral.effect;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeMod;

import javax.annotation.Nonnull;
import java.util.UUID;

public class AstralTravelEffect extends MobEffect {
    protected AstralTravelEffect() {
        super(MobEffectCategory.NEUTRAL, 13158600);
    }

    @Override
    public double getAttributeModifierValue(int amplifier, @Nonnull AttributeModifier modifier) {
        if (modifier.getId().toString().equals("8ac2f7b4-70ae-434b-ac1d-26de7bfcc494")) {
            return 3 * (double) (amplifier + 1);
        }
        else if (modifier.getId().toString().equals("6e379b13-4212-4aca-8f0a-57421752570f")) {
            return .2 * (double) (amplifier);
        }
        else {
            return super.getAttributeModifierValue(amplifier, modifier);
        }
    }
}
