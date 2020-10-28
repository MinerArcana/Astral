package com.alan19.astral.entity.projectile;

import com.alan19.astral.api.AstralAPI;
import com.alan19.astral.api.intentiontracker.IBeamTracker;
import com.alan19.astral.api.intentiontracker.intentiontrackerbehaviors.IIntentionTrackerBehavior;
import com.alan19.astral.blocks.etherealblocks.Ethereal;
import com.alan19.astral.effects.AstralEffects;
import com.alan19.astral.entity.AstralEntities;
import com.alan19.astral.particle.AstralParticles;
import com.alan19.astral.util.ExperienceHelper;
import com.alan19.astral.util.IntentionBeamMaterials;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SSpawnParticlePacket;
import net.minecraft.tags.Tag;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

/**
 * Entity for the Intention Tracker. Loosely based off the Mana Burst from Botania.
 * https://github.com/Vazkii/Botania/blob/eed14c95cccea7c496fe674327d0ec8b0e999cc9/src/main/java/vazkii/botania/common/entity/EntityManaBurst.java#L49
 */
public class IntentionBeam extends Entity implements IProjectile {
    private static final DataParameter<Optional<UUID>> playerUUID = EntityDataManager.createKey(IntentionBeam.class, DataSerializers.OPTIONAL_UNIQUE_ID);
    private static final DataParameter<Integer> beamLevel = EntityDataManager.createKey(IntentionBeam.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> maxDistance = EntityDataManager.createKey(IntentionBeam.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> touchedBlock = EntityDataManager.createKey(IntentionBeam.class, DataSerializers.BOOLEAN);
    private static final DataParameter<BlockPos> spawnLocation = EntityDataManager.createKey(IntentionBeam.class, DataSerializers.BLOCK_POS);

    public IntentionBeam(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    public IntentionBeam(int beamLevel, int maxDistance, PlayerEntity playerEntity, World worldIn) {
        super(AstralEntities.INTENTION_BEAM_ENTITY.get(), worldIn);
        dataManager.set(IntentionBeam.beamLevel, beamLevel);
        dataManager.set(IntentionBeam.maxDistance, maxDistance);
        dataManager.set(IntentionBeam.playerUUID, Optional.of(playerEntity.getUniqueID()));
        dataManager.set(IntentionBeam.touchedBlock, false);
    }

    public static void teleportPlayer(ServerPlayerEntity sender, IntentionBeam intentionBeam) {
        final BlockPos destination = new BlockPos(intentionBeam.getPosX(), intentionBeam.getPosY() - sender.getEyeHeight(), intentionBeam.getPosZ());
        final double teleportDistance = new BlockPos(sender.getPosX(), sender.getPosYEye(), sender.getPosZ()).distanceSq(intentionBeam.getPosX(), intentionBeam.getPosY(), intentionBeam.getPosZ(), true);
        final int xpCost = (int) Math.floor(1 + teleportDistance / 50);
        //Teleport if teleport distance is greater than 1, and if sender
        if (teleportDistance > 1 && ExperienceHelper.getPlayerXP(sender) >= xpCost && !sender.getServerWorld().getBlockState(destination).isCollisionShapeOpaque(sender.getServerWorld(), destination) && !sender.getServerWorld().getBlockState(intentionBeam.getPosition()).isCollisionShapeOpaque(sender.getServerWorld(), intentionBeam.getPosition())) {
            sender.teleportKeepLoaded(intentionBeam.getPosX(), intentionBeam.getPosY() - sender.getEyeHeight(), intentionBeam.getPosZ());
            ExperienceHelper.drainPlayerXP(sender, xpCost);
            Random rand = sender.getRNG();
            sender.getServerWorld().spawnParticle(AstralParticles.INTENTION_BEAM_PARTICLE.get(), sender.getPosX() + (rand.nextDouble() - rand.nextDouble()), sender.getPosY() + (rand.nextDouble() - rand.nextDouble()), sender.getPosZ() + (rand.nextDouble() - rand.nextDouble()), 7, 0, 0, 0, .1);
            intentionBeam.playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, 1F, 1F);
        }
        intentionBeam.remove();
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        dataManager.set(spawnLocation, getPosition());
    }

    protected void onHit(RayTraceResult raytraceresult) {
        final BlockState blockState = world.getBlockState(getPosition());
        final IIntentionTrackerBehavior intentionTrackerBehavior = AstralAPI.getIntentionTrackerBehavior(blockState.getBlock());
        if (intentionTrackerBehavior != null && dataManager.get(playerUUID).isPresent()) {
            intentionTrackerBehavior.onIntentionBeamHit(world.getPlayerByUuid(dataManager.get(playerUUID).get()), dataManager.get(beamLevel), (BlockRayTraceResult) raytraceresult, blockState);
            remove();
        }
        else if (blockState.getBlock() instanceof Ethereal || !IntentionBeamMaterials.getMaterialsForLevel(dataManager.get(beamLevel)).contains(blockState.getMaterial())) {
            dataManager.set(touchedBlock, true);
            remove();
        }
    }

    public void setPlayer(PlayerEntity player) {
        dataManager.set(playerUUID, Optional.of(player.getUniqueID()));
    }

    public void setLevel(int level) {
        dataManager.set(beamLevel, level);
    }

    public void setMaxDistance(int distance) {
        dataManager.set(maxDistance, distance);
    }

    @Override
    public boolean handleFluidAcceleration(@Nonnull Tag<Fluid> fluidTag) {
        return false;
    }

    @Override
    protected void registerData() {
        dataManager.register(playerUUID, Optional.empty());
        dataManager.register(beamLevel, 0);
        dataManager.register(maxDistance, 64);
        dataManager.register(touchedBlock, false);
        dataManager.register(spawnLocation, getPosition());
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
        compound.putBoolean("touchedBlock", dataManager.get(touchedBlock));
        compound.put("spawnLocation", NBTUtil.writeBlockPos(dataManager.get(spawnLocation)));
    }

    @Override
    public void readAdditional(@Nonnull CompoundNBT compound) {
        if (compound.contains("playerID")) {
            dataManager.set(playerUUID, Optional.of(compound.getUniqueId("playerID")));
        }
        dataManager.set(beamLevel, compound.getInt("beamLevel"));
        dataManager.set(maxDistance, compound.getInt("maxDistance"));
        dataManager.set(touchedBlock, compound.getBoolean("touchedBlock"));
        dataManager.set(spawnLocation, NBTUtil.readBlockPos(compound.getCompound("spawnLocation")));
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
            if (world instanceof ServerWorld && ticksExisted % 5 == 0) {
                SSpawnParticlePacket intentionBeamParticlePacket = new SSpawnParticlePacket(AstralParticles.INTENTION_BEAM_PARTICLE.get(), false, getPosX(), getPosY(), getPosZ(), (rand.nextFloat() - rand.nextFloat()) * .5f, (rand.nextFloat() - rand.nextFloat()) * .5f, (rand.nextFloat() - rand.nextFloat()) * .5f, 0f, 2);
                dataManager.get(playerUUID)
                        .map(uuid -> ((ServerPlayerEntity) world.getPlayerByUuid(uuid)))
                        .ifPresent(serverPlayerEntity -> serverPlayerEntity.connection.sendPacket(intentionBeamParticlePacket));


            }
            final Optional<PlayerEntity> playerEntityOptional = dataManager.get(playerUUID).map(uuid -> world.getPlayerByUuid(uuid));
            if (world instanceof ServerWorld && Math.pow(dataManager.get(maxDistance), 2) < getPosition().distanceSq(dataManager.get(spawnLocation)) || playerEntityOptional.map(playerEntity -> !playerEntity.isPotionActive(AstralEffects.ASTRAL_TRAVEL.get())).orElse(true)) {
                this.remove();
                if (playerEntityOptional.isPresent() && world instanceof ServerWorld) {
                    final PlayerEntity playerEntity = playerEntityOptional.get();
                    if (playerEntity.isPotionActive(AstralEffects.ASTRAL_TRAVEL.get())) {
                        teleportPlayer((ServerPlayerEntity) playerEntity, this);
                    }
                }
                return;
            }
            Vec3d vec3d = this.getMotion();
            RayTraceResult raytraceresult = ProjectileHelper.rayTrace(this, this.getBoundingBox().expand(vec3d).grow(1.0D), entity -> !entity.isSpectator(), RayTraceContext.BlockMode.OUTLINE, true);
            if (!world.isAirBlock(getPosition())) {
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
            if (!this.hasNoGravity()) {
                this.setMotion(this.getMotion().add(0.0D, -0.06F, 0.0D));
            }

            this.setPosition(d0, d1, d2);
        }
    }

    @Override
    public void remove() {
        if (world instanceof ServerWorld) {
            ((ServerWorld)world).spawnParticle(AstralParticles.INTENTION_BEAM_PARTICLE.get(), getPosX(), getPosY(), getPosZ(), 5, rand.nextDouble(), rand.nextDouble(), rand.nextDouble(), rand.nextDouble() / 4);
        }
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
