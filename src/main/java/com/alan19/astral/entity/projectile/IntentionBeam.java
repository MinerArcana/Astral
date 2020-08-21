package com.alan19.astral.entity.projectile;

import com.alan19.astral.blocks.IntentionBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tags.Tag;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.UUID;

/**
 * Entity for the Intention Tracker. Based off the Mana Burst from Botania.
 * https://github.com/Vazkii/Botania/blob/eed14c95cccea7c496fe674327d0ec8b0e999cc9/src/main/java/vazkii/botania/common/entity/EntityManaBurst.java#L49
 */
public class IntentionBeam extends ThrowableEntity {
    private UUID playerUUID;
    private int beamLevel;
    private int maxDistance;

    protected IntentionBeam(PlayerEntity playerEntity, EntityType<? extends ThrowableEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public IntentionBeam(EntityType<? extends ThrowableEntity> entityType, World world){
        super(entityType, world);
    }


    @Override
    protected void onImpact(RayTraceResult result) {
        if (result.getType() == RayTraceResult.Type.BLOCK){
            final BlockRayTraceResult blockRayTraceResult = (BlockRayTraceResult) result;
            final BlockState blockState = world.getBlockState(blockRayTraceResult.getPos());
            if (blockState.getBlock() instanceof IntentionBlock){
                ((IntentionBlock) blockState.getBlock()).onIntentionTrackerHit(world.getPlayerByUuid(playerUUID), beamLevel, blockRayTraceResult, blockState);
            }
            else {
                //TODO Teleportation effect
            }
        }
    }

    public void setPlayer(PlayerEntity player){
        playerUUID = player.getUniqueID();
    }

    public void setLevel(int level){
        beamLevel = level;
    }

    public void setMaxDistance(int distance){
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
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putUniqueId("playerID", playerUUID);
        compound.putInt("beamLevel", beamLevel);
        compound.putInt("maxDistance", maxDistance);
    }
}
