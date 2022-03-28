package com.alan19.astral.api.sleepmanager;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class SleepManagerStorage implements Capability.IStorage<ISleepManager> {
    @Nullable
    @Override
    public Tag writeNBT(Capability<ISleepManager> capability, ISleepManager instance, Direction side) {
        return instance.serializeNBT();
    }

    @Override
    public void readNBT(Capability<ISleepManager> capability, ISleepManager instance, Direction side, Tag nbt) {
        instance.deserializeNBT((CompoundTag) nbt);
    }
}
