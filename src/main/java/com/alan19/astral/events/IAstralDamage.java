package com.alan19.astral.events;

import net.minecraft.util.DamageSource;

public interface IAstralDamage {
    String DAMAGE_NAME = "astral.astral";

    static boolean isDamageAstral(DamageSource source) {
        return source instanceof IAstralDamage;
    }

    static boolean canDamageTypeDamageAstral(DamageSource source) {
        return isDamageAstral(source) || source.isMagic() || source.isBypassInvul() || source.getMsgId().equals(DAMAGE_NAME);
    }
}
