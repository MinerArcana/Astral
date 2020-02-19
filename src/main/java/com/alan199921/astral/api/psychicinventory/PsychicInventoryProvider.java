package com.alan199921.astral.api.psychicinventory;

import com.alan199921.astral.api.AstralAPI;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PsychicInventoryProvider implements ICapabilitySerializable<CompoundNBT> {
    private final IPsychicInventory psychicInventory;
    private final LazyOptional<IPsychicInventory> psychicInventoryOptional;

    public PsychicInventoryProvider() {
        this(new PsychicInventory());
    }

    public PsychicInventoryProvider(PsychicInventory psychicInventory) {
        this.psychicInventory = psychicInventory;
        this.psychicInventoryOptional = LazyOptional.of(() -> psychicInventory);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == AstralAPI.psychicInventoryCapability ? psychicInventoryOptional.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        return psychicInventory.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        psychicInventory.deserializeNBT(nbt);
    }
}
