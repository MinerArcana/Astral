package com.alan199921.astral.api.psychicinventory;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PsychicInventoryProvider implements ICapabilitySerializable<CompoundNBT> {
    @CapabilityInject(IPsychicInventory.class)
    public static final Capability<IPsychicInventory> PSYCHIC_INVENTORY_CAPABILITY = null;

    private IPsychicInventory instance = PSYCHIC_INVENTORY_CAPABILITY.getDefaultInstance();

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == PSYCHIC_INVENTORY_CAPABILITY ? LazyOptional.of(() -> instance).cast() : LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        return instance.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        instance.deserializeNBT(nbt);
    }
}
