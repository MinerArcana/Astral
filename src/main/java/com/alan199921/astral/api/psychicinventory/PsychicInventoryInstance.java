package com.alan199921.astral.api.psychicinventory;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

import java.util.stream.IntStream;

public class PsychicInventoryInstance {
    private InventoryType inventoryType = InventoryType.PHYSICAL;


    private static final String ASTRAL = "astral";
    private static final String INNER_REALM = "innerRealm";
    private static final String PHYSICAL = "physical";
    private static final String MAIN_INVENTORY = "MainInventory";
    private static final String ARMOR_INVENTORY = "ArmorInventory";
    private static final String HANDS_INVENTORY = "HandsInventory";
    private static final String ASTRAL_MAIN_INVENTORY = ASTRAL + MAIN_INVENTORY;
    private static final String ASTRAL_ARMOR_INVENTORY = ASTRAL + ARMOR_INVENTORY;
    private static final String ASTRAL_HANDS_INVENTORY = ASTRAL + HANDS_INVENTORY;
    private static final String INNER_REALM_MAIN_INVENTORY = INNER_REALM + MAIN_INVENTORY;
    private static final String INNER_REALM_ARMOR_INVENTORY = INNER_REALM + ARMOR_INVENTORY;
    private static final String INNER_REALM_HANDS_INVENTORY = INNER_REALM + HANDS_INVENTORY;
    private static final String PHYSICAL_MAIN_INVENTORY = PHYSICAL + MAIN_INVENTORY;
    private static final String PHYSICAL_ARMOR_INVENTORY = PHYSICAL + ARMOR_INVENTORY;
    private static final String PHYSICAL_HANDS_INVENTORY = PHYSICAL + HANDS_INVENTORY;

    private ItemStackHandler astralMainInventory = new ItemStackHandler(36);
    private ItemStackHandler astralHandsInventory = new ItemStackHandler(2);
    private ItemStackHandler astralArmorInventory = new ItemStackHandler(4);
    private ItemStackHandler physicalInventory = new ItemStackHandler(36);
    private ItemStackHandler physicalHands = new ItemStackHandler(2);
    private ItemStackHandler physicalArmor = new ItemStackHandler(4);
    private ItemStackHandler innerRealmMain = new ItemStackHandler(36);
    private ItemStackHandler innerRealmHands = new ItemStackHandler(2);
    private ItemStackHandler innerRealmArmor = new ItemStackHandler(4);

    public ItemStackHandler getInnerRealmMain() {
        return innerRealmMain;
    }

    public ItemStackHandler getInnerRealmHands() {
        return innerRealmHands;
    }

    public ItemStackHandler getInnerRealmArmor() {
        return innerRealmArmor;
    }

    public ItemStackHandler getPhysicalHands() {
        return physicalHands;
    }

    public ItemStackHandler getPhysicalArmor() {
        return physicalArmor;
    }

    public ItemStackHandler getPhysicalInventory() {
        return physicalInventory;
    }

    public ItemStackHandler getAstralMainInventory() {
        return astralMainInventory;
    }

    public ItemStackHandler getAstralHandsInventory() {
        return astralHandsInventory;
    }

    public ItemStackHandler getAstralArmorInventory() {
        return astralArmorInventory;
    }

    public CompoundNBT serialize() {
        CompoundNBT compoundNBT = new CompoundNBT();
        compoundNBT.put(ASTRAL_MAIN_INVENTORY, astralMainInventory.serializeNBT());
        compoundNBT.put(ASTRAL_HANDS_INVENTORY, astralHandsInventory.serializeNBT());
        compoundNBT.put(ASTRAL_ARMOR_INVENTORY, astralArmorInventory.serializeNBT());
        compoundNBT.put(PHYSICAL_MAIN_INVENTORY, physicalInventory.serializeNBT());
        compoundNBT.put(PHYSICAL_HANDS_INVENTORY, physicalHands.serializeNBT());
        compoundNBT.put(PHYSICAL_ARMOR_INVENTORY, physicalArmor.serializeNBT());
        compoundNBT.put(INNER_REALM_MAIN_INVENTORY, innerRealmMain.serializeNBT());
        compoundNBT.put(INNER_REALM_ARMOR_INVENTORY, innerRealmArmor.serializeNBT());
        compoundNBT.put(INNER_REALM_HANDS_INVENTORY, innerRealmHands.serializeNBT());
        compoundNBT.putString("inventoryType", inventoryType.toString());

        return compoundNBT;
    }

    public void deserialize(CompoundNBT nbt) {
        astralMainInventory.deserializeNBT(nbt.getCompound(ASTRAL_MAIN_INVENTORY));
        astralHandsInventory.deserializeNBT(nbt.getCompound(ASTRAL_HANDS_INVENTORY));
        astralArmorInventory.deserializeNBT(nbt.getCompound(ASTRAL_ARMOR_INVENTORY));
        physicalInventory.deserializeNBT(nbt.getCompound(PHYSICAL_MAIN_INVENTORY));
        physicalHands.deserializeNBT(nbt.getCompound(PHYSICAL_HANDS_INVENTORY));
        physicalArmor.deserializeNBT(nbt.getCompound(PHYSICAL_ARMOR_INVENTORY));
        innerRealmMain.deserializeNBT(nbt.getCompound(INNER_REALM_MAIN_INVENTORY));
        innerRealmArmor.deserializeNBT(nbt.getCompound(INNER_REALM_ARMOR_INVENTORY));
        innerRealmHands.deserializeNBT(nbt.getCompound(INNER_REALM_HANDS_INVENTORY));
        inventoryType = InventoryType.valueOf(nbt.getString("inventoryType"));

    }

    /**
     * Sets an itemstack to a slot in a psychic inventory category
     *
     * @param slotIn        The slot to set an item into
     * @param stack         The itemstack to be inserted
     * @param inventoryType The inventory type to be set
     */
    public void setItemStackToSlot(EquipmentSlotType slotIn, ItemStack stack, InventoryType inventoryType) {
        final ItemStackHandler[] inventories = getInventory(inventoryType);
        if (slotIn == EquipmentSlotType.OFFHAND) {
            inventories[2].setStackInSlot(0, stack);
        }
        else if (slotIn.getSlotType() == EquipmentSlotType.Group.ARMOR) {
            inventories[1].setStackInSlot(slotIn.getIndex(), stack);
        }
    }

    /**
     * Get the stack in an equipment slot
     *
     * @param slotType      The slot type
     * @param inventoryType The inventory category to check
     * @return The stack in the specified equipment slot of the inventory category
     */
    public ItemStack getStackFromSlot(EquipmentSlotType slotType, InventoryType inventoryType) {
        final ItemStackHandler[] inventory = getInventory(inventoryType);
        if (slotType == EquipmentSlotType.OFFHAND) {
            return inventory[2].getStackInSlot(0);
        }
        return slotType.getSlotType() == EquipmentSlotType.Group.ARMOR ? inventory[1].getStackInSlot(slotType.getIndex()) : ItemStack.EMPTY;
    }

    /**
     * Gets the inventory of an inventory type as an array
     *
     * @param inventoryType The inventory type to switch to
     * @return An array of ItemStackHandlers of size 3, index 0 is the main inventory, 1 is the armor, 2 is the hands
     */
    private ItemStackHandler[] getInventory(InventoryType inventoryType) {
        switch (inventoryType) {
            case ASTRAL:
                return new ItemStackHandler[]{astralMainInventory, astralArmorInventory, astralHandsInventory};
            case PHYSICAL:
                return new ItemStackHandler[]{physicalInventory, physicalArmor, physicalHands};
            case INNER_REALM:
                return new ItemStackHandler[]{innerRealmMain, innerRealmArmor, innerRealmHands};
            default:
                return new ItemStackHandler[]{new ItemStackHandler(), new ItemStackHandler(), new ItemStackHandler()};
        }
    }

    /**
     * Sets the inventory the player has access to and switches the items accordingly
     *
     * @param inventoryType   The inventory type to switch to
     * @param playerInventory The player inventory to be modified
     */
    public void setInventoryType(InventoryType inventoryType, PlayerInventory playerInventory) {
        transferInventoryToCapability(playerInventory);

        this.inventoryType = inventoryType;
        if (playerInventory.player.isAlive()) {
            final ItemStackHandler[] inventories = getInventory(inventoryType);
            final ItemStackHandler mainInventory = inventories[0];
            IntStream.range(0, mainInventory.getSlots()).forEach(i -> {
                if (playerInventory.getStackInSlot(i) == ItemStack.EMPTY) {
                    playerInventory.setInventorySlotContents(i, mainInventory.getStackInSlot(i));
                }
            });
            final ItemStackHandler armorInventory = inventories[1];
            final ItemStackHandler handsInventory = inventories[2];
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

    /**
     * Transfer your current inventory into the capability
     *
     * @param playerInventory The player inventory to be emptied
     */
    private void transferInventoryToCapability(PlayerInventory playerInventory) {
        //Transfer items to capability before extracting
        final ItemStackHandler[] originalInventories = getInventory(this.inventoryType);
        ItemStackHandler mainInventory = originalInventories[0];

        final NonNullList<ItemStack> playerMainInventory = playerInventory.mainInventory;
        for (int i = 0; i < playerMainInventory.size(); i++) {
            ItemStack itemStack = playerMainInventory.get(i);
            mainInventory.setStackInSlot(i, itemStack);
            playerMainInventory.set(i, ItemStack.EMPTY);
        }
        //Insert armor and offhand to capability
        for (EquipmentSlotType slotType : EquipmentSlotType.values()) {
            setItemStackToSlot(slotType, playerInventory.player.getItemStackFromSlot(slotType), inventoryType);
        }
    }

    public InventoryType getInventoryType() {
        return inventoryType;
    }

}
