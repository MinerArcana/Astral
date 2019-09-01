package com.alan199921.astral.capabilities.inner_realm_chunk_claim;

import com.google.gson.Gson;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.Direction;
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
        CompoundNBT spawnLocationTag = new CompoundNBT();
        for (UUID uuid: instance.getClaimedChunkMap().keySet()) {
            spawnLocationTag.put(uuid.toString(), new StringNBT(new Gson().toJson(instance.getClaimedChunkMap().get(uuid))));
        }
        return new CompoundNBT().put("claimedChunks", spawnLocationTag);
    }

    @Override
    public void readNBT(Capability<IInnerRealmChunkClaimCapability> capability, IInnerRealmChunkClaimCapability instance, Direction side, INBT nbt) {
        HashMap<UUID, ArrayList<IChunk>> claimedChunkMap = new HashMap<>();
        CompoundNBT compoundNBT = (CompoundNBT) nbt;
        CompoundNBT claimedChunks = (CompoundNBT) compoundNBT.get("claimedChunks");
        assert claimedChunks != null;
        for (String id: claimedChunks.keySet()) {
            claimedChunkMap.put(UUID.fromString(id), new Gson().fromJson(claimedChunks.getCompound(id).getString(), ArrayList.class));
        }
        instance.setClaimedChunkMap(claimedChunkMap);
    }
}
