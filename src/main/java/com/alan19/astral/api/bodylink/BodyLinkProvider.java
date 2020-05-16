package com.alan19.astral.api.bodylink;

import com.alan19.astral.api.AstralAPI;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BodyLinkProvider implements ICapabilitySerializable<CompoundNBT> {
    private final BodyLinkCapability bodyLinkCapability;
    private final LazyOptional<IBodyLinkCapability> bodyLinkOptional;

    public BodyLinkProvider() {
        this(new BodyLinkCapability());
    }

    public BodyLinkProvider(BodyLinkCapability bodyLinkCapability) {
        this.bodyLinkCapability = bodyLinkCapability;
        this.bodyLinkOptional = LazyOptional.of(() -> bodyLinkCapability);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == AstralAPI.bodyLinkCapability ? bodyLinkOptional.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        return bodyLinkCapability.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        bodyLinkCapability.deserializeNBT(nbt);
    }
}
