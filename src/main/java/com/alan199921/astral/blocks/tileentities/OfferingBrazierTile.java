package com.alan199921.astral.blocks.tileentities;

import com.alan199921.astral.api.AstralAPI;
import com.alan199921.astral.util.UtilityFunctions;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class OfferingBrazierTile extends TileEntity implements ITickableTileEntity {
    private LazyOptional<IItemHandler> handler = LazyOptional.of(this::createHandler);
    private int burnTicks = 0;
    private int progress = 0;
    private Optional<UUID> boundPlayer = Optional.empty();
    private ItemStack lastStack = ItemStack.EMPTY;

    public OfferingBrazierTile() {
        super(AstralTiles.OFFERING_BRAZIER_TILE);
    }

    public Optional<UUID> getBoundPlayer() {
        return boundPlayer;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void tick() {
        handler.ifPresent(inventory -> boundPlayer.ifPresent(uuid -> {
            if (hasFuel()) {
                burnTicks--;
            }
            if (hasFuel() && inventory.getStackInSlot(1).getCount() > 0) {
                if (lastStack != inventory.getStackInSlot(1)) {
                    progress = 0;
                    lastStack = inventory.getStackInSlot(1);
                }
                else {
                    progress++;
                }
                if (progress >= 200 && boundPlayer.isPresent()) {
                    if (world instanceof ServerWorld) {
                        AstralAPI.getOverworldPsychicInventory((ServerWorld) world).ifPresent(overworldPsychicInventory -> {
                            final ItemStackHandler innerRealmMain = overworldPsychicInventory.getInventoryOfPlayer(uuid).getInnerRealmMain();
                            UtilityFunctions.insertIntoAnySlot(innerRealmMain, new ItemStack(lastStack.getItem()));
                            lastStack.shrink(1);
                        });
                    }
                    progress = 0;
                }
            }
            else if (burnTicks <= 0 && AbstractFurnaceTileEntity.isFuel(inventory.getStackInSlot(0)) && !inventory.getStackInSlot(1).isEmpty()) {
                this.getWorld().setBlockState(pos, this.getWorld().getBlockState(pos).with(AbstractFurnaceBlock.LIT, true));
                final ItemStack fuelInSlot = inventory.getStackInSlot(0);
                burnTicks += AbstractFurnaceTileEntity.getBurnTimes().get(fuelInSlot.getItem());
                fuelInSlot.shrink(1);
            }
            if (burnTicks <= 0) {
                this.getWorld().setBlockState(pos, this.getBlockState().with(AbstractFurnaceBlock.LIT, false), 3);
            }
            else {
                this.getWorld().setBlockState(pos, this.getBlockState().with(AbstractFurnaceBlock.LIT, true), 3);
            }
        }));
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
        if (!inventory.getStackInSlot(1).isEmpty()) {
            ItemStack itemStack = inventory.extractItem(1, inventory.getStackInSlot(1).getCount(), false);
            playerEntity.addItemStackToInventory(itemStack);
        }
        else if (!inventory.getStackInSlot(0).isEmpty()) {
            ItemStack itemStack = inventory.extractItem(0, inventory.getStackInSlot(0).getCount(), false);
            playerEntity.addItemStackToInventory(itemStack);
        }
        markDirty();
    }

    public void insertItem(IItemHandler brazierInventory, ItemStack heldItem) {
        if (brazierInventory.isItemValid(0, heldItem) && !brazierInventory.insertItem(0, heldItem, true).isItemEqual(heldItem)) {
            final int leftover = brazierInventory.insertItem(0, heldItem.copy(), false).getCount();
            heldItem.setCount(leftover);
        }
        else if (brazierInventory.isItemValid(1, heldItem) && !brazierInventory.insertItem(1, heldItem, true).isItemEqual(heldItem)) {
            final int leftover = brazierInventory.insertItem(1, heldItem.copy(), false).getCount();
            heldItem.setCount(leftover);
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

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                if (slot == 0) {
                    return AbstractFurnaceTileEntity.isFuel(stack) && super.isItemValid(slot, stack);
                }
                else {
                    return super.isItemValid(slot, stack);
                }
            }
        };
    }

    public void setUUID(UUID uuid) {
        boundPlayer = Optional.of(uuid);
    }

    @Override
    public void read(CompoundNBT nbt) {
        super.read(nbt);
        burnTicks = nbt.getInt("burnTicks");
        progress = nbt.getInt("progress");
        boolean boundPlayerExists = nbt.getBoolean("boundPlayerExists");
        boundPlayer = boundPlayerExists ? Optional.of(nbt.getUniqueId("boundPlayer")) : Optional.empty();
        lastStack.deserializeNBT(nbt.getCompound("lastStack"));
        handler.ifPresent(iItemHandler -> {
            if (iItemHandler instanceof ItemStackHandler) {
                ((ItemStackHandler) iItemHandler).deserializeNBT(nbt.getCompound("brazierInventory"));
            }
        });
    }

    @Override
    @Nonnull
    public CompoundNBT write(CompoundNBT nbt) {
        super.write(nbt);
        nbt.putInt("burnTicks", burnTicks);
        nbt.putInt("progress", progress);
        nbt.putBoolean("boundPlayerExists", boundPlayer.isPresent());
        boundPlayer.ifPresent(uuid -> nbt.putUniqueId("boundPlayer", uuid));
        nbt.put("lastStack", lastStack.serializeNBT());
        handler.ifPresent(iItemHandler -> {
            if (iItemHandler instanceof ItemStackHandler) {
                nbt.put("brazierInventory", ((ItemStackHandler) iItemHandler).serializeNBT());
            }
        });
        return nbt;
    }
}
