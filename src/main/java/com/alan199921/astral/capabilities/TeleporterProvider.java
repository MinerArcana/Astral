package com.alan199921.astral.capabilities;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TeleporterProvider implements ICapabilitySerializable<INBT> {

    @CapabilityInject(IPocketDimTeleporter.class)
    public static final Capability<IPocketDimTeleporter> TELEPORTER_CAPABILITY = null;

    private IPocketDimTeleporter instance = TELEPORTER_CAPABILITY.getDefaultInstance();

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return null;
    }

    @Override
    public INBT serializeNBT() {
        return null;
    }

    @Override
    public void deserializeNBT(INBT nbt) {

    }
}
