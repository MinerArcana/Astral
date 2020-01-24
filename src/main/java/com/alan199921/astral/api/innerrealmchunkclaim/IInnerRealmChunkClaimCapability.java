package com.alan199921.astral.api.innerrealmchunkclaim;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.INBT;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public interface IInnerRealmChunkClaimCapability extends INBTSerializable {
    void handleChunkClaim(PlayerEntity player, Chunk chunk);

    boolean playerHasClaimedChunk(PlayerEntity player, ChunkPos chunk);

    HashMap<UUID, ArrayList<ChunkPos>> getClaimedChunkMap();

    void setClaimedChunkMap(HashMap<UUID, ArrayList<ChunkPos>> claimedChunkMap);

    void addChunkToPlayerClaims(PlayerEntity player, ChunkPos chunk);

    INBT serializeNBT();

    void deserializeNBT(INBT nbt);
}
