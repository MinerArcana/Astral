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
        return new LazyOptional<>(cap::getDefaultInstance);
    }

    @Override
    public INBT serializeNBT() {
        return TELEPORTER_CAPABILITY.getStorage().writeNBT(TELEPORTER_CAPABILITY, this.instance, null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        TELEPORTER_CAPABILITY.getStorage().readNBT(TELEPORTER_CAPABILITY, this.instance, null, nbt);
    }
}
