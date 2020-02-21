package com.alan199921.astral.blocks.tileentities;

import com.alan199921.astral.api.AstralAPI;
import com.alan199921.astral.api.psychicinventory.IPsychicInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.UUID;

public class OfferingBrazierTile extends TileEntity implements ITickableTileEntity {
    private LazyOptional<IItemHandler> handler = LazyOptional.of(this::createHandler);
    private int burnTicks = 0;
    private int progress = 0;
    private UUID boundPlayer = UUID.randomUUID();
    private ItemStack lastStack = ItemStack.EMPTY;

    public OfferingBrazierTile() {
        super(AstralTiles.OFFERING_BRAZIER_TILE);
    }

    @Override
    public void tick() {
        handler.ifPresent(inventory -> {
            if (hasFuel() && inventory.getStackInSlot(1).getCount() > 0) {
                if (lastStack != inventory.getStackInSlot(1)) {
                    progress = 0;
                    lastStack = inventory.getStackInSlot(1);
                }
                else {
                    progress++;
                }
                if (progress >= 200 && boundPlayer != null) {
                    if (world instanceof ServerWorld) {
                        final IPsychicInventory overworldPsychicInventory = AstralAPI.getOverworldPsychicInventory((ServerWorld) world);
                        overworldPsychicInventory.getInventoryOfPlayer(boundPlayer).getMainInventory().insertItem(0, new ItemStack(lastStack.getItem()), false);
                        System.out.println("Transferred item to psychic inventory!");
                        lastStack.shrink(1);
                    }
                    progress = 0;
                }
            }
            else if (burnTicks <= 0 && AbstractFurnaceTileEntity.getBurnTimes().get(inventory.getStackInSlot(0).getItem()) > 0) {
                final ItemStack fuelInSlot = inventory.getStackInSlot(0);
                burnTicks += AbstractFurnaceTileEntity.getBurnTimes().get(inventory.getStackInSlot(0).getItem());
                fuelInSlot.shrink(1);
            }
        });


    }

    private boolean hasFuel() {
        return burnTicks > 0;
    }

    public void extractInsertItem(PlayerEntity playerEntity, Hand hand) {
        handler.ifPresent(inventory -> {
            ItemStack held = playerEntity.getHeldItem(hand);
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
        }
        else {
            ItemStack itemStack = inventory.extractItem(1, inventory.getStackInSlot(1).getCount(), false);
            playerEntity.addItemStackToInventory(itemStack);
        }
    }

    public void insertItem(IItemHandler brazierInventory, ItemStack heldItem) {
        if (AbstractFurnaceTileEntity.isFuel(heldItem)) {
            brazierInventory.insertItem(0, heldItem, false).getCount();
        }
        else {
            brazierInventory.insertItem(1, heldItem, false).getCount();
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

    public void setUUID(UUID uuid) {
        boundPlayer = uuid;
    }

    @Override
    public void read(CompoundNBT nbt) {
        super.read(nbt);
        burnTicks = nbt.getInt("burnTicks");
        progress = nbt.getInt("progress");
        boundPlayer = nbt.getUniqueId("boundPlayer");
        lastStack.deserializeNBT(nbt.getCompound("lastStack"));
    }

    @Override
    @Nonnull
    public CompoundNBT write(CompoundNBT nbt) {
        CompoundNBT write = super.write(nbt);
        write.putInt("burnTicks", burnTicks);
        write.putInt("progress", progress);
        write.putUniqueId("boundPlayer", boundPlayer);
        write.put("lastStack", lastStack.serializeNBT());
        return write;
    }
}
