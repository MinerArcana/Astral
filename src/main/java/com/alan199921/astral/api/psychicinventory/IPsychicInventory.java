package com.alan199921.astral.api.psychicinventory;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface IPsychicInventory extends INBTSerializable<CompoundNBT> {
    void addSleep();

    void addSleep(int ticks);

    boolean isPlayerSleeping();
}
