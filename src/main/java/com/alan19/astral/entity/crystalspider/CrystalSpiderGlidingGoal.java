package com.alan19.astral.entity.crystalspider;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;

public class CrystalSpiderGlidingGoal extends RandomWalkingGoal {
    private final MobEntity entityIn;

    public CrystalSpiderGlidingGoal(CreatureEntity creatureIn, double speedIn) {
        super(creatureIn, speedIn);
        entityIn = creatureIn;
    }


    @Override
    public boolean canUse() {
        return entityIn.getY() < 128;
    }

    @Override
    public void tick() {
        if (entityIn.isInWater()) {
            entityIn.setJumping(true);
        }
    }
}
