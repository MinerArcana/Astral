package com.alan199921.astral.capabilities;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;

import java.util.HashMap;
import java.util.UUID;

public interface IPocketDimTeleporter {
    void setSpawn(BlockPos pos);
    void newPlayer(ServerPlayerEntity player);
    void teleport(ServerPlayerEntity player, DimensionType dimension);
    BlockPos getSpawn();
    HashMap<UUID, BlockPos> getSpawnList();
}
