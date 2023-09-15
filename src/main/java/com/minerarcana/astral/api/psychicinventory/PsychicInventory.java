package com.minerarcana.astral.api.psychicinventory;

import net.minecraft.nbt.CompoundTag;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PsychicInventory implements IPsychicInventory  {
    public static final String PSYCHIC_INVENTORIES = "psychicInventories";
    private final Map<UUID, PsychicInventoryInstance> playerInventoriesMap = new HashMap<>();


    @Override
    public CompoundTag serializeNBT() {
        CompoundTag psychicInventoryTag = new CompoundTag();
        playerInventoriesMap.forEach((key, value) -> psychicInventoryTag.put(key.toString(), value.serialize()));
        CompoundTag psychicInventoryMap = new CompoundTag();
        psychicInventoryMap.put(PSYCHIC_INVENTORIES, psychicInventoryTag);
        return psychicInventoryMap;
    }

    @Override
    public void deserializeNBT(CompoundTag Tag) {
        for (String s : Tag.getCompound(PSYCHIC_INVENTORIES).getAllKeys()) {
            final PsychicInventoryInstance psychicInventoryInstance = new PsychicInventoryInstance();
            psychicInventoryInstance.deserialize(Tag.getCompound(PSYCHIC_INVENTORIES).getCompound(s));
            playerInventoriesMap.put(UUID.fromString(s), psychicInventoryInstance);
        }
    }

    @Override
    public PsychicInventoryInstance getInventoryOfPlayer(UUID player) {
        return playerInventoriesMap.computeIfAbsent(player, uuid -> new PsychicInventoryInstance());
    }

    @Override
    public InventoryType getInventoryType(UUID playerId) {
        return playerInventoriesMap.get(playerId).getInventoryType();
    }
}
