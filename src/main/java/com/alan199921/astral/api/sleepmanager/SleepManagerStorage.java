package com.alan199921.astral.api.sleepmanager;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class SleepManagerStorage implements Capability.IStorage<ISleepManager> {
    @Nullable
    @Override
    public INBT writeNBT(Capability<ISleepManager> capability, ISleepManager instance, Direction side) {
        return instance.serializeNBT();
    }

    @Override
    public void readNBT(Capability<ISleepManager> capability, ISleepManager instance, Direction side, INBT nbt) {
        instance.deserializeNBT((CompoundNBT) nbt);
    }
}
