package com.alan199921.astral.api.sleepmanager;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SleepManagerStorage implements ICapabilitySerializable<CompoundNBT>, Capability.IStorage<ISleepManager> {
    @CapabilityInject(ISleepManager.class)
    public static final Capability<ISleepManager> SLEEP_MANAGER_CAPABILITY = null;
    private ISleepManager instance = SLEEP_MANAGER_CAPABILITY.getDefaultInstance();

    @Nullable
    @Override
    public INBT writeNBT(Capability<ISleepManager> capability, ISleepManager instance, Direction side) {
        return instance.serializeNBT();
    }

    @Override
    public void readNBT(Capability<ISleepManager> capability, ISleepManager instance, Direction side, INBT nbt) {
        instance.deserializeNBT((CompoundNBT) nbt);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == SLEEP_MANAGER_CAPABILITY ? LazyOptional.of(() -> instance).cast() : LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        return instance.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        instance.deserializeNBT(nbt);
    }
}
