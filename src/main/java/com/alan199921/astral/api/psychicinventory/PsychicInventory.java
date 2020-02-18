package com.alan199921.astral.api.psychicinventory;

import net.minecraft.nbt.CompoundNBT;

import java.util.HashMap;
import java.util.UUID;

public class PsychicInventory implements IPsychicInventory {

    public static final String PSYCHIC_INVENTORIES = "psychicInventories";
    private int sleepGauge = 0;
    private boolean activeGhost = false;

    private HashMap<UUID, PsychicInventoryInstance> playerInventoriesMap = new HashMap<>();


    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT psychicInventoryNBT = new CompoundNBT();
        playerInventoriesMap.forEach((key, value) -> psychicInventoryNBT.put(key.toString(), value.serialize()));
        psychicInventoryNBT.putInt("sleepGauge", sleepGauge);
        psychicInventoryNBT.putBoolean("activeGhost", activeGhost);
        CompoundNBT psychicInventoryMap = new CompoundNBT();
        psychicInventoryMap.put(PSYCHIC_INVENTORIES, psychicInventoryNBT);
        return psychicInventoryMap;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        nbt.getCompound(PSYCHIC_INVENTORIES).keySet().forEach(s -> {
            final PsychicInventoryInstance psychicInventoryInstance = new PsychicInventoryInstance();
            psychicInventoryInstance.deserialize(nbt.getCompound(PSYCHIC_INVENTORIES).getCompound(s));
            playerInventoriesMap.put(UUID.fromString(s), psychicInventoryInstance);
        });
        sleepGauge = nbt.getInt("sleepGauge");
        activeGhost = nbt.getBoolean("activeGhost");
    }

    @Override
    public PsychicInventoryInstance getInventoryOfPlayer(UUID player) {
        return playerInventoriesMap.get(player);
    }
}
