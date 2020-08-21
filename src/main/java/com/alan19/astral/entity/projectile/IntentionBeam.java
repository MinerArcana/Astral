package com.alan19.astral.entity.projectile;

import com.alan19.astral.blocks.IntentionBlock;
import com.alan19.astral.network.AstralNetwork;
import com.alan19.astral.particle.AstralParticles;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.tags.Tag;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import java.util.UUID;

/**
 * Entity for the Intention Tracker. Based off the Mana Burst from Botania.
 * https://github.com/Vazkii/Botania/blob/eed14c95cccea7c496fe674327d0ec8b0e999cc9/src/main/java/vazkii/botania/common/entity/EntityManaBurst.java#L49
 */
public class IntentionBeam extends Entity implements IProjectile {
    private UUID playerUUID;
    private int beamLevel;
    private int maxDistance;

    public IntentionBeam(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    protected void onHit(RayTraceResult result) {
        AstralNetwork.sendOfferingBrazierFinishParticles(new BlockPos(getPosX(), getPosY(), getPosZ()), world.getChunkAt(getPosition()));
        if (result.getType() == RayTraceResult.Type.BLOCK) {
            final BlockRayTraceResult blockRayTraceResult = (BlockRayTraceResult) result;
            final BlockState blockState = world.getBlockState(blockRayTraceResult.getPos());
            if (blockState.getBlock() instanceof IntentionBlock) {
                ((IntentionBlock) blockState.getBlock()).onIntentionTrackerHit(world.getPlayerByUuid(playerUUID), beamLevel, blockRayTraceResult, blockState);
                remove();
            }
            else {
                //TODO Teleportation effect
            }
        }
    }

    public void setPlayer(PlayerEntity player) {
        playerUUID = player.getUniqueID();
    }

    public void setLevel(int level) {
        beamLevel = level;
    }

    public void setMaxDistance(int distance) {
        maxDistance = distance;
    }

    @Override
    public boolean handleFluidAcceleration(Tag<Fluid> fluidTag) {
        return false;
    }

    @Override
    protected void registerData() {

    }

    @Override
    public boolean isInLava() {
        return false;
    }

    @Override
    public void writeAdditional(@Nonnull CompoundNBT compound) {
        compound.putUniqueId("playerID", playerUUID);
        compound.putInt("beamLevel", beamLevel);
        compound.putInt("maxDistance", maxDistance);
    }

    @Override
    public void readAdditional(@Nonnull CompoundNBT compound) {
        playerUUID = compound.getUniqueId("playerID");
        beamLevel = compound.getInt("beamLevel");
        maxDistance = compound.getInt("maxDistance");
    }

    @Override
    @Nonnull
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    /**
     * Updates the entity motion clientside, called by packets from the server
     */
    @Override
    @OnlyIn(Dist.CLIENT)
    public void setVelocity(double x, double y, double z) {
        this.setMotion(x, y, z);
        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt(x * x + z * z);
            this.rotationPitch = (float) (MathHelper.atan2(y, f) * (double) (180F / (float) Math.PI));
            this.rotationYaw = (float) (MathHelper.atan2(x, z) * (double) (180F / (float) Math.PI));
            this.prevRotationPitch = this.rotationPitch;
            this.prevRotationYaw = this.rotationYaw;
            this.setLocationAndAngles(this.getPosX(), this.getPosY(), this.getPosZ(), this.rotationYaw, this.rotationPitch);
        }

    }

    /**
     * Similar to setArrowHeading, it's point the throwable entity to a x, y, z direction.
     */
    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        Vec3d vec3d = (new Vec3d(x, y, z)).normalize().add(this.rand.nextGaussian() * (double) 0.0075F * (double) inaccuracy, this.rand.nextGaussian() * (double) 0.0075F * (double) inaccuracy, this.rand.nextGaussian() * (double) 0.0075F * (double) inaccuracy).scale(velocity);
        this.setMotion(vec3d);
        float f = MathHelper.sqrt(horizontalMag(vec3d));
        this.rotationYaw = (float) (MathHelper.atan2(vec3d.x, z) * (double) (180F / (float) Math.PI));
        this.rotationPitch = (float) (MathHelper.atan2(vec3d.y, f) * (double) (180F / (float) Math.PI));
        this.prevRotationYaw = this.rotationYaw;
        this.prevRotationPitch = this.rotationPitch;
    }

    /**
     * Called to update the entity's position/logic. Adapted from LLamaSpitEntity.java
     */
    @Override
    public void tick() {
        super.tick();
        if (world instanceof ServerWorld && ticksExisted % 5 == 0){
            AstralNetwork.sendOfferingBrazierFinishParticles(new BlockPos(getPosX(), getPosY(), getPosZ()), world.getChunkAt(getPosition()));
        }
        if (ticksExisted >= maxDistance * 4) {
            this.remove();
            return;
        }
        Vec3d vec3d = this.getMotion();
        RayTraceResult raytraceresult = ProjectileHelper.rayTrace(this, this.getBoundingBox().expand(vec3d).grow(1.0D), (entity) -> !entity.isSpectator(), RayTraceContext.BlockMode.OUTLINE, true);
        if (raytraceresult.getType() != RayTraceResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
            this.onHit(raytraceresult);
        }

        double d0 = this.getPosX() + vec3d.x;
        double d1 = this.getPosY() + vec3d.y;
        double d2 = this.getPosZ() + vec3d.z;
        float f = MathHelper.sqrt(horizontalMag(vec3d));
        this.rotationYaw = (float) (MathHelper.atan2(vec3d.x, vec3d.z) * (double) (180F / (float) Math.PI));

        this.rotationPitch = (float) (MathHelper.atan2(vec3d.y, f) * (double) (180F / (float) Math.PI));
        while (this.rotationPitch - this.prevRotationPitch < -180.0F) {
            this.prevRotationPitch -= 360.0F;
        }

        while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
            this.prevRotationPitch += 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
            this.prevRotationYaw -= 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
            this.prevRotationYaw += 360.0F;
        }

        this.rotationPitch = MathHelper.lerp(0.2F, this.prevRotationPitch, this.rotationPitch);
        this.rotationYaw = MathHelper.lerp(0.2F, this.prevRotationYaw, this.rotationYaw);
        if (!this.world.isMaterialInBB(this.getBoundingBox(), Material.AIR) || this.isInWaterOrBubbleColumn()) {
            this.remove();
        }
        else {
            if (!this.hasNoGravity()) {
                this.setMotion(this.getMotion().add(0.0D, -0.06F, 0.0D));
            }

            this.setPosition(d0, d1, d2);
        }
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }
}
