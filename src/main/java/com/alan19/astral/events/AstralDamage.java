package com.alan19.astral.events;

import net.minecraft.util.DamageSource;

public class AstralDamage extends DamageSource implements IAstralDamage {
    public AstralDamage() {
        super(IAstralDamage.DAMAGE_NAME);
    }
}
