package com.alan19.astral.api.bodytracker;

import net.minecraft.nbt.CompoundNBT;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BodyTracker implements IBodyTracker {
    private final Map<UUID, CompoundNBT> bodyTrackerMap = new HashMap<>();

    @Override
    public Map<UUID, CompoundNBT> getBodyTrackerMap() {
        return bodyTrackerMap;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        bodyTrackerMap.forEach((uuid, mobNBT) -> nbt.put(uuid.toString(), mobNBT));
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        nbt.keySet().forEach(s -> bodyTrackerMap.put(UUID.fromString(s), nbt.getCompound(s)));
    }
}
