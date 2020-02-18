package com.alan199921.astral.api.sleepmanager;

import net.minecraft.nbt.CompoundNBT;

public class SleepManager implements ISleepManager {
    public static final String SLEEP_COUNT = "sleepCount";
    private int sleepCount = 0;

    @Override
    public int getSleep() {
        return 0;
    }

    @Override
    public void setSleep(int newSleepCount) {
        sleepCount = newSleepCount;
    }

    @Override
    public void addSleep() {
        sleepCount++;
    }

    @Override
    public void resetSleep() {
        sleepCount = 0;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt(SLEEP_COUNT, sleepCount);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        sleepCount = nbt.getInt(SLEEP_COUNT);
    }
}
