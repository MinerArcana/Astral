package com.alan19.astral.api.innerrealmchunkclaim;

import com.alan19.astral.api.AstralAPI;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class InnerRealmChunkClaimProvider implements ICapabilitySerializable<CompoundTag> {
    private final InnerRealmChunkClaim chunkClaimCapability;
    private final LazyOptional<IInnerRealmChunkClaim> chunkClaimOptional;

    public InnerRealmChunkClaimProvider() {
        this(new InnerRealmChunkClaim());
    }

    public InnerRealmChunkClaimProvider(InnerRealmChunkClaim chunkClaimCapability) {
        this.chunkClaimCapability = chunkClaimCapability;
        this.chunkClaimOptional = LazyOptional.of(() -> chunkClaimCapability);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == AstralAPI.INNER_REALM_CHUNK_CLAIM_CAPABILITY ? chunkClaimOptional.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return chunkClaimCapability.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        chunkClaimCapability.deserializeNBT(nbt);
    }
}
