package com.alan199921.astral.capabilities.bodylink;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class BodyLinkStorage implements Capability.IStorage<IBodyLinkCapability> {
    @Nullable
    @Override
    public INBT writeNBT(Capability<IBodyLinkCapability> capability, IBodyLinkCapability instance, Direction side) {
        return instance.serializeNBT();
    }

    @Override
    public void readNBT(Capability<IBodyLinkCapability> capability, IBodyLinkCapability instance, Direction side, INBT nbt) {
        instance.deserializeNBT(nbt);
    }
}
