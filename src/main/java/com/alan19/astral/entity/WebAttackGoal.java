package com.alan19.astral.entity;

import com.alan19.astral.entity.projectile.CrystalWebProjectileEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class WebAttackGoal extends Goal {
    private final CrystalSpiderEntity parentEntity;
    public int attackTimer;

    public WebAttackGoal(CrystalSpiderEntity crystalSpiderEntity) {
        this.parentEntity = crystalSpiderEntity;
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean shouldExecute() {
        return this.parentEntity.getAttackTarget() != null;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting() {
        this.attackTimer = 0;
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    @Override
    public void resetTask() {
        this.parentEntity.setAttacking(false);
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    @Override
    public void tick() {
        LivingEntity livingentity = this.parentEntity.getAttackTarget();
        if (livingentity != null && livingentity.getDistanceSq(this.parentEntity) < 4096.0D && this.parentEntity.canEntityBeSeen(livingentity)) {
            World world = this.parentEntity.world;
            this.attackTimer++;
            if (this.attackTimer == 20) {
                world.playSound(null, parentEntity.getPosX(), parentEntity.getPosY(), parentEntity.getPosZ(), SoundEvents.ENTITY_LLAMA_SPIT, parentEntity.getSoundCategory(), 1.0F, 1.0F + (world.getRandom().nextFloat() - world.getRandom().nextFloat()) * 0.2F);
                CrystalWebProjectileEntity crystalWebProjectileEntity = new CrystalWebProjectileEntity(world, parentEntity);
                final double x = livingentity.getPosX() - parentEntity.getPosX();
                final double y = livingentity.getPosYHeight(0.3333333333333333D) - crystalWebProjectileEntity.getPosY();
                final double z = livingentity.getPosZ() - parentEntity.getPosZ();
                crystalWebProjectileEntity.shoot(x, y + MathHelper.sqrt(x * x + z * z) * 0.2F, z, .5F, 10F);
                this.attackTimer = -40;
            }
        }
        else if (this.attackTimer > 0) {
            --this.attackTimer;
        }

        this.parentEntity.setAttacking(this.attackTimer > 10);
    }
}
