package com.alan199921.astral.blocks.tileentities;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Hand;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class OfferingBrazierTile extends TileEntity implements ITickableTileEntity {
    private LazyOptional<IItemHandler> handler = LazyOptional.of(this::createHandler);

    public OfferingBrazierTile() {
        super(TileEntityType.FURNACE);
    }

    @Override
    public void tick() {

    }

    public void extractInsertItem(PlayerEntity playerEntity, Hand hand) {
        handler.ifPresent(inventory -> {
            ItemStack held = playerEntity.getHeldItem(hand);
            boolean used = false;
            if (!held.isEmpty()) {
                insertItem(inventory, held);
            }
            else {
                extractItem(playerEntity, inventory);
            }
        });
    }

    public void extractItem(PlayerEntity playerEntity, IItemHandler inventory) {
        boolean used = false;
        if (!inventory.getStackInSlot(1).isEmpty()) {
            ItemStack itemStack = inventory.extractItem(0, inventory.getStackInSlot(1).getCount(), false);
            playerEntity.addItemStackToInventory(itemStack);
            used = itemStack.getCount() == 0;
        }
        if (!used && !inventory.getStackInSlot(0).isEmpty()) {
            ItemStack itemStack = inventory.extractItem(1, inventory.getStackInSlot(1).getCount(), false);
            playerEntity.addItemStackToInventory(itemStack);
        }
    }

    public void insertItem(IItemHandler brazierInventory, ItemStack heldItem) {
        boolean used = false;
        if (AbstractFurnaceTileEntity.isFuel(heldItem)) {
            final int leftOver = brazierInventory.insertItem(0, heldItem, false).getCount();
            heldItem.setCount(leftOver);
            if (leftOver != 0) {
                used = true;
            }
        }
        if (!used) {
            final int leftOver = brazierInventory.insertItem(1, heldItem, false).getCount();
            heldItem.setCount(leftOver);
        }
    }

    private IItemHandler createHandler() {
        return new ItemStackHandler(2) {
            @Override
            protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
                return stack.getMaxStackSize();
            }

            @Override
            protected void onContentsChanged(int slot) {
                markDirty();
            }
        };
    }

}
