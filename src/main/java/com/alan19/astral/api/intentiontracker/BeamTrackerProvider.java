package com.alan19.astral.api.intentiontracker;

import com.alan19.astral.api.AstralAPI;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BeamTrackerProvider implements ICapabilitySerializable<CompoundNBT> {
    private final IBeamTracker beamTracker;
    private final LazyOptional<IBeamTracker> beamTrackerLazyOptional;

    public BeamTrackerProvider(){
        this(new BeamTracker());
    }

    public BeamTrackerProvider(BeamTracker beamTracker) {
        this.beamTracker = beamTracker;
        this.beamTrackerLazyOptional = LazyOptional.of(() -> beamTracker);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == AstralAPI.beamTrackerCapability ? beamTrackerLazyOptional.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        return beamTracker.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        beamTracker.deserializeNBT(nbt);
    }
}
