package com.alan199921.astral.api.constructtracker;

import com.alan199921.astral.api.AstralAPI;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ConstructTrackerProvider implements ICapabilitySerializable<CompoundNBT> {
    private final IConstructTracker constructTracker;
    private final LazyOptional<IConstructTracker> constructOptional;

    public ConstructTrackerProvider() {
        this(new ConstructTracker());
    }

    public ConstructTrackerProvider(ConstructTracker tracker) {
        this.constructTracker = tracker;
        this.constructOptional = LazyOptional.of(() -> constructTracker);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == AstralAPI.constructTrackerCapability ? constructOptional.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        return constructTracker.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        constructTracker.deserializeNBT(nbt);
    }

}
