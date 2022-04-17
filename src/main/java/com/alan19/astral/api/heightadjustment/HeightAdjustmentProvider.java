package com.alan19.astral.api.heightadjustment;

import com.alan19.astral.api.AstralAPI;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class HeightAdjustmentProvider implements ICapabilitySerializable<CompoundTag> {

    private final HeightAdjustmentCapability heightAdjustmentCapability;
    private final LazyOptional<IHeightAdjustmentCapability> heightAdjustmentOptional;

    public HeightAdjustmentProvider() {
        this(new HeightAdjustmentCapability());
    }

    public HeightAdjustmentProvider(HeightAdjustmentCapability heightAdjustmentCapability) {
        this.heightAdjustmentCapability = heightAdjustmentCapability;
        this.heightAdjustmentOptional = LazyOptional.of(() -> heightAdjustmentCapability);
    }

    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == AstralAPI.HEIGHT_ADJUSTMENT_CAPABILITY ? heightAdjustmentOptional.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return heightAdjustmentCapability.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        heightAdjustmentCapability.deserializeNBT(nbt);
    }
}
