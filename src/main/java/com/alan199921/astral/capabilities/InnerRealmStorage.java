package com.alan199921.astral.capabilities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.UUID;

public class InnerRealmStorage implements Capability.IStorage<IPocketDimTeleporter> {

    @Nullable
    @Override
    public INBT writeNBT(Capability<IPocketDimTeleporter> capability, IPocketDimTeleporter instance, Direction side) {
        CompoundNBT spawnLocationTag = new CompoundNBT();
        for (UUID uuid: instance.getSpawnList().keySet()) {
            spawnLocationTag.put(uuid.toString(), NBTUtil.writeBlockPos(instance.getSpawnList().get(uuid)));
        }
        return new CompoundNBT().put("spawnLocations", spawnLocationTag);
    }

    @Override
    public void readNBT(Capability<IPocketDimTeleporter> capability, IPocketDimTeleporter instance, Direction side, INBT nbt) {
        NBTUtil.readBlockPos();
    }
}
