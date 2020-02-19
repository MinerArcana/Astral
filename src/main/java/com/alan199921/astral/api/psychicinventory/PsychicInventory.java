package com.alan199921.astral.api.psychicinventory;

import net.minecraft.nbt.CompoundNBT;

import java.util.HashMap;
import java.util.UUID;

public class PsychicInventory implements IPsychicInventory {

    public static final String PSYCHIC_INVENTORIES = "psychicInventories";
    private HashMap<UUID, PsychicInventoryInstance> playerInventoriesMap = new HashMap<>();


    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT psychicInventoryNBT = new CompoundNBT();
        playerInventoriesMap.forEach((key, value) -> psychicInventoryNBT.put(key.toString(), value.serialize()));
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
    }

    @Override
    public PsychicInventoryInstance getInventoryOfPlayer(UUID player) {
        if (!playerInventoriesMap.containsKey(player)) {
            playerInventoriesMap.put(player, new PsychicInventoryInstance());
        }
        return playerInventoriesMap.get(player);
    }
}
