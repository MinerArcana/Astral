package com.minerarcana.astral.api.bodytracker;

import com.minerarcana.astral.Astral;
import com.minerarcana.astral.api.AstralCapabilities;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BodyTrackerProvider implements ICapabilitySerializable<CompoundTag> {
    public static final ResourceLocation KEY = new ResourceLocation(Astral.MOD_ID, "body_tracker");

    private final BodyTracker bodyTrackerCapability;
    private final LazyOptional<IBodyTracker> optional;

    public BodyTrackerProvider() {
        this(new BodyTracker());
    }

    public BodyTrackerProvider(BodyTracker bodyTrackerCapability) {
        this.bodyTrackerCapability = bodyTrackerCapability;
        this.optional = LazyOptional.of(() -> bodyTrackerCapability);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == AstralCapabilities.BODY_TRACKER_CAPABILITY ? optional.cast() : LazyOptional.empty();
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

