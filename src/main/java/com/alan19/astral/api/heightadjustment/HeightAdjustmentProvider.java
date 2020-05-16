package com.alan19.astral.api.heightadjustment;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class HeightAdjustmentProvider implements ICapabilitySerializable<CompoundNBT> {
    @CapabilityInject(IHeightAdjustmentCapability.class)
    public static final Capability<IHeightAdjustmentCapability> HEIGHT_ADJUSTMENT_CAPABILITY = null;

    private final IHeightAdjustmentCapability instance = HEIGHT_ADJUSTMENT_CAPABILITY.getDefaultInstance();

    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == HEIGHT_ADJUSTMENT_CAPABILITY) {
            return LazyOptional.of(() -> instance).cast();
        }
        else {
            return LazyOptional.empty();
        }
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
