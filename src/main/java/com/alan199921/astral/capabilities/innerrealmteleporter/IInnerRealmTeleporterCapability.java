package com.alan199921.astral.capabilities.innerrealmteleporter;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.INBT;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashMap;
import java.util.UUID;

public interface IInnerRealmTeleporterCapability extends INBTSerializable {
    void newPlayer(ServerPlayerEntity player);
    void teleport(ServerPlayerEntity player);
    BlockPos getSpawn(UUID uniqueID);
    HashMap<UUID, BlockPos> getSpawnList();
    void setSpawnList(HashMap<UUID, BlockPos> uuidBlockPosHashMap);

    void deserializeNBT(INBT nbt);
    INBT serializeNBT();
}
