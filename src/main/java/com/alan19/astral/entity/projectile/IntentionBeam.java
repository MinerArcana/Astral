package com.alan19.astral.entity.projectile;

import com.alan19.astral.api.AstralAPI;
import com.alan19.astral.api.intentiontracker.IBeamTracker;
import com.alan19.astral.blocks.IntentionBlock;
import com.alan19.astral.entity.AstralEntities;
import com.alan19.astral.network.AstralNetwork;
import com.alan19.astral.particle.AstralParticles;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tags.Tag;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.UUID;

/**
 * Entity for the Intention Tracker. Loosely based off the Mana Burst from Botania.
 * https://github.com/Vazkii/Botania/blob/eed14c95cccea7c496fe674327d0ec8b0e999cc9/src/main/java/vazkii/botania/common/entity/EntityManaBurst.java#L49
 */
public class IntentionBeam extends Entity implements IProjectile {
    private static final DataParameter<Optional<UUID>> playerUUID = EntityDataManager.createKey(IntentionBeam.class, DataSerializers.OPTIONAL_UNIQUE_ID);
    private static final DataParameter<Integer> beamLevel = EntityDataManager.createKey(IntentionBeam.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> maxDistance = EntityDataManager.createKey(IntentionBeam.class, DataSerializers.VARINT);

    public IntentionBeam(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    public IntentionBeam(int beamLevel, int maxDistance, PlayerEntity playerEntity, World worldIn) {
        super(AstralEntities.INTENTION_BEAM_ENTITY.get(), worldIn);
        dataManager.set(IntentionBeam.beamLevel, beamLevel);
        dataManager.set(IntentionBeam.maxDistance, maxDistance);
        dataManager.set(IntentionBeam.playerUUID, Optional.of(playerEntity.getUniqueID()));
    }

    protected void onHit(RayTraceResult result) {
        if (world instanceof ServerWorld) {
            AstralNetwork.sendOfferingBrazierFinishParticles(new BlockPos(getPosX(), getPosY(), getPosZ()), world.getChunkAt(getPosition()));
        }
        if (result.getType() == RayTraceResult.Type.BLOCK) {
            final BlockRayTraceResult blockRayTraceResult = (BlockRayTraceResult) result;
            final BlockState blockState = world.getBlockState(blockRayTraceResult.getPos());
            if (blockState.getBlock() instanceof IntentionBlock && dataManager.get(playerUUID).isPresent()) {
                ((IntentionBlock) blockState.getBlock()).onIntentionTrackerHit(world.getPlayerByUuid(dataManager.get(playerUUID).get()), dataManager.get(beamLevel), blockRayTraceResult, blockState);
                remove();
            }
            else {
                //TODO Teleportation effect
            }
        }
    }

    public void setPlayer(PlayerEntity player) {
        dataManager.set(this.playerUUID, Optional.of(player.getUniqueID()));
    }

    public void setLevel(int level) {
        dataManager.set(this.beamLevel, level);
    }

    public void setMaxDistance(int distance) {
        dataManager.set(this.maxDistance, distance);
    }

    @Override
    public boolean handleFluidAcceleration(@Nonnull Tag<Fluid> fluidTag) {
        return false;
    }

    @Override
    protected void registerData() {
        dataManager.register(playerUUID, Optional.empty());
        dataManager.register(beamLevel, 0);
        dataManager.register(maxDistance, 32);
    }

    @Override
    public boolean isInLava() {
        return false;
    }

    @Override
    public void writeAdditional(@Nonnull CompoundNBT compound) {
        if (dataManager.get(playerUUID).isPresent()) {
            compound.putUniqueId("playerID", dataManager.get(playerUUID).get());
        }
        compound.putInt("beamLevel", dataManager.get(beamLevel));
        compound.putInt("maxDistance", dataManager.get(maxDistance));
    }

    @Override
    public void readAdditional(@Nonnull CompoundNBT compound) {
        if (compound.contains("playerID")) {
            dataManager.set(playerUUID, Optional.of(compound.getUniqueId("playerID")));
        }
        dataManager.set(beamLevel, compound.getInt("beamLevel"));
        dataManager.set(maxDistance, compound.getInt("maxDistance"));
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
        if (isAlive()) {
            if (world instanceof ClientWorld && ticksExisted % 5 == 0) {
                for (int i = 0; i < 2; i++){
                    world.addParticle(AstralParticles.INTENTION_BEAM_PARTICLE.get(), getPosX() + (rand.nextDouble() - rand.nextDouble()) * .5, getPosY() + (rand.nextDouble() - rand.nextDouble()) * .5, getPosZ() + (rand.nextDouble() - rand.nextDouble()) * .5, 0, 0, 0);
                }
            }
            if (ticksExisted >= dataManager.get(maxDistance) * 4) {
                this.remove();
                return;
            }
            Vec3d vec3d = this.getMotion();
            RayTraceResult raytraceresult = ProjectileHelper.rayTrace(this, this.getBoundingBox().expand(vec3d).grow(1.0D), entity -> !entity.isSpectator(), RayTraceContext.BlockMode.OUTLINE, true);
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
    }

    @Override
    public void remove() {
        if (dataManager.get(playerUUID).isPresent()) {
            final PlayerEntity player = world.getPlayerByUuid(dataManager.get(playerUUID).get());
            if (player != null) {
                player.getCapability(AstralAPI.beamTrackerCapability).ifPresent(IBeamTracker::clearIntentionBeam);
            }
        }
        super.remove();
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }
}
