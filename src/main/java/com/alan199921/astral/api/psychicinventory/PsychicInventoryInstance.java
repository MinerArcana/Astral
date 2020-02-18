package com.alan199921.astral.api.psychicinventory;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.items.ItemStackHandler;

public class PsychicInventoryInstance {
    private final ItemStackHandler mainInventory = new ItemStackHandler(36);
    private final ItemStackHandler handsInventory = new ItemStackHandler(2);
    private final ItemStackHandler armorInventory = new ItemStackHandler(4);

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
        compoundNBT.put("mainInventory", mainInventory.serializeNBT());
        compoundNBT.put("handsInventory", handsInventory.serializeNBT());
        compoundNBT.put("armorInventory", armorInventory.serializeNBT());

        return compoundNBT;
    }

    public void deserialize(CompoundNBT nbt) {
        mainInventory.deserializeNBT(nbt.getCompound("mainInventory"));
        handsInventory.deserializeNBT(nbt.getCompound("handsInventory"));
        armorInventory.deserializeNBT(nbt.getCompound("armorInventory"));
    }
}
