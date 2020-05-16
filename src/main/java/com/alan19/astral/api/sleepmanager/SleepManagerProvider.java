package com.alan19.astral.api.sleepmanager;

import com.alan19.astral.api.AstralAPI;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SleepManagerProvider implements ICapabilitySerializable<CompoundNBT> {
    private final ISleepManager sleepManager;
    private final LazyOptional<ISleepManager> sleepManagerOptional;

    public SleepManagerProvider() {
        this(new SleepManager());
    }

    public SleepManagerProvider(SleepManager sleepManager) {
        this.sleepManager = sleepManager;
        this.sleepManagerOptional = LazyOptional.of(() -> sleepManager);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == AstralAPI.sleepManagerCapability ? sleepManagerOptional.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        return sleepManager.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        sleepManager.deserializeNBT(nbt);
    }
}
