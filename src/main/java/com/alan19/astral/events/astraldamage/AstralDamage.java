package com.alan19.astral.events.astraldamage;

import com.alan19.astral.events.IAstralDamage;
import net.minecraft.util.DamageSource;

public class AstralDamage extends DamageSource implements IAstralDamage {
    public AstralDamage() {
        super(IAstralDamage.DAMAGE_NAME);
    }
}
