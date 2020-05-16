package com.alan19.astral.api.innerrealmchunkclaim;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.Direction;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class InnerRealmChunkClaimStorage implements Capability.IStorage<IInnerRealmChunkClaimCapability> {
    //TODO Move this over to NBTCapStorage
    @Nullable
    @Override
    public INBT writeNBT(Capability<IInnerRealmChunkClaimCapability> capability, IInnerRealmChunkClaimCapability instance, Direction side) {
        CompoundNBT playerChunkMap = new CompoundNBT();
        for (UUID uuid : instance.getClaimedChunkMap().keySet()) {
            ListNBT claimedChunks = new ListNBT();
            instance.getClaimedChunkMap().get(uuid)
                    .forEach(chunkPos -> claimedChunks.add(NBTUtil.writeBlockPos(chunkPos.asBlockPos())));
            playerChunkMap.put(uuid.toString(), claimedChunks);
        }
        CompoundNBT claimedChunks = new CompoundNBT();
        claimedChunks.put("claimedChunks", playerChunkMap);
        return claimedChunks;
    }

    @Override
    public void readNBT(Capability<IInnerRealmChunkClaimCapability> capability, IInnerRealmChunkClaimCapability instance, Direction side, INBT nbt) {
        HashMap<UUID, ArrayList<ChunkPos>> claimedChunkMap = new HashMap<>();
        CompoundNBT compoundNBT = (CompoundNBT) nbt;
        CompoundNBT claimedChunks = (CompoundNBT) compoundNBT.get("claimedChunks");
        for (String id : claimedChunks.keySet()) {
            ArrayList<ChunkPos> chunkPosArrayList = new ArrayList<>();
            for (INBT locationTag : claimedChunks.getList(id, Constants.NBT.TAG_COMPOUND)) {
                chunkPosArrayList.add(new ChunkPos(NBTUtil.readBlockPos((CompoundNBT) locationTag)));
            }
            claimedChunkMap.put(UUID.fromString(id), chunkPosArrayList);
        }
        instance.setClaimedChunkMap(claimedChunkMap);
    }
}
