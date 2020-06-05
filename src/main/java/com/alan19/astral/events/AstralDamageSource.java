package com.alan19.astral.events;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;

public class AstralDamageSource {
    public static DamageSource causeAstralMobDamage(LivingEntity mob) {
        return new EntityDamageSource(IAstralDamage.DAMAGE_NAME, mob);
    }

}
