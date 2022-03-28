package com.alan19.astral.api.heightadjustment;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface IHeightAdjustmentCapability extends INBTSerializable<CompoundTag> {
    int getHeightDifference();

    void setHeightDifference(int newHeight);

    void deactivate();

    void activate();

    boolean isActive();
}
