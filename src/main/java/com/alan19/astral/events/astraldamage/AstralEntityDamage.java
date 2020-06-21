package com.alan19.astral.events.astraldamage;

import com.alan19.astral.events.IAstralDamage;
import net.minecraft.entity.Entity;
import net.minecraft.util.EntityDamageSource;

import javax.annotation.Nullable;

public class AstralEntityDamage extends EntityDamageSource implements IAstralDamage {

    public AstralEntityDamage(@Nullable Entity damageSourceEntityIn) {
        super(IAstralDamage.DAMAGE_NAME, damageSourceEntityIn);
    }
}
