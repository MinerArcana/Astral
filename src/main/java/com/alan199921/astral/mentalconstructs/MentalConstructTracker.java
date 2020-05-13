package com.alan199921.astral.mentalconstructs;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MentalConstructTracker implements IMentalConstructTracker {
    private final HashMap<PlayerEntity, List<MentalConstruct>> mentalConstructTracker = new HashMap<>();

    @Override
    public List<MentalConstruct> getMentalConstructsForPlayer(PlayerEntity player) {
        return mentalConstructTracker.getOrDefault(player, new ArrayList<>());
    }

    @Override
    public CompoundNBT serializeNBT() {
        return null;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {

    }
}
