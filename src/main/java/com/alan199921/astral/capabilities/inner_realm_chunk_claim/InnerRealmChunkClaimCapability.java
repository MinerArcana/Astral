package com.alan199921.astral.capabilities.inner_realm_chunk_claim;

import net.minecraft.world.chunk.IChunk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class InnerRealmChunkClaimCapability implements IInnerRealmChunkClaimCapability {
    private HashMap<UUID, ArrayList<IChunk>> claimedChunksMap;

    @Override
    public void claimChunk(UUID player, IChunk chunk) {
        if (claimedChunksMap.containsKey(player)){
            claimedChunksMap.get(player).add(chunk);
        }
        else{
            ArrayList<IChunk> claimedChunks = new ArrayList<>();
            claimedChunks.add(chunk);
            this.claimedChunksMap.put(player, claimedChunks);
        }
    }

    @Override
    public boolean doesPlayerHaveAccess(UUID player, IChunk chunk) {
        return claimedChunksMap.getOrDefault(player, new ArrayList<>()).contains(chunk);
    }

    @Override
    public HashMap<UUID, ArrayList<IChunk>> getClaimedChunkMap() {
        return claimedChunksMap;
    }

    @Override
    public void setClaimedChunkMap(HashMap<UUID, ArrayList<IChunk>> claimedChunkMap) {
        this.claimedChunksMap = claimedChunkMap;
    }
}
