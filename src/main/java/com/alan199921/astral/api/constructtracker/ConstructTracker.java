package com.alan199921.astral.api.constructtracker;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

import java.util.HashMap;
import java.util.UUID;

public class ConstructTracker implements IConstructTracker {
    private final HashMap<UUID, PlayerMentalConstructTracker> playerConstructTracker = new HashMap<>();

    @Override
    public PlayerMentalConstructTracker getMentalConstructsForPlayer(PlayerEntity player) {
        if (!playerConstructTracker.containsKey(player.getUniqueID())) {
            playerConstructTracker.put(player.getUniqueID(), new PlayerMentalConstructTracker());
        }
        return playerConstructTracker.getOrDefault(player.getUniqueID(), new PlayerMentalConstructTracker());
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        playerConstructTracker.forEach((uuid, tracker) -> nbt.put(uuid.toString(), tracker.serializeNBT()));
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        for (String s : nbt.keySet()) {
            PlayerMentalConstructTracker tracker = new PlayerMentalConstructTracker();
            tracker.deserializeNBT((CompoundNBT) nbt.get(s));
            playerConstructTracker.put(UUID.fromString(s), tracker);
        }
    }
}
