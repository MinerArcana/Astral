package com.minerarcana.astral.api.innerrealmcubegen;

import com.minerarcana.astral.Astral;
import com.minerarcana.astral.api.AstralCapabilities;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InnerRealmCubeGenProvider implements ICapabilitySerializable<CompoundTag> {
    public static final ResourceLocation KEY = new ResourceLocation(Astral.MOD_ID, "inner_realm_cube_generator");
    private final InnerRealmCubeGen capability;
    private final LazyOptional<IInnerRealmCubeGen> optional;

    public InnerRealmCubeGenProvider() {
        this(new InnerRealmCubeGen());
    }

    public InnerRealmCubeGenProvider(InnerRealmCubeGen innerRealmCubeGen) {
        this.capability = innerRealmCubeGen;
        optional = LazyOptional.of(() -> innerRealmCubeGen);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == AstralCapabilities.CUBE_GEN_CAPABILITY ? optional.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return capability.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        capability.deserializeNBT(nbt);
    }
}
