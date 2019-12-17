package com.alan199921.astral.events;

import net.minecraft.util.DamageSource;

public class AstralDamage extends DamageSource implements IAstralDamage {
    public AstralDamage() {
        super(IAstralDamage.DAMAGE_NAME);
    }
}
