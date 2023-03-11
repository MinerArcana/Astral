package com.minerarcana.astral.api.innerrealmcubegen;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.*;

public class InnerRealmCubeGen implements IInnerRealmCubeGen {
    private Map<UUID, List<ChunkPos>> claimedChunksMap = new HashMap<>();

    @Override
    public boolean isChunkClaimedByPlayer(Player player, ChunkPos chunk) {
        return claimedChunksMap.getOrDefault(player.getUUID(), Collections.emptyList()).contains(chunk);
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
    public boolean claimChunk(Player player, ChunkPos chunk) {
        UUID playerID = player.getUUID();
        // Player fails to claim chunk if the chunk has been claimed
        if (claimedChunksMap.entrySet().stream().flatMap(uuidListEntry -> uuidListEntry.getValue().stream()).toList().contains(chunk)) {
            return false;
        }
        claimedChunksMap.compute(playerID, (uuid, chunkPos) -> {
            List<ChunkPos> chunks = chunkPos != null ? chunkPos : new ArrayList<>();
            chunks.add(chunk);
            return chunks;
        });
        InnerRealmUtils.generateInnerRealmChunk(player.getLevel(), chunk);
        for (int i = 0; i < 4; i++) {
            LevelChunk adjacentChunk = InnerRealmUtils.getAdjacentChunk(chunk.getWorldPosition(), i, player.getLevel());
            if (isChunkClaimedByPlayer(player, adjacentChunk.getPos())) {
                InnerRealmUtils.destroyWall(player.getLevel(), chunk, i);
                InnerRealmUtils.destroyWall(player.getLevel(), adjacentChunk.getPos(), (i + 2) % 4);
            }
        }

        return true;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag playerChunkMap = new CompoundTag();
        for (Map.Entry<UUID, List<ChunkPos>> entry : claimedChunksMap.entrySet()) {
            UUID key = entry.getKey();
            List<ChunkPos> chunkPosList = entry.getValue();
            final ListTag chunkListNBT = new ListTag();
            // Convert each of the ChunkPos stored in the entry to a BlockPos CompoundNBT and then store them in a list
            chunkListNBT.addAll(chunkPosList.stream().map(chunkPos -> NbtUtils.writeBlockPos(chunkPos.getWorldPosition())).toList());
            playerChunkMap.put(key.toString(), chunkListNBT);
        }
        return playerChunkMap;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        Map<UUID, List<ChunkPos>> claimedChunkMap = new HashMap<>();
        // Iterate over each key (player UUID) add the UUID and the ListNBT converted to a list of ChunkPos as entries to the HashMap
        for (String s : nbt.getAllKeys()) {
            List<ChunkPos> list = new ArrayList<>();
            for (Tag inbt : nbt.getList(s, Tag.TAG_COMPOUND)) {
                ChunkPos pos = new ChunkPos(NbtUtils.readBlockPos((CompoundTag) inbt));
                list.add(pos);
            }
            claimedChunkMap.put(UUID.fromString(s), list);
        }
        this.claimedChunksMap = claimedChunkMap;
    }
}
