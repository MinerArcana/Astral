package com.alan19.astral.events.astraldamage;

import com.alan19.astral.events.IAstralDamage;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.LivingEntity;

public class AstralDamageSource {
    public static DamageSource causeAstralMobDamage(LivingEntity mob) {
        return new EntityDamageSource(IAstralDamage.DAMAGE_NAME, mob);
    }

}
