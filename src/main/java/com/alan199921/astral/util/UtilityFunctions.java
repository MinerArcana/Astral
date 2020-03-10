package com.alan199921.astral.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class UtilityFunctions {
    public static ItemStack insertIntoAnySlot(IItemHandler itemHandler, ItemStack itemStack) {
        final int slots = itemHandler.getSlots();
        ItemStack leftovers = itemStack;
        for (int i = 0; i < slots; i++) {
            leftovers = itemHandler.insertItem(i, leftovers, false);
            if (leftovers.isEmpty()) {
                return ItemStack.EMPTY;
            }
        }
        return leftovers;
    }
}
