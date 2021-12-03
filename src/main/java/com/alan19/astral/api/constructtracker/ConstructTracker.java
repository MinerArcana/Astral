package com.alan19.astral.api.constructtracker;

import com.alan19.astral.mentalconstructs.MentalConstruct;
import com.alan19.astral.mentalconstructs.MentalConstructType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ConstructTracker implements IConstructTracker {
    private final Map<UUID, PlayerMentalConstructTracker> playerConstructTracker = new HashMap<>();

    @Override
    public PlayerMentalConstructTracker getMentalConstructsForPlayer(PlayerEntity player) {
        if (!playerConstructTracker.containsKey(player.getUUID())) {
            playerConstructTracker.put(player.getUUID(), new PlayerMentalConstructTracker());
        }
        return playerConstructTracker.get(player.getUUID());
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        playerConstructTracker.forEach((uuid, tracker) -> nbt.put(uuid.toString(), tracker.serializeNBT()));
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        for (String s : nbt.getAllKeys()) {
            PlayerMentalConstructTracker tracker = new PlayerMentalConstructTracker();
            tracker.deserializeNBT(nbt.getCompound(s));
            playerConstructTracker.put(UUID.fromString(s), tracker);
        }
    }

    @Override
    public void resetConstructEffect(MentalConstructType mentalConstruct, World worldIn, BlockPos blockPos) {
        playerConstructTracker.values().stream().filter(tracker -> {
            if (tracker.getMentalConstructs().containsKey(mentalConstruct.getRegistryName())) {
                final MentalConstruct entry = tracker.getMentalConstructs().get(mentalConstruct.getRegistryName());
                return entry.getConstructPos().equals(blockPos) && worldIn.dimension() == entry.getDimensionKey();
            }
            return false;
        }).forEach(filteredTracker -> filteredTracker.removeMentalConstruct(mentalConstruct));
    }

    @Override
    public void updateAllPlayers(MentalConstructType mentalConstructType, ServerWorld world, BlockPos blockPos, int level) {
        playerConstructTracker.values().forEach(playerMentalConstructTracker -> playerMentalConstructTracker.modifyConstructInfo(blockPos, world, mentalConstructType, level));
    }
}
