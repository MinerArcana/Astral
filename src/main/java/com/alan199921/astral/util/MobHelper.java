package com.alan199921.astral.util;

import net.minecraft.entity.LivingEntity;

public class MobHelper {

    public static void resetTarget(LivingEntity entity) {
        resetTarget(entity, false);
    }

    public static void resetTarget(LivingEntity entity, boolean resetRevengeTarget) {
        entity.setRevengeTarget(null);
        if (resetRevengeTarget) {
            entity.setRevengeTarget(null);
        }
    }

}
