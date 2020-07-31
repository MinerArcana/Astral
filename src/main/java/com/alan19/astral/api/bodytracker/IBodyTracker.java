package com.alan19.astral.api.bodytracker;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Map;
import java.util.UUID;

/**
 * Capability to keep track of the NBT of the body if it has died (maybe it could only track if the body's alive)
 */
public interface IBodyTracker extends INBTSerializable<CompoundNBT> {
    Map<UUID, CompoundNBT> getBodyTrackerMap();
}
