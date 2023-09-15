package com.minerarcana.astral.api.psychicinventory;

import com.minerarcana.astral.Astral;
import com.minerarcana.astral.api.AstralCapabilities;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PsychicInventoryProvider implements ICapabilitySerializable<CompoundTag> {
    public static final ResourceLocation KEY = new ResourceLocation(Astral.MOD_ID, "psychic_inventory");
    private final PsychicInventory psychicInventory;
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
        return cap == AstralCapabilities.PSYCHIC_INVENTORY_CAPABILITY ? psychicInventoryOptional.cast() : LazyOptional.empty();
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
