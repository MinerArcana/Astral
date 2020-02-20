package com.alan199921.astral.api.psychicinventory;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.items.ItemStackHandler;

public class PsychicInventoryInstance {
    public static final String MAIN_INVENTORY = "mainInventory";
    public static final String HANDS_INVENTORY = "handsInventory";
    public static final String ARMOR_INVENTORY = "armorInventory";
    public static final String PHYSICAL_INVENTORY = "physicalInventory";
    public static final String PHYSICAL_HANDS = "physicalHands";
    public static final String PHYSICAL_ARMOR = "physicalArmor";
    private ItemStackHandler mainInventory = new ItemStackHandler(36);
    private ItemStackHandler handsInventory = new ItemStackHandler(2);
    private ItemStackHandler armorInventory = new ItemStackHandler(4);
    private ItemStackHandler physicalInventory = new ItemStackHandler(36);
    private ItemStackHandler physicalHands = new ItemStackHandler(2);
    private ItemStackHandler physicalArmor = new ItemStackHandler(4);

    public ItemStackHandler getPhysicalHands() {
        return physicalHands;
    }

    public ItemStackHandler getPhysicalArmor() {
        return physicalArmor;
    }

    public ItemStackHandler getPhysicalInventory() {
        return physicalInventory;
    }

    public ItemStackHandler getMainInventory() {
        return mainInventory;
    }

    public ItemStackHandler getHandsInventory() {
        return handsInventory;
    }

    public ItemStackHandler getArmorInventory() {
        return armorInventory;
    }

    public CompoundNBT serialize() {
        CompoundNBT compoundNBT = new CompoundNBT();
        compoundNBT.put(MAIN_INVENTORY, mainInventory.serializeNBT());
        compoundNBT.put(HANDS_INVENTORY, handsInventory.serializeNBT());
        compoundNBT.put(ARMOR_INVENTORY, armorInventory.serializeNBT());
        compoundNBT.put(PHYSICAL_INVENTORY, physicalInventory.serializeNBT());
        compoundNBT.put(PHYSICAL_HANDS, physicalHands.serializeNBT());
        compoundNBT.put(PHYSICAL_ARMOR, physicalArmor.serializeNBT());
        return compoundNBT;
    }

    public void deserialize(CompoundNBT nbt) {
        mainInventory.deserializeNBT(nbt.getCompound(MAIN_INVENTORY));
        handsInventory.deserializeNBT(nbt.getCompound(HANDS_INVENTORY));
        armorInventory.deserializeNBT(nbt.getCompound(ARMOR_INVENTORY));
        physicalInventory.deserializeNBT(nbt.getCompound(PHYSICAL_INVENTORY));
        physicalHands.deserializeNBT(nbt.getCompound(PHYSICAL_HANDS));
        physicalArmor.deserializeNBT(nbt.getCompound(PHYSICAL_ARMOR));
    }
}
