package com.alan199921.astral.capabilities.inner_realm_teleporter;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;

import java.util.HashMap;
import java.util.UUID;

public interface IInnerRealmTeleporterCapability {
    void newPlayer(ServerPlayerEntity player);
    void teleport(ServerPlayerEntity player);
    BlockPos getSpawn(UUID uniqueID);
    HashMap<UUID, BlockPos> getSpawnList();
    void setSpawnList(HashMap<UUID, BlockPos> uuidBlockPosHashMap);
}
