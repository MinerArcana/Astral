package com.alan19.astral.mentalconstructs;

import net.minecraft.entity.player.PlayerEntity;

public class Library extends MentalConstruct {
    @Override
    public void performEffect(PlayerEntity player, int level) {
        if (player.experienceLevel < level) {
            player.giveExperiencePoints((int) Math.max(1, player.experience * (1 + level * .01)));
        }
    }

    @Override
    public EffectType getEffectType() {
        return EffectType.CONDITIONAL;
    }

    @Override
    public MentalConstructType getType() {
        return AstralMentalConstructs.GARDEN.get();
    }
}
