package com.alan199921.astral.events;

import net.minecraft.entity.Entity;
import net.minecraft.util.EntityDamageSource;

public class AstralDamage extends EntityDamageSource {

    private static final String damageName = "astral.astral";

    public AstralDamage(Entity source) {
        super(damageName, source);
        setDamageBypassesArmor();
    }

    public static boolean isAstralDamage(String damageType) {
        return damageType.equals(damageName);
    }
}
