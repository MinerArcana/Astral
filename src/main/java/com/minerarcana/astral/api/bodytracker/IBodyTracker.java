package com.minerarcana.astral.api.bodytracker;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Map;
import java.util.UUID;

public interface IBodyTracker extends INBTSerializable<CompoundTag> {
    Map<UUID, CompoundTag> getBodyTrackerMap();

    void setBodyNBT(Player player);

    void mergePlayerWithBody(ServerPlayer serverPlayerEntity);
}
