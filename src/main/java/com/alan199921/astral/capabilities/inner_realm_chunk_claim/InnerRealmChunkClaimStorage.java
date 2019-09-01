package com.alan199921.astral.capabilities.inner_realm_chunk_claim;

import com.google.gson.Gson;
import net.minecraft.nbt.*;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.IChunk;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class InnerRealmChunkClaimStorage implements Capability.IStorage<IInnerRealmChunkClaimCapability> {
    @Nullable
    @Override
    public INBT writeNBT(Capability<IInnerRealmChunkClaimCapability> capability, IInnerRealmChunkClaimCapability instance, Direction side) {
        CompoundNBT playerChunkMap = new CompoundNBT();
        for (UUID uuid: instance.getClaimedChunkMap().keySet()) {
            ListNBT claimedChunks = new ListNBT();
            instance.getClaimedChunkMap().get(uuid)
                    .forEach(chunkPos -> claimedChunks.add(NBTUtil.writeBlockPos(chunkPos.asBlockPos())));
            playerChunkMap.put(uuid.toString(), claimedChunks);
        }
        return new CompoundNBT().put("claimedChunks", playerChunkMap);
    }

    @Override
    public void readNBT(Capability<IInnerRealmChunkClaimCapability> capability, IInnerRealmChunkClaimCapability instance, Direction side, INBT nbt) {
        HashMap<UUID, ArrayList<ChunkPos>> claimedChunkMap = new HashMap<>();
        CompoundNBT compoundNBT = (CompoundNBT) nbt;
        CompoundNBT claimedChunks = (CompoundNBT) compoundNBT.get("claimedChunks");
        for (String id: claimedChunks.keySet()) {
            ArrayList<ChunkPos> chunkPosArrayList = new ArrayList<>();
            claimedChunks.getList(id, 0);
            claimedChunkMap.put(UUID.fromString(id), chunkPosArrayList);
        }
        instance.setClaimedChunkMap(claimedChunkMap);
    }
}
