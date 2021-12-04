package com.alan19.astral.events.astraldamage;

import com.alan19.astral.events.IAstralDamage;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

public class AstralEntityDamage extends EntityDamageSource implements IAstralDamage {

    public AstralEntityDamage(@Nullable Entity damageSourceEntityIn) {
        super(IAstralDamage.DAMAGE_NAME, damageSourceEntityIn);
    }
}
