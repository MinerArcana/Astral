package com.alan199921.astral.capabilities.inner_realm_chunk_claim;

import net.minecraft.world.chunk.IChunk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public interface IInnerRealmChunkClaimCapability {
    void claimChunk(UUID player, IChunk chunk);
    boolean doesPlayerHaveAccess(UUID player, IChunk chunk);
    HashMap<UUID, ArrayList<IChunk>> getClaimedChunkMap();
    void setClaimedChunkMap(HashMap<UUID, ArrayList<IChunk>> claimedChunkMap);
}
