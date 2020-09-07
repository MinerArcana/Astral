package com.alan19.astral.api.bodytracker;

import com.alan19.astral.api.AstralAPI;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BodyTrackerProvider implements ICapabilitySerializable<CompoundNBT> {
    private final BodyTracker bodyTrackerCapability;
    private final LazyOptional<IBodyTracker> bodyTrackerOptional;

    public BodyTrackerProvider() {
        this(new BodyTracker());
    }

    public BodyTrackerProvider(BodyTracker bodyTrackerCapability) {
        this.bodyTrackerCapability = bodyTrackerCapability;
        this.bodyTrackerOptional = LazyOptional.of(() -> bodyTrackerCapability);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == AstralAPI.bodyTrackerCapability ? bodyTrackerOptional.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        return bodyTrackerCapability.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        bodyTrackerCapability.deserializeNBT(nbt);
    }
}

