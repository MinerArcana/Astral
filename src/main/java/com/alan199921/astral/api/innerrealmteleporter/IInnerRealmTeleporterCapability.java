package com.alan199921.astral.api.innerrealmteleporter;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashMap;
import java.util.UUID;

public interface IInnerRealmTeleporterCapability extends INBTSerializable<CompoundNBT> {
    void prepareSpawnChunk(PlayerEntity player);

    void teleport(PlayerEntity player);

    BlockPos getSpawn(PlayerEntity player);

    HashMap<UUID, BlockPos> getSpawnList();

    void setSpawnList(HashMap<UUID, BlockPos> uuidBlockPosHashMap);

    void deserializeNBT(CompoundNBT nbt);

    CompoundNBT serializeNBT();
}
