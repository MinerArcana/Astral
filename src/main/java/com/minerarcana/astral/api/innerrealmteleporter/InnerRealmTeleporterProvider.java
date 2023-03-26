package com.minerarcana.astral.api.innerrealmteleporter;

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

public class InnerRealmTeleporterProvider implements ICapabilitySerializable<CompoundTag> {
    public static final ResourceLocation KEY = new ResourceLocation(Astral.MOD_ID, "inner_realm_teleporter");
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
        return cap == AstralCapabilities.TELEPORTER_CAPABILITY ? teleporterOptional.cast() : LazyOptional.empty();
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
