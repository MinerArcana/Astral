package com.alan199921.astral.events;

import net.minecraft.entity.Entity;
import net.minecraft.util.EntityDamageSource;

import javax.annotation.Nullable;

public class AstralEntityDamage extends EntityDamageSource implements IAstralDamage {


    public AstralEntityDamage(@Nullable Entity damageSourceEntityIn) {
        super(IAstralDamage.DAMAGE_NAME, damageSourceEntityIn);
    }
}
