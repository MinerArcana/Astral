package com.alan199921.astral.api.psychicinventory;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface IPsychicInventory extends INBTSerializable<CompoundNBT> {
    boolean addSleep();

    void clearSleep();

    int getSleep();

    boolean isEntityTraveling(LivingEntity entity);
}
