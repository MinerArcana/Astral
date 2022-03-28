package com.alan19.astral.api.innerrealmteleporter;

import com.alan19.astral.api.AstralAPI;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class InnerRealmTeleporterProvider implements ICapabilitySerializable<CompoundTag> {
    private final InnerRealmTeleporter teleporterCapability;
    private final LazyOptional<IInnerRealmTeleporter> teleporterOptional;

    public InnerRealmTeleporterProvider() {
        this(new InnerRealmTeleporter());
    }

    public InnerRealmTeleporterProvider(InnerRealmTeleporter teleporterCapability) {
        this.teleporterCapability = teleporterCapability;
        this.teleporterOptional = LazyOptional.of(() -> teleporterCapability);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == AstralAPI.teleporterCapability ? teleporterOptional.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return teleporterCapability.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        teleporterCapability.deserializeNBT(nbt);
    }
}
