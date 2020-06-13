package com.alan19.astral.entity;

import com.alan19.astral.entity.projectile.CrystalWebProjectileEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
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
    public void tick() {
        LivingEntity livingentity = this.parentEntity.getAttackTarget();
        if (livingentity != null && livingentity.getDistanceSq(this.parentEntity) < 4096.0D && this.parentEntity.canEntityBeSeen(livingentity)) {
            World world = this.parentEntity.world;
            this.attackTimer++;
            if (this.attackTimer == 10) {
                world.playEvent(null, 1015, new BlockPos(this.parentEntity), 0);
            }

            if (this.attackTimer == 20) {
                Vec3d vec3d = this.parentEntity.getLook(1.0F);
                world.playEvent(null, 1016, new BlockPos(this.parentEntity), 0);
                CrystalWebProjectileEntity crystalWebProjectileEntity = new CrystalWebProjectileEntity(AstralEntities.CRYSTAL_WEB_PROJECTILE_ENTITY.get(), world);
                crystalWebProjectileEntity.setPosition(this.parentEntity.getPosX() + vec3d.x * 4.0D, this.parentEntity.getPosYHeight(0.5D) + 0.5D, this.parentEntity.getPosZ() + vec3d.z * 4.0D);
                crystalWebProjectileEntity.setMotion((livingentity.getPosX() - (this.parentEntity.getPosX() + vec3d.x * 4.0D)) / 4, (livingentity.getPosYHeight(0.5D) - (0.5D + this.parentEntity.getPosYHeight(0.5D))) / 4, (livingentity.getPosZ() - (this.parentEntity.getPosZ() + vec3d.z * 4.0D)) / 4);
                world.addEntity(crystalWebProjectileEntity);
                this.attackTimer = -40;
            }
        }
        else if (this.attackTimer > 0) {
            --this.attackTimer;
        }

        this.parentEntity.setAttacking(this.attackTimer > 10);
    }
}
