package com.alan199921.astral.api.psychicinventory;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.UUID;

public interface IPsychicInventory extends INBTSerializable<CompoundNBT> {
    PsychicInventoryInstance getInventoryOfPlayer(UUID player);
}