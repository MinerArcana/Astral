package com.alan19.astral.entity.projectile;

import com.alan19.astral.api.AstralAPI;
import com.alan19.astral.api.intentiontracker.IBeamTracker;
import com.alan19.astral.api.intentiontracker.intentiontrackerbehaviors.IIntentionTrackerBehavior;
import com.alan19.astral.blocks.etherealblocks.Ethereal;
import com.alan19.astral.entity.AstralEntities;
import com.alan19.astral.particle.AstralParticles;
import com.alan19.astral.util.IntentionBeamMaterials;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tags.ITag;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.UUID;

/**
 * Entity for the Intention Tracker. Loosely based off the Mana Burst from Botania.
 * https://github.com/Vazkii/Botania/blob/eed14c95cccea7c496fe674327d0ec8b0e999cc9/src/main/java/vazkii/botania/common/entity/EntityManaBurst.java#L49
 */
public class IntentionBeam extends ThrowableEntity {
    private static final DataParameter<Optional<UUID>> playerUUID = EntityDataManager.defineId(IntentionBeam.class, DataSerializers.OPTIONAL_UUID);
    private static final DataParameter<Integer> beamLevel = EntityDataManager.defineId(IntentionBeam.class, DataSerializers.INT);
    private static final DataParameter<Integer> maxDistance = EntityDataManager.defineId(IntentionBeam.class, DataSerializers.INT);
    private static final DataParameter<Boolean> touchedBlock = EntityDataManager.defineId(IntentionBeam.class, DataSerializers.BOOLEAN);

    public IntentionBeam(EntityType<? extends ThrowableEntity> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    public IntentionBeam(int beamLevel, int maxDistance, PlayerEntity playerEntity, World worldIn) {
        super(AstralEntities.INTENTION_BEAM_ENTITY.get(), worldIn);
        entityData.set(IntentionBeam.beamLevel, beamLevel);
        entityData.set(IntentionBeam.maxDistance, maxDistance);
        entityData.set(IntentionBeam.playerUUID, Optional.of(playerEntity.getUUID()));
        entityData.set(IntentionBeam.touchedBlock, false);
    }

    protected void onHit(RayTraceResult result) {
        if (result.getType() == RayTraceResult.Type.BLOCK) {
            final BlockRayTraceResult blockRayTraceResult = (BlockRayTraceResult) result;
            final BlockState blockState = level.getBlockState(blockRayTraceResult.getBlockPos());
            final IIntentionTrackerBehavior intentionTrackerBehavior = AstralAPI.getIntentionTrackerBehavior(blockState.getBlock());
            if (intentionTrackerBehavior != null && entityData.get(playerUUID).isPresent()) {
                intentionTrackerBehavior.onIntentionBeamHit(level.getPlayerByUUID(entityData.get(playerUUID).get()), entityData.get(beamLevel), blockRayTraceResult, blockState);
                remove();
            }
            else if (blockState.getBlock() instanceof Ethereal || !IntentionBeamMaterials.getMaterialsForLevel(entityData.get(beamLevel)).contains(blockState.getMaterial())){
                entityData.set(touchedBlock, true);
                remove();
            }
        }
    }

    public void setPlayer(PlayerEntity player) {
        entityData.set(playerUUID, Optional.of(player.getUUID()));
    }

    public void setLevel(int level) {
        entityData.set(beamLevel, level);
    }

    public void setMaxDistance(int distance) {
        entityData.set(maxDistance, distance);
    }

    @Override
    public boolean updateFluidHeightAndDoFluidPushing(@Nonnull ITag<Fluid> fluidTag, double p_210500_2_) {
        return false;
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(playerUUID, Optional.empty());
        entityData.define(beamLevel, 0);
        entityData.define(maxDistance, 32);
        entityData.define(touchedBlock, false);
    }

    @Override
    public boolean isInLava() {
        return false;
    }

    @Override
    public void addAdditionalSaveData(@Nonnull CompoundNBT compound) {
        if (entityData.get(playerUUID).isPresent()) {
            compound.putUUID("playerID", entityData.get(playerUUID).get());
        }
        compound.putInt("beamLevel", entityData.get(beamLevel));
        compound.putInt("maxDistance", entityData.get(maxDistance));
        compound.putBoolean("touchedBlock", entityData.get(touchedBlock));
    }

    @Override
    public void readAdditionalSaveData(@Nonnull CompoundNBT compound) {
        if (compound.contains("playerID")) {
            entityData.set(playerUUID, Optional.of(compound.getUUID("playerID")));
        }
        entityData.set(beamLevel, compound.getInt("beamLevel"));
        entityData.set(maxDistance, compound.getInt("maxDistance"));
        entityData.set(touchedBlock, compound.getBoolean("touchedBlock"));
    }

    @Override
    @Nonnull
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    /**
     * Updates the entity motion clientside, called by packets from the server
     */
    @Override
    @OnlyIn(Dist.CLIENT)
    public void lerpMotion(double x, double y, double z) {
        this.setDeltaMovement(x, y, z);
        if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
            float f = MathHelper.sqrt(x * x + z * z);
            this.xRot = (float) (MathHelper.atan2(y, f) * (double) (180F / (float) Math.PI));
            this.yRot = (float) (MathHelper.atan2(x, z) * (double) (180F / (float) Math.PI));
            this.xRotO = this.xRot;
            this.yRotO = this.yRot;
            this.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, this.xRot);
        }

    }

    /**
     * Similar to setArrowHeading, it's point the throwable entity to a x, y, z direction.
     */
    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        Vector3d vec3d = (new Vector3d(x, y, z)).normalize().add(this.random.nextGaussian() * (double) 0.0075F * (double) inaccuracy, this.random.nextGaussian() * (double) 0.0075F * (double) inaccuracy, this.random.nextGaussian() * (double) 0.0075F * (double) inaccuracy).scale(velocity);
        this.setDeltaMovement(vec3d);
        float f = MathHelper.sqrt(getHorizontalDistanceSqr(vec3d));
        this.yRot = (float) (MathHelper.atan2(vec3d.x, z) * (double) (180F / (float) Math.PI));
        this.xRot = (float) (MathHelper.atan2(vec3d.y, f) * (double) (180F / (float) Math.PI));
        this.yRotO = this.yRot;
        this.xRotO = this.xRot;
    }

    /**
     * Called to update the entity's position/logic. Adapted from LLamaSpitEntity.java
     */
    @Override
    public void tick() {
        super.tick();
        if (isAlive()) {
            if (level instanceof ServerWorld && tickCount % 5 == 0 && entityData.get(playerUUID).isPresent()) {
                final ServerWorld world = (ServerWorld) this.level;
                final ServerPlayerEntity player = (ServerPlayerEntity) world.getEntity(entityData.get(playerUUID).get());
                if (player != null) {
                    world.getWorldServer().sendParticles(player, AstralParticles.INTENTION_BEAM_PARTICLE.get(), true, getX(), getY(), getZ(), 2, (random.nextDouble() - random.nextDouble()) * .5, (random.nextDouble() - random.nextDouble()), (random.nextDouble() - random.nextDouble()) * .5, 0);
                }
            }
            if (tickCount >= entityData.get(maxDistance) * 4) {
                this.remove();
                return;
            }
            Vector3d vec3d = this.getDeltaMovement();
            RayTraceResult raytraceresult = ProjectileHelper.getHitResult(this, this::canHitEntity);
            if (raytraceresult.getType() != RayTraceResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                this.onHit(raytraceresult);
            }

            double d0 = this.getX() + vec3d.x;
            double d1 = this.getY() + vec3d.y;
            double d2 = this.getZ() + vec3d.z;
            float f = MathHelper.sqrt(getHorizontalDistanceSqr(vec3d));
            this.yRot = (float) (MathHelper.atan2(vec3d.x, vec3d.z) * (double) (180F / (float) Math.PI));

            this.xRot = (float) (MathHelper.atan2(vec3d.y, f) * (double) (180F / (float) Math.PI));
            while (this.xRot - this.xRotO < -180.0F) {
                this.xRotO -= 360.0F;
            }

            while (this.xRot - this.xRotO >= 180.0F) {
                this.xRotO += 360.0F;
            }

            while (this.yRot - this.yRotO < -180.0F) {
                this.yRotO -= 360.0F;
            }

            while (this.yRot - this.yRotO >= 180.0F) {
                this.yRotO += 360.0F;
            }

            this.xRot = MathHelper.lerp(0.2F, this.xRotO, this.xRot);
            this.yRot = MathHelper.lerp(0.2F, this.yRotO, this.yRot);
            if (!this.isNoGravity()) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.06F, 0.0D));
            }

            this.setPos(d0, d1, d2);
        }
    }

    @Override
    public void remove() {
        if (level instanceof ServerWorld) {
            ((ServerWorld)level).sendParticles(AstralParticles.INTENTION_BEAM_PARTICLE.get(), getX(), getY(), getZ(), 5, random.nextDouble(), random.nextDouble(), random.nextDouble(), random.nextDouble() / 4);
        }
        if (entityData.get(playerUUID).isPresent()) {
            final PlayerEntity player = level.getPlayerByUUID(entityData.get(playerUUID).get());
            if (player != null) {
                player.getCapability(AstralAPI.beamTrackerCapability).ifPresent(IBeamTracker::clearIntentionBeam);
            }
        }
        super.remove();
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }
}
