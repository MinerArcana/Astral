package com.alan199921.astral.capabilities.inner_realm_chunk_claim;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.IChunk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public interface IInnerRealmChunkClaimCapability {
    void handleChunkClaim(PlayerEntity player, IChunk chunk);
    boolean playerHasClaimedChunk(PlayerEntity player, ChunkPos chunk);
    HashMap<UUID, ArrayList<ChunkPos>> getClaimedChunkMap();
    void setClaimedChunkMap(HashMap<UUID, ArrayList<ChunkPos>> claimedChunkMap);
    void addChunkToPlayerClaims(PlayerEntity player, ChunkPos chunk);
}
