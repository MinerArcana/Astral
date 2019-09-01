package com.alan199921.astral.capabilities.inner_realm_chunk_claim;

import com.alan199921.astral.dimensions.innerrealm.InnerRealmUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.ChunkPos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class InnerRealmChunkClaimCapability implements IInnerRealmChunkClaimCapability {
    private HashMap<UUID, ArrayList<ChunkPos>> claimedChunksMap = new HashMap<>();

    @Override
    public void claimChunk(PlayerEntity player, ChunkPos chunk) {
        UUID playerID = player.getUniqueID();
        if (claimedChunksMap.containsKey(playerID)){
            claimedChunksMap.get(playerID).add(chunk);
        }
        else{
            ArrayList<ChunkPos> claimedChunks = new ArrayList<>();
            claimedChunks.add(chunk);
            this.claimedChunksMap.put(playerID, claimedChunks);
        }
        if (playerHaveAccessToChunk(player, chunk)){
            InnerRealmUtils innerRealmUtils = new InnerRealmUtils();
            innerRealmUtils.generateInnerRealmChunk(player.getEntityWorld(), chunk);
        }
    }

    @Override
    public boolean playerHaveAccessToChunk(PlayerEntity player, ChunkPos chunk) {
        return claimedChunksMap.getOrDefault(player.getUniqueID(), new ArrayList<>()).contains(chunk);
    }

    @Override
    public HashMap<UUID, ArrayList<ChunkPos>> getClaimedChunkMap() {
        return claimedChunksMap;
    }

    @Override
    public void setClaimedChunkMap(HashMap<UUID, ArrayList<ChunkPos>> claimedChunkMap) {
        this.claimedChunksMap = claimedChunkMap;
    }
}
