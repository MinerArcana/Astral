package com.alan199921.astral.api.heightadjustment;

import net.minecraftforge.common.util.INBTSerializable;

public interface IHeightAdjustmentCapability extends INBTSerializable {
    int getHeightDifference();

    void setHeightDifference(int newHeight);

    void deactivate();

    void activate();

    boolean isActive();
}
