package com.alan199921.astral.capabilities.inner_realm_teleporter;

import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class InnerRealmTeleporterProvider implements ICapabilityProvider {

    @CapabilityInject(IInnerRealmTeleporterCapability.class)
    public static final Capability<IInnerRealmTeleporterCapability> TELEPORTER_CAPABILITY = null;

    private IInnerRealmTeleporterCapability instance = TELEPORTER_CAPABILITY.getDefaultInstance();

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == TELEPORTER_CAPABILITY)
        {
            return LazyOptional.of(() -> instance).cast();
        }
        else
        {
            return LazyOptional.empty();
        }
    }
}
