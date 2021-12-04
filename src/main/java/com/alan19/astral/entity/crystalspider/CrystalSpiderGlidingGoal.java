package com.alan19.astral.entity.crystalspider;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;

public class CrystalSpiderGlidingGoal extends RandomStrollGoal {
    private final Mob entityIn;

    public CrystalSpiderGlidingGoal(PathfinderMob creatureIn, double speedIn) {
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
