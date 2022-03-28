package com.alan19.astral.api.sleepmanager;

import com.alan19.astral.configs.AstralConfig;
import net.minecraft.nbt.CompoundTag;

public class SleepManager implements ISleepManager {
    public static final String SLEEP_COUNT = "sleepCount";
    public static final String GOING_TO_INNER_REALM = "goingToInnerRealm";
    private int sleepCount = 0;
    private boolean goingToInnerRealm = false;

    @Override
    public boolean isGoingToInnerRealm() {
        return goingToInnerRealm;
    }

    @Override
    public void setGoingToInnerRealm(boolean goingToInnerRealm) {
        this.goingToInnerRealm = goingToInnerRealm;
    }

    @Override
    public int getSleep() {
        return sleepCount;
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
    public boolean isEntityTraveling() {
        return sleepCount >= AstralConfig.getTravelingSettings().startupTime.get();
    }


    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt(SLEEP_COUNT, sleepCount);
        nbt.putBoolean(GOING_TO_INNER_REALM, goingToInnerRealm);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        sleepCount = nbt.getInt(SLEEP_COUNT);
        goingToInnerRealm = nbt.getBoolean(GOING_TO_INNER_REALM);
    }
}
