package com.alan199921.astral.api.psychicinventory;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.ItemStackHandler;

public interface IPsychicInventory extends INBTSerializable<CompoundNBT> {
    ItemStackHandler getPhysicalMainInventory();

    ItemStackHandler getPhysicalArmor();

    ItemStackHandler getPhysicalHands();

    ItemStackHandler getPsychicMainInventory();

    ItemStackHandler getPsychicArmor();

    ItemStackHandler getPsychicHands();

    boolean addSleep();

    void clearSleep();

    int getSleep();

    boolean isEntityTraveling(LivingEntity entity);
}
