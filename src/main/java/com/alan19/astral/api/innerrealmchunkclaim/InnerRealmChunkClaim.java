package com.alan19.astral.api.innerrealmchunkclaim;

import com.alan19.astral.dimensions.innerrealm.InnerRealmUtils;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.Constants;

import java.util.*;
import java.util.stream.Collectors;

public class InnerRealmChunkClaim implements IInnerRealmChunkClaim {
    private Map<UUID, List<ChunkPos>> claimedChunksMap = new HashMap<>();

    @Override
    public boolean isChunkClaimedByPlayer(ServerPlayerEntity player, ChunkPos chunk) {
        return claimedChunksMap.getOrDefault(player.getUniqueID(), Collections.emptyList()).contains(chunk);
    }

    /**
     * Attempt to add a chunk to the player's claimed chunk map.
     * If the chunk has been claimed by any players (including the player claiming it), return false.
     * Otherwise, add the chunk to the player's claimed chunk map.
     * Then generate a room for the claimed chunk.
     * If the chunks in the newly generated chunk have been claimed, break down the walls that lead to claimed chunks.
     *
     * @param player The player that is claiming a chunk
     * @param chunk  The position of the chunk that is being claimed
     * @return true if the chunk is successfully claimed, false if the chunk has already been claimed.
     */
    @Override
    public boolean claimChunk(ServerPlayerEntity player, ChunkPos chunk) {
        UUID playerID = player.getUniqueID();
        // Player fails to claim chunk if the chunk has been claimed
        if (claimedChunksMap.entrySet().stream().flatMap(uuidListEntry -> uuidListEntry.getValue().stream()).collect(Collectors.toList()).contains(chunk)) {
            return false;
        }
        claimedChunksMap.compute(playerID, (uuid, chunkPos) -> {
            List<ChunkPos> chunks = chunkPos != null ? chunkPos : new ArrayList<>();
            chunks.add(chunk);
            return chunks;
        });
        InnerRealmUtils.generateInnerRealmChunk(player.getServerWorld(), chunk);
        for (int i = 0; i < 4; i++) {
            Chunk adjacentChunk = InnerRealmUtils.getAdjacentChunk(chunk.asBlockPos(), i, player.getEntityWorld());
            if (isChunkClaimedByPlayer(player, adjacentChunk.getPos())) {
                InnerRealmUtils.destroyWall(player.getServerWorld(), chunk, i);
                InnerRealmUtils.destroyWall(player.getServerWorld(), adjacentChunk.getPos(), (i + 2) % 4);
            }
        }

        return true;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT playerChunkMap = new CompoundNBT();
        for (Map.Entry<UUID, List<ChunkPos>> entry : claimedChunksMap.entrySet()) {
            UUID key = entry.getKey();
            List<ChunkPos> chunkPosList = entry.getValue();
            final ListNBT chunkListNBT = new ListNBT();
            // Convert each of the ChunkPos stored in the entry to a BlockPos CompoundNBT and then store them in a list
            chunkListNBT.addAll(chunkPosList.stream().map(chunkPos -> NBTUtil.writeBlockPos(chunkPos.asBlockPos())).collect(Collectors.toList()));
            playerChunkMap.put(key.toString(), chunkListNBT);
        }
        return playerChunkMap;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        Map<UUID, List<ChunkPos>> claimedChunkMap = new HashMap<>();
        // Iterate over each key (player UUID) add the UUID and the ListNBT converted to a list of ChunkPos as entries to the HashMap
        for (String s : nbt.keySet()) {
            List<ChunkPos> list = new ArrayList<>();
            for (INBT inbt : nbt.getList(s, Constants.NBT.TAG_COMPOUND)) {
                ChunkPos pos = new ChunkPos(NBTUtil.readBlockPos((CompoundNBT) inbt));
                list.add(pos);
            }
            claimedChunkMap.put(UUID.fromString(s), list);
        }
        this.claimedChunksMap = claimedChunkMap;
    }
}
