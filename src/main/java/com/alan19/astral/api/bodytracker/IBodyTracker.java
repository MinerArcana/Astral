package com.alan19.astral.api.bodytracker;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Map;
import java.util.UUID;

public interface IBodyTracker extends INBTSerializable<CompoundTag> {
    Map<UUID, CompoundTag> getBodyTrackerMap();

    void setBodyNBT(UUID uuid, CompoundTag nbt, ServerLevel world);

    void mergePlayerWithBody(ServerPlayer serverPlayerEntity, ServerLevel world);
}
