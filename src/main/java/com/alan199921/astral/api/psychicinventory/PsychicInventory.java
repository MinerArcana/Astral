package com.alan199921.astral.api.psychicinventory;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.items.ItemStackHandler;

import java.util.HashMap;
import java.util.UUID;
import java.util.stream.IntStream;

public class PsychicInventory implements IPsychicInventory {

    public static final String PSYCHIC_INVENTORIES = "psychicInventories";
    private HashMap<UUID, PsychicInventoryInstance> playerInventoriesMap = new HashMap<>();
    private InventoryType inventoryType = InventoryType.PHYSICAL;


    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT psychicInventoryNBT = new CompoundNBT();
        playerInventoriesMap.forEach((key, value) -> psychicInventoryNBT.put(key.toString(), value.serialize()));
        CompoundNBT psychicInventoryMap = new CompoundNBT();
        psychicInventoryMap.put(PSYCHIC_INVENTORIES, psychicInventoryNBT);
        psychicInventoryMap.putString("inventoryType", inventoryType.toString());
        return psychicInventoryMap;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        nbt.getCompound(PSYCHIC_INVENTORIES).keySet().forEach(s -> {
            final PsychicInventoryInstance psychicInventoryInstance = new PsychicInventoryInstance();
            psychicInventoryInstance.deserialize(nbt.getCompound(PSYCHIC_INVENTORIES).getCompound(s));
            playerInventoriesMap.put(UUID.fromString(s), psychicInventoryInstance);
        });
        inventoryType = InventoryType.valueOf(nbt.getString("inventoryType"));
    }

    @Override
    public PsychicInventoryInstance getInventoryOfPlayer(UUID player) {
        if (!playerInventoriesMap.containsKey(player)) {
            playerInventoriesMap.put(player, new PsychicInventoryInstance());
        }
        return playerInventoriesMap.get(player);
    }

    @Override
    public InventoryType getInventoryType() {
        return inventoryType;
    }

    public ItemStackHandler getInventory(InventoryType inventoryType, UUID playerId, InventorySection section) {
        PsychicInventoryInstance inventoryInstance = getInventoryOfPlayer(playerId);
        switch (inventoryType) {
            case ASTRAL:
                if (section == InventorySection.MAIN) {
                    return inventoryInstance.getAstralMainInventory();
                }
                if (section == InventorySection.ARMOR) {
                    return inventoryInstance.getAstralArmorInventory();
                }
                if (section == InventorySection.HANDS) {
                    return inventoryInstance.getAstralHandsInventory();
                }
                return new ItemStackHandler();
            case PHYSICAL:
                if (section == InventorySection.MAIN) {
                    return inventoryInstance.getPhysicalInventory();
                }
                if (section == InventorySection.ARMOR) {
                    return inventoryInstance.getPhysicalArmor();
                }
                if (section == InventorySection.HANDS) {
                    return inventoryInstance.getPhysicalHands();
                }
                return new ItemStackHandler();
            case INNER_REALM:
                if (section == InventorySection.MAIN) {
                    return inventoryInstance.getInnerRealmMain();
                }
                if (section == InventorySection.ARMOR) {
                    return inventoryInstance.getInnerRealmArmor();
                }
                if (section == InventorySection.HANDS) {
                    return inventoryInstance.getInnerRealmHands();
                }
                return new ItemStackHandler();
            default:
                return new ItemStackHandler();
        }
    }

    @Override
    public void setInventoryType(InventoryType inventoryType, PlayerInventory playerInventory) {
        //TODO Transfer items to capability before extracting
        this.inventoryType = inventoryType;
        final UUID uniqueID = playerInventory.player.getUniqueID();
        final ItemStackHandler mainInventory = getInventory(inventoryType, uniqueID, InventorySection.MAIN);
        IntStream.range(0, mainInventory.getSlots()).forEach(i -> {
            if (playerInventory.getStackInSlot(i) == ItemStack.EMPTY) {
                playerInventory.setInventorySlotContents(i, mainInventory.getStackInSlot(i));
            }
        });
        final ItemStackHandler armorInventory = getInventory(inventoryType, uniqueID, InventorySection.ARMOR);
        final ItemStackHandler handsInventory = getInventory(inventoryType, uniqueID, InventorySection.HANDS);
        for (EquipmentSlotType slot : EquipmentSlotType.values()) {
            EquipmentSlotType.Group group = slot.getSlotType();
            if (group == EquipmentSlotType.Group.HAND && !slot.equals(EquipmentSlotType.MAINHAND)) {
                playerInventory.player.setItemStackToSlot(slot, handsInventory.getStackInSlot(slot.getIndex()));
            }
            else if (group == EquipmentSlotType.Group.ARMOR) {
                playerInventory.player.setItemStackToSlot(slot, armorInventory.getStackInSlot(slot.getIndex()));
            }
        }
    }


}
