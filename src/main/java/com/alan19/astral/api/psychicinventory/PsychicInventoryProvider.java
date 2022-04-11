package com.alan19.astral.api.psychicinventory;

import com.alan19.astral.api.AstralAPI;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PsychicInventoryProvider implements ICapabilitySerializable<CompoundTag> {
    private final PsychicInventory psychicInventory;
    private final LazyOptional<IPsychicInventory> optional;

    public PsychicInventoryProvider() {
        this(new PsychicInventory());
    }

    public PsychicInventoryProvider(PsychicInventory psychicInventory) {
        this.psychicInventory = psychicInventory;
        this.optional = LazyOptional.of(() -> psychicInventory);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == AstralAPI.PSYCHIC_INVENTORY_CAPABILITY ? optional.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return psychicInventory.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        psychicInventory.deserializeNBT(nbt);
    }
}
