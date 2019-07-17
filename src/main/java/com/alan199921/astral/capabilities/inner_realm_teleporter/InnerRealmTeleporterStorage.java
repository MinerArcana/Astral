package com.alan199921.astral.capabilities.inner_realm_teleporter;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.UUID;

public class InnerRealmTeleporterStorage implements Capability.IStorage<IInnerRealmTeleporterCapability> {

    @Nullable
    @Override
    public INBT writeNBT(Capability<IInnerRealmTeleporterCapability> capability, IInnerRealmTeleporterCapability instance, Direction side) {
        CompoundNBT spawnLocationTag = new CompoundNBT();
        for (UUID uuid: instance.getSpawnList().keySet()) {
            spawnLocationTag.put(uuid.toString(), NBTUtil.writeBlockPos(instance.getSpawnList().get(uuid)));
        }
        return new CompoundNBT().put("spawnLocations", spawnLocationTag);
    }

    @Override
    public void readNBT(Capability<IInnerRealmTeleporterCapability> capability, IInnerRealmTeleporterCapability instance, Direction side, INBT nbt) {
        HashMap<UUID, BlockPos> spawnList = new HashMap<>();
        CompoundNBT compoundNBT = (CompoundNBT) nbt;
        CompoundNBT spawnLocations = (CompoundNBT) compoundNBT.get("spawnLocations");
        assert spawnLocations != null;
        for (String id: spawnLocations.keySet()) {
            spawnList.put(UUID.fromString(id), NBTUtil.readBlockPos(spawnLocations.getCompound(id)));
        }
        instance.setSpawnList(spawnList);
    }
}
