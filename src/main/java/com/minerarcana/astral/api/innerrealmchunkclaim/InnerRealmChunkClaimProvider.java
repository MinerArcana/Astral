package com.minerarcana.astral.api.innerrealmchunkclaim;

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

public class InnerRealmChunkClaimProvider implements ICapabilitySerializable<CompoundTag> {

    public static final ResourceLocation KEY = new ResourceLocation(Astral.MOD_ID, "inner_realm_chunk_claim");
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
        return cap == AstralCapabilities.CHUNK_CLAIM_CAPABILITY ? chunkClaimOptional.cast() : LazyOptional.empty();
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
