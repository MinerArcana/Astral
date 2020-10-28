package com.alan19.astral.api.bodytracker;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Capability to keep track of the NBT of the body if it has died (maybe it could only track if the body's alive)
 */
public interface IBodyTracker extends INBTSerializable<CompoundNBT> {
    Map<UUID, BodyInfo> getBodyTrackerMap();
    Optional<BodyInfo> getBodyInfo(PlayerEntity playerEntity);
    void setBodyInfo(BodyInfo bodyInfo, PlayerEntity playerEntity);

    void mergeBodies(PlayerEntity playerEntity, ServerWorld world);
    void updatePlayer(ServerPlayerEntity playerEntity, ServerWorld world);

    void removeBody(UUID playerID);
}
