package com.alan19.astral.api.psychicinventory;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.UUID;

public interface IPsychicInventory extends INBTSerializable<CompoundTag> {
    PsychicInventoryInstance getInventoryOfPlayer(UUID player);

    InventoryType getInventoryType(UUID playerId);

}
