package com.alan19.astral.api.constructtracker;

import com.alan19.astral.mentalconstructs.MentalConstruct;
import com.alan19.astral.mentalconstructs.MentalConstructType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ConstructTracker implements IConstructTracker {
    private final Map<UUID, PlayerMentalConstructTracker> playerConstructTracker = new HashMap<>();

    @Override
    public PlayerMentalConstructTracker getMentalConstructsForPlayer(Player player) {
        if (!playerConstructTracker.containsKey(player.getUUID())) {
            playerConstructTracker.put(player.getUUID(), new PlayerMentalConstructTracker());
        }
        return playerConstructTracker.get(player.getUUID());
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        playerConstructTracker.forEach((uuid, tracker) -> nbt.put(uuid.toString(), tracker.serializeNBT()));
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        for (String s : nbt.getAllKeys()) {
            PlayerMentalConstructTracker tracker = new PlayerMentalConstructTracker();
            tracker.deserializeNBT(nbt.getCompound(s));
            playerConstructTracker.put(UUID.fromString(s), tracker);
        }
    }

    @Override
    public void resetConstructEffect(MentalConstructType mentalConstruct, Level worldIn, BlockPos blockPos) {
        playerConstructTracker.values().stream().filter(tracker -> {
            if (tracker.getMentalConstructs().containsKey(mentalConstruct.getRegistryName())) {
                final MentalConstruct entry = tracker.getMentalConstructs().get(mentalConstruct.getRegistryName());
                return entry.getConstructPos().equals(blockPos) && worldIn.dimension() == entry.getDimensionKey();
            }
            return false;
        }).forEach(filteredTracker -> filteredTracker.removeMentalConstruct(mentalConstruct));
    }

    @Override
    public void updateAllPlayers(MentalConstructType mentalConstructType, ServerLevel world, BlockPos blockPos, int level) {
        playerConstructTracker.values().forEach(playerMentalConstructTracker -> playerMentalConstructTracker.modifyConstructInfo(blockPos, world, mentalConstructType, level));
    }
}
