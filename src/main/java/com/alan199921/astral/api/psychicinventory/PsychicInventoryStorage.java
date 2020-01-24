package com.alan199921.astral.api.psychicinventory;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class PsychicInventoryStorage implements Capability.IStorage<IPsychicInventory> {
    @Nullable
    @Override
    public INBT writeNBT(Capability<IPsychicInventory> capability, IPsychicInventory instance, Direction side) {
        return instance.serializeNBT();
    }

    @Override
    public void readNBT(Capability<IPsychicInventory> capability, IPsychicInventory instance, Direction side, INBT nbt) {
        if (nbt instanceof CompoundNBT) {
            instance.deserializeNBT((CompoundNBT) nbt);
        }
    }
}
