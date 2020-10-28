package com.alan19.astral.api.innerrealmchunkclaim;

import com.alan19.astral.dimensions.innerrealm.InnerRealmUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class InnerRealmChunkClaim implements IInnerRealmChunkClaim {
    private HashMap<UUID, ArrayList<ChunkPos>> claimedChunksMap = new HashMap<>();

    @Override
    public void handleChunkClaim(PlayerEntity player, Chunk chunk) {
        //If player does not have access to a chunk, create a new box and check it's adjacent chunks for boxes and
        //break down the appropriate walls
        if (!playerHasClaimedChunk(player, chunk.getPos())) {
            InnerRealmUtils.generateInnerRealmChunk(player.getEntityWorld(), chunk);
        }
        addChunkToPlayerClaims(player, chunk.getPos());
        for (int i = 0; i < 4; i++) {
            Chunk adjacentChunk = InnerRealmUtils.getAdjacentChunk(chunk.getPos().asBlockPos(), i, player.getEntityWorld());
            if (playerHasClaimedChunk(player, adjacentChunk.getPos())) {
                InnerRealmUtils.destroyWall(player.getEntityWorld(), chunk, i);
            }
        }
    }

    @Override
    public boolean playerHasClaimedChunk(PlayerEntity player, ChunkPos chunk) {
        for (ArrayList<ChunkPos> claimedChunks : claimedChunksMap.values()) {
            if (claimedChunks.contains(chunk)) {
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
        if (claimedChunksMap.containsKey(playerID)) {
            claimedChunksMap.get(playerID).add(chunk);
        }
        else {
            ArrayList<ChunkPos> claimedChunks = new ArrayList<>();
            claimedChunks.add(chunk);
            this.claimedChunksMap.put(playerID, claimedChunks);
        }
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT playerChunkMap = new CompoundNBT();
        claimedChunksMap.forEach((key, value) -> {
            ListNBT claimedChunksList = new ListNBT();
            value.forEach(chunkPos -> claimedChunksList.add(NBTUtil.writeBlockPos(chunkPos.asBlockPos())));
            playerChunkMap.put(key.toString(), claimedChunksList);
        });
        // TODO Get rid of wrapper in 1.16
        CompoundNBT claimedChunks = new CompoundNBT();
        claimedChunks.put("claimedChunks", playerChunkMap);
        return claimedChunks;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        HashMap<UUID, ArrayList<ChunkPos>> claimedChunkMap = new HashMap<>();
        CompoundNBT claimedChunks = (CompoundNBT) nbt.get("claimedChunks");
        if (claimedChunks != null) {
            for (String id : claimedChunks.keySet()) {
                ArrayList<ChunkPos> chunkPosArrayList = new ArrayList<>();
                for (INBT locationTag : claimedChunks.getList(id, Constants.NBT.TAG_COMPOUND)) {
                    chunkPosArrayList.add(new ChunkPos(NBTUtil.readBlockPos((CompoundNBT) locationTag)));
                }
                claimedChunkMap.put(UUID.fromString(id), chunkPosArrayList);
            }
        }
        setClaimedChunkMap(claimedChunkMap);
    }
}
