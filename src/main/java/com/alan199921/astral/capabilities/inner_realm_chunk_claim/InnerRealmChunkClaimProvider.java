package com.alan199921.astral.capabilities.inner_realm_chunk_claim;

import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class InnerRealmChunkClaimProvider implements ICapabilityProvider {
    @CapabilityInject(IInnerRealmChunkClaimCapability.class)
    public static final Capability<IInnerRealmChunkClaimCapability> CHUNK_CLAIM_CAPABILITY = null;

    private IInnerRealmChunkClaimCapability instance = CHUNK_CLAIM_CAPABILITY.getDefaultInstance();


    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CHUNK_CLAIM_CAPABILITY)
        {
            return LazyOptional.of(() -> instance).cast();
        }
        else
        {
            return LazyOptional.empty();
        }
    }
}
