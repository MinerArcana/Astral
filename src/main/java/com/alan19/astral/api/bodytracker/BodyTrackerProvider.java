package com.alan19.astral.api.bodytracker;

import com.alan19.astral.api.AstralAPI;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BodyTrackerProvider implements ICapabilitySerializable<CompoundTag> {
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
        return cap == AstralAPI.BODY_TRACKER_CAPABILITY ? bodyTrackerOptional.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return bodyTrackerCapability.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        bodyTrackerCapability.deserializeNBT(nbt);
    }
}

