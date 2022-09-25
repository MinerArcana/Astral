package com.alan19.astral.entity.projectile;

import com.alan19.astral.api.AstralAPI;
import com.alan19.astral.api.intentiontracker.IBeamTracker;
import com.alan19.astral.api.intentiontracker.intentiontrackerbehaviors.IIntentionTrackerBehavior;
import com.alan19.astral.blocks.etherealblocks.Ethereal;
import com.alan19.astral.entity.AstralEntities;
import com.alan19.astral.particle.AstralParticles;
import com.alan19.astral.util.IntentionBeamMaterials;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.UUID;

/**
 * Entity for the Intention Tracker. Loosely based off the Mana Burst from Botania.
 * https://github.com/Vazkii/Botania/blob/eed14c95cccea7c496fe674327d0ec8b0e999cc9/src/main/java/vazkii/botania/common/entity/EntityManaBurst.java#L49
 */
public class IntentionBeam extends ThrowableProjectile {
    private static final EntityDataAccessor<Optional<UUID>> playerUUID = SynchedEntityData.defineId(IntentionBeam.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Integer> beamLevel = SynchedEntityData.defineId(IntentionBeam.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> maxDistance = SynchedEntityData.defineId(IntentionBeam.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> touchedBlock = SynchedEntityData.defineId(IntentionBeam.class, EntityDataSerializers.BOOLEAN);

    public IntentionBeam(EntityType<? extends ThrowableProjectile> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    public IntentionBeam(int beamLevel, int maxDistance, Player playerEntity, Level worldIn) {
        super(AstralEntities.INTENTION_BEAM_ENTITY.get(), worldIn);
        entityData.set(IntentionBeam.beamLevel, beamLevel);
        entityData.set(IntentionBeam.maxDistance, maxDistance);
        entityData.set(IntentionBeam.playerUUID, Optional.of(playerEntity.getUUID()));
        entityData.set(IntentionBeam.touchedBlock, false);
    }

    protected void onHit(HitResult result) {
        if (result.getType() == HitResult.Type.BLOCK) {
            final BlockHitResult blockRayTraceResult = (BlockHitResult) result;
            final BlockState blockState = level.getBlockState(blockRayTraceResult.getBlockPos());
            final IIntentionTrackerBehavior intentionTrackerBehavior = AstralAPI.getIntentionTrackerBehavior(blockState.getBlock());
            if (intentionTrackerBehavior != null && entityData.get(playerUUID).isPresent()) {
                intentionTrackerBehavior.onIntentionBeamHit(level.getPlayerByUUID(entityData.get(playerUUID).get()), entityData.get(beamLevel), blockRayTraceResult, blockState);
                remove(RemovalReason.DISCARDED);
            }
            else if (blockState.getBlock() instanceof Ethereal || !IntentionBeamMaterials.getMaterialsForLevel(entityData.get(beamLevel)).contains(blockState.getMaterial())) {
                entityData.set(touchedBlock, true);
                remove(RemovalReason.DISCARDED);
            }
        }
    }

    public void setPlayer(Player player) {
        entityData.set(playerUUID, Optional.of(player.getUUID()));
    }

    public void setLevel(int level) {
        entityData.set(beamLevel, level);
    }

    public void setMaxDistance(int distance) {
        entityData.set(maxDistance, distance);
    }

    @Override
    public boolean updateFluidHeightAndDoFluidPushing(@NotNull TagKey<Fluid> pFluidTag, double pMotionScale) {
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
    public void addAdditionalSaveData(@Nonnull CompoundTag compound) {
        if (entityData.get(playerUUID).isPresent()) {
            compound.putUUID("playerID", entityData.get(playerUUID).get());
        }
        compound.putInt("beamLevel", entityData.get(beamLevel));
        compound.putInt("maxDistance", entityData.get(maxDistance));
        compound.putBoolean("touchedBlock", entityData.get(touchedBlock));
    }

    @Override
    public void readAdditionalSaveData(@Nonnull CompoundTag compound) {
        if (compound.contains("playerID")) {
            entityData.set(playerUUID, Optional.of(compound.getUUID("playerID")));
        }
        entityData.set(beamLevel, compound.getInt("beamLevel"));
        entityData.set(maxDistance, compound.getInt("maxDistance"));
        entityData.set(touchedBlock, compound.getBoolean("touchedBlock"));
    }

    @Override
    @Nonnull
    public Packet<?> getAddEntityPacket() {
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
            float f = Mth.sqrt((float) (x * x + z * z));
            setXRot((float) (Mth.atan2(y, f) * (180F / (float) Math.PI)));
            setYRot((float) (Mth.atan2(x, z) * (180F / (float) Math.PI)));
            this.xRotO = this.getXRot();
            this.yRotO = this.getYRot();
            this.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot());
        }

    }

    /**
     * Similar to setArrowHeading, it's point the throwable entity to a x, y, z direction.
     */
    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        Vec3 vec3d = (new Vec3(x, y, z)).normalize().add(this.random.nextGaussian() * 0.0075F * inaccuracy, this.random.nextGaussian() * 0.0075F * inaccuracy, this.random.nextGaussian() * 0.0075F * inaccuracy).scale(velocity);
        this.setDeltaMovement(vec3d);
        float f = Mth.sqrt((float) vec3d.horizontalDistanceSqr());
        setYRot((float) (Mth.atan2(vec3d.x, z) * (180F / (float) Math.PI)));
        setXRot((float) (Mth.atan2(vec3d.y, f) * (180F / (float) Math.PI)));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
    }

    /**
     * Called to update the entity's position/logic. Adapted from LLamaSpitEntity.java
     */
    @Override
    public void tick() {
        super.tick();
        if (isAlive()) {
            if (level instanceof ServerLevel && tickCount % 5 == 0 && entityData.get(playerUUID).isPresent()) {
                final ServerLevel world = (ServerLevel) this.level;
                final ServerPlayer player = (ServerPlayer) world.getEntity(entityData.get(playerUUID).get());
                if (player != null) {
                    world.sendParticles(player, AstralParticles.INTENTION_BEAM_PARTICLE.get(), true, getX(), getY(), getZ(), 2, (random.nextDouble() - random.nextDouble()) * .5, (random.nextDouble() - random.nextDouble()), (random.nextDouble() - random.nextDouble()) * .5, 0);
                }
            }
            if (tickCount >= entityData.get(maxDistance) * 4) {
                this.remove(RemovalReason.DISCARDED);
                return;
            }
            Vec3 vec3d = this.getDeltaMovement();
            HitResult raytraceresult = ProjectileUtil.getHitResult(this, this::canHitEntity);
            if (raytraceresult.getType() != HitResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                this.onHit(raytraceresult);
            }

            double d0 = this.getX() + vec3d.x;
            double d1 = this.getY() + vec3d.y;
            double d2 = this.getZ() + vec3d.z;
            float f = Mth.sqrt((float) vec3d.horizontalDistanceSqr());
            setYRot((float) (Mth.atan2(vec3d.x, vec3d.z) * (double) (180F / (float) Math.PI)));
            setXRot((float) (Mth.atan2(vec3d.y, f) * (double) (180F / (float) Math.PI)));
            while (this.getXRot() - this.xRotO < -180.0F) {
                this.xRotO -= 360.0F;
            }

            while (this.getXRot() - this.xRotO >= 180.0F) {
                this.xRotO += 360.0F;
            }

            while (this.getYRot() - this.yRotO < -180.0F) {
                this.yRotO -= 360.0F;
            }

            while (this.getYRot() - this.yRotO >= 180.0F) {
                this.yRotO += 360.0F;
            }

            setXRot(Mth.lerp(0.2F, this.xRotO, this.getXRot()));
            setYRot(Mth.lerp(0.2F, this.yRotO, this.getYRot()));
            if (!this.isNoGravity()) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.06F, 0.0D));
            }

            this.setPos(d0, d1, d2);
        }
    }

    @Override
    public void remove(RemovalReason pReason) {
        if (level instanceof ServerLevel) {
            ((ServerLevel) level).sendParticles(AstralParticles.INTENTION_BEAM_PARTICLE.get(), getX(), getY(), getZ(), 5, random.nextDouble(), random.nextDouble(), random.nextDouble(), random.nextDouble() / 4);
        }
        if (entityData.get(playerUUID).isPresent()) {
            final Player player = level.getPlayerByUUID(entityData.get(playerUUID).get());
            if (player != null) {
                player.getCapability(AstralAPI.BEAM_TRACKER_CAPABILITY).ifPresent(IBeamTracker::clearIntentionBeam);
            }
        }
        super.remove(pReason);
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }
}
