package com.alan199921.astral.capabilities.inner_realm_chunk_claim;

import com.alan199921.astral.dimensions.innerrealm.InnerRealmUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.IChunk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class InnerRealmChunkClaimCapability implements IInnerRealmChunkClaimCapability {
    private HashMap<UUID, ArrayList<ChunkPos>> claimedChunksMap = new HashMap<>();

    @Override
    public void handleChunkClaim(PlayerEntity player, IChunk chunk) {
        //If player does not have access to a chunk, create a new box and check it's adjacent chunks for boxes and
        //break down the appropriate walls
        InnerRealmUtils innerRealmUtils = new InnerRealmUtils();
        if (!playerHasClaimedChunk(player, chunk.getPos())){
            innerRealmUtils.generateInnerRealmChunk(player.getEntityWorld(), chunk);
        }
        addChunkToPlayerClaims(player, chunk.getPos());
        for (int i = 0; i < 4; i++){
            IChunk adjacentChunk = innerRealmUtils.getAdjacentChunk(chunk.getPos().asBlockPos(), i, player.getEntityWorld());
            if (playerHasClaimedChunk(player, adjacentChunk.getPos())){
                innerRealmUtils.destroyPlaneMeridian(player.getEntityWorld(), chunk, i);
                innerRealmUtils.destroyPlaneMeridian(player.getEntityWorld(), adjacentChunk, (i + 2) % 4);
            }
        }
    }

    @Override
    public boolean playerHasClaimedChunk(PlayerEntity player, ChunkPos chunk) {
        for (ArrayList<ChunkPos> claimedChunks : claimedChunksMap.values()) {
            if (claimedChunks.contains(chunk)){
                return true;
            }
        }
        return false;
    }

    @Override
    public HashMap<UUID, ArrayList<ChunkPos>> getClaimedChunkMap() {
        return claimedChunksMap;
    }

    @Override
    public void setClaimedChunkMap(HashMap<UUID, ArrayList<ChunkPos>> claimedChunkMap) {
        this.claimedChunksMap = claimedChunkMap;
    }

    @Override
    public void addChunkToPlayerClaims(PlayerEntity player, ChunkPos chunk) {
        UUID playerID = player.getUniqueID();
        //Add player to HashMap
        if (claimedChunksMap.containsKey(playerID)){
            claimedChunksMap.get(playerID).add(chunk);
        }
        else{
            ArrayList<ChunkPos> claimedChunks = new ArrayList<>();
            claimedChunks.add(chunk);
            this.claimedChunksMap.put(playerID, claimedChunks);
        }
    }
}
