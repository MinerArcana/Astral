package com.alan199921.astral.entities;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class PhysicalBodyInventory implements IItemHandler {
    private LazyOptional<ItemStackHandler> itemStackHandlerLazyOptional;

    public <U> PhysicalBodyInventory(LazyOptional<ItemStackHandler> map) {
        this.itemStackHandlerLazyOptional = map;
    }

    @Override
    public int getSlots() {
        return itemStackHandlerLazyOptional.map(ItemStackHandler::getSlots).orElse(0);
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return itemStackHandlerLazyOptional.map(itemStackHandler -> itemStackHandler.getStackInSlot(slot)).orElse(ItemStack.EMPTY);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return itemStackHandlerLazyOptional.map(itemStackHandler -> itemStackHandler.insertItem(slot, stack, simulate)).orElse(ItemStack.EMPTY);
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return itemStackHandlerLazyOptional.map(itemStackHandler -> itemStackHandler.extractItem(slot, amount, simulate)).orElse(ItemStack.EMPTY);
    }

    @Override
    public int getSlotLimit(int slot) {
        return itemStackHandlerLazyOptional.map(itemStackHandler -> itemStackHandler.getSlotLimit(slot)).orElse(0);
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return itemStackHandlerLazyOptional.map(itemStackHandler -> itemStackHandler.isItemValid(slot, stack)).orElse(false);
    }
}
