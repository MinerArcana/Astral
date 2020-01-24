package com.alan199921.astral.api.heightadjustment;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;

public class HeightAdjustmentCapability implements IHeightAdjustmentCapability {
    private int heightDifference = 0;
    private boolean active = false;

    @Override
    public int getHeightDifference() {
        return heightDifference;
    }

    @Override
    public void setHeightDifference(int newHeight) {
        heightDifference = newHeight;
    }

    @Override
    public void deactivate() {
        active = false;
    }

    @Override
    public void activate() {
        active = true;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public INBT serializeNBT() {
        CompoundNBT compoundNBT = new CompoundNBT();
        compoundNBT.putInt("heightDifference", heightDifference);
        compoundNBT.putBoolean("differenceActive", active);
        return compoundNBT;
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        CompoundNBT compoundNBT = (CompoundNBT) nbt;
        heightDifference = compoundNBT.getInt("heightDifference");
        active = compoundNBT.getBoolean("differenceActive");
    }
}
