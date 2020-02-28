package com.alan199921.astral.api.psychicinventory;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.items.ItemStackHandler;

public class PsychicInventoryInstance {
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
        return compoundNBT;
    }

    public void deserialize(CompoundNBT nbt) {
        astralMainInventory.deserializeNBT(nbt.getCompound(PHYSICAL_MAIN_INVENTORY));
        astralHandsInventory.deserializeNBT(nbt.getCompound(PHYSICAL_HANDS_INVENTORY));
        astralArmorInventory.deserializeNBT(nbt.getCompound(PHYSICAL_ARMOR_INVENTORY));
        physicalInventory.deserializeNBT(nbt.getCompound(PHYSICAL_MAIN_INVENTORY));
        physicalHands.deserializeNBT(nbt.getCompound(PHYSICAL_HANDS_INVENTORY));
        physicalArmor.deserializeNBT(nbt.getCompound(PHYSICAL_ARMOR_INVENTORY));
        innerRealmMain.deserializeNBT(nbt.getCompound(INNER_REALM_MAIN_INVENTORY));
        innerRealmArmor.deserializeNBT(nbt.getCompound(INNER_REALM_ARMOR_INVENTORY));
        innerRealmHands.deserializeNBT(nbt.getCompound(INNER_REALM_HANDS_INVENTORY));
    }

    public ItemStack getStackFromPsychicSlot(EquipmentSlotType slotType) {
        switch (slotType.getSlotType()) {
            case HAND:
                return astralHandsInventory.getStackInSlot(slotType.getSlotIndex());
            case ARMOR:
                return astralArmorInventory.getStackInSlot(slotType.getIndex());
            default:
                return ItemStack.EMPTY;
        }
    }

    public ItemStack getStackFromPhysicalSlot(EquipmentSlotType slotType) {
        switch (slotType.getSlotType()) {
            case HAND:
                return physicalHands.getStackInSlot(slotType.getSlotIndex());
            case ARMOR:
                return physicalInventory.getStackInSlot(slotType.getIndex());
            default:
                return ItemStack.EMPTY;
        }
    }
}
