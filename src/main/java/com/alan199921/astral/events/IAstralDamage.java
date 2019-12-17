package com.alan199921.astral.events;

import net.minecraft.util.DamageSource;

public interface IAstralDamage {
    String DAMAGE_NAME = "astral.astral";

    static boolean isDamageAstral(DamageSource source) {
        return source instanceof IAstralDamage;
    }
}