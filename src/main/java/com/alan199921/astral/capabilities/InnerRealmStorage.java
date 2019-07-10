package com.alan199921.astral.capabilities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class InnerRealmStorage implements Capability.IStorage<IPocketDimTeleporter> {

    @Nullable
    @Override
    public INBT writeNBT(Capability<IPocketDimTeleporter> capability, IPocketDimTeleporter instance, Direction side) {
        return NBTUtil.writeBlockPos(instance.getSpawn());
    }

    @Override
    public void readNBT(Capability<IPocketDimTeleporter> capability, IPocketDimTeleporter instance, Direction side, INBT nbt) {
        instance.setSpawn(NBTUtil.readBlockPos((CompoundNBT) nbt));
    }
}
