package com.alan19.astral.api.bodytracker;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Map;
import java.util.UUID;

public interface IBodyTracker extends INBTSerializable<CompoundNBT> {
    Map<UUID, CompoundNBT> getBodyTrackerMap();

    void setBodyNBT(UUID uuid, CompoundNBT nbt, ServerWorld world);

    void mergePlayerWithBody(ServerPlayerEntity serverPlayerEntity, ServerWorld world);
}
