package com.alan199921.astral.entities;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

public class PhysicalBodyInventory implements IItemHandler {
    private final NonNullList<ItemStack> mainInventory = NonNullList.withSize(6 * 7, ItemStack.EMPTY);

    @Override
    public int getSlots() {
        return mainInventory.size();
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return mainInventory.get(slot);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return mainInventory.set(slot, stack);
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return mainInventory.remove(slot).split(amount);
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return true;
    }

    public NonNullList<ItemStack> getMainInventory() {
        return mainInventory;
    }
}
