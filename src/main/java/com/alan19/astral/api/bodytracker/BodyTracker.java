package com.alan19.astral.api.bodytracker;

import com.alan19.astral.api.constructtracker.PlayerMentalConstructTracker;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.server.ServerWorld;

import java.util.Map;
import java.util.UUID;

public class BodyTracker implements IBodyTracker {
    private Map<UUID, Boolean> bodyTrackerMap;

    @Override
    public Map<UUID, Boolean> getBodyTrackerMap(ServerWorld world) {
        return bodyTrackerMap;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        bodyTrackerMap.forEach((uuid, active) -> nbt.putBoolean(uuid.toString(), active));
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        nbt.keySet().forEach(s -> bodyTrackerMap.put(UUID.fromString(s), nbt.getBoolean(s)));
    }
}
