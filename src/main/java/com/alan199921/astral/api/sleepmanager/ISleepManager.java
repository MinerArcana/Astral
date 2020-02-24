package com.alan199921.astral.api.sleepmanager;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface ISleepManager extends INBTSerializable<CompoundNBT> {
    int getSleep();

    void setSleep(int sleepCount);

    void addSleep();

    void resetSleep();

    boolean isEntityTraveling();
}