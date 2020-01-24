package com.alan199921.astral.api.heightadjustment;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class HeightAdjustmentStorage implements Capability.IStorage<IHeightAdjustmentCapability> {
    @Nullable
    @Override
    public INBT writeNBT(Capability<IHeightAdjustmentCapability> capability, IHeightAdjustmentCapability instance, Direction side) {
        return instance.serializeNBT();
    }

    @Override
    public void readNBT(Capability<IHeightAdjustmentCapability> capability, IHeightAdjustmentCapability instance, Direction side, INBT nbt) {
        instance.deserializeNBT(nbt);
    }
}
