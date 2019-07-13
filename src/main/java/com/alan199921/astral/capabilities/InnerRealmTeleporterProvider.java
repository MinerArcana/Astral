package com.alan199921.astral.capabilities;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class InnerRealmTeleporterProvider implements ICapabilitySerializable<INBT> {

    @CapabilityInject(IInnerRealmTeleporter.class)
    public static final Capability<IInnerRealmTeleporter> TELEPORTER_CAPABILITY = null;

    private IInnerRealmTeleporter instance = TELEPORTER_CAPABILITY.getDefaultInstance();

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return LazyOptional.of(new NonNullSupplier<T>() {
            @Nonnull
            @Override
            public T get() {
                return cap.getDefaultInstance();
            }
        });
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
