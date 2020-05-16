package com.alan19.astral.api.heightadjustment;

import net.minecraft.nbt.CompoundNBT;

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
    public CompoundNBT serializeNBT() {
        CompoundNBT compoundNBT = new CompoundNBT();
        compoundNBT.putInt("heightDifference", heightDifference);
        compoundNBT.putBoolean("differenceActive", active);
        return compoundNBT;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        heightDifference = nbt.getInt("heightDifference");
        active = nbt.getBoolean("differenceActive");
    }
}
