package com.alan19.astral.blocks.tileentities;

import com.alan19.astral.api.AstralAPI;
import com.alan19.astral.network.AstralNetwork;
import com.alan19.astral.recipe.AbstractBrazierRecipe;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
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
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class OfferingBrazierTileEntity extends TileEntity implements ITickableTileEntity {
    private final LazyOptional<IItemHandler> handler = LazyOptional.of(this::createHandler);
    private int burnTicks = 0;
    private int progress = 0;
    private Optional<UUID> boundPlayer = Optional.empty();
    private ItemStack lastStack = ItemStack.EMPTY;

    public OfferingBrazierTileEntity() {
        super(AstralTiles.OFFERING_BRAZIER.get());
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
        if (level instanceof ServerWorld) {
            if (level.getGameTime() % 10 == 0) {
                updateOfferingBrazierInventory();
            }
            handler.ifPresent(inventory -> boundPlayer.ifPresent(uuid -> {
                if (hasFuel()) {
                    burnTicks--;
                }
                if (hasFuel() && inventory.getStackInSlot(1).getCount() > 0) {
                    burnItems(inventory, uuid);
                }
                else if (burnTicks <= 0 && AbstractFurnaceTileEntity.isFuel(inventory.getStackInSlot(0)) && !inventory.getStackInSlot(1).isEmpty()) {
                    final ItemStack fuelInSlot = inventory.getStackInSlot(0);
                    burnTicks += AbstractFurnaceTileEntity.getFuel().get(fuelInSlot.getItem());
                    fuelInSlot.shrink(1);
                    updateOfferingBrazierInventory();
                }
                this.level.setBlockAndUpdate(worldPosition, this.getBlockState().setValue(AbstractFurnaceBlock.LIT, burnTicks > 0));
            }));
        }
    }

    private AbstractBrazierRecipe matchRecipe(ItemStack stackInSlot) {
        if (level != null) {
            return level.getRecipeManager().getRecipes().stream().filter(recipe -> recipe instanceof AbstractBrazierRecipe).map(recipe -> (AbstractBrazierRecipe) recipe).filter(recipe -> recipe.matches(stackInSlot)).findFirst().orElse(null);
        }
        return null;
    }

    public void burnItems(IItemHandler inventory, UUID uuid) {
        if (lastStack != inventory.getStackInSlot(1)) {
            progress = 0;
            lastStack = inventory.getStackInSlot(1);
        }
        else {
            progress++;
        }
        if (progress >= 200 && boundPlayer.isPresent() && level != null) {
            AbstractBrazierRecipe recipe = matchRecipe(inventory.getStackInSlot(1));
            ItemStack output = recipe != null ? recipe.getResultItem() : new ItemStack(lastStack.getItem());
            this.level.setBlockAndUpdate(worldPosition, getBlockState().setValue(AbstractFurnaceBlock.LIT, true));

            AstralAPI.getOverworldPsychicInventory((ServerWorld) level).ifPresent(overworldPsychicInventory -> {
                final ItemStackHandler innerRealmMain = overworldPsychicInventory.getInventoryOfPlayer(uuid).getInnerRealmMain();
                ItemHandlerHelper.insertItemStacked(innerRealmMain, output, false);
                lastStack.shrink(1);
            });
            AstralNetwork.sendOfferingBrazierFinishParticles(worldPosition, level.getChunkAt(worldPosition));
            updateOfferingBrazierInventory();
            progress = 0;
        }
    }

    private boolean hasFuel() {
        return burnTicks > 0;
    }

    public void extractInsertItem(PlayerEntity playerEntity, Hand hand) {
        handler.ifPresent(inventory -> {
            ItemStack held = playerEntity.getItemInHand(hand);
            if (!held.isEmpty()) {
                insertItem(inventory, held);
            }
            else {
                extractItem(playerEntity, inventory);
            }
        });
        updateOfferingBrazierInventory();
    }

    public void extractItem(PlayerEntity playerEntity, IItemHandler inventory) {
        if (!inventory.getStackInSlot(1).isEmpty()) {
            ItemStack itemStack = inventory.extractItem(1, inventory.getStackInSlot(1).getCount(), false);
            playerEntity.addItem(itemStack);
        }
        else if (!inventory.getStackInSlot(0).isEmpty()) {
            ItemStack itemStack = inventory.extractItem(0, inventory.getStackInSlot(0).getCount(), false);
            playerEntity.addItem(itemStack);
        }
        setChanged();
    }

    public void insertItem(IItemHandler brazierInventory, ItemStack heldItem) {
        if (brazierInventory.isItemValid(0, heldItem) && !brazierInventory.insertItem(0, heldItem, true).sameItem(heldItem)) {
            final int leftover = brazierInventory.insertItem(0, heldItem.copy(), false).getCount();
            heldItem.setCount(leftover);
        }
        else if (brazierInventory.isItemValid(1, heldItem) && !brazierInventory.insertItem(1, heldItem, true).sameItem(heldItem)) {
            final int leftover = brazierInventory.insertItem(1, heldItem.copy(), false).getCount();
            heldItem.setCount(leftover);
        }
    }

    /**
     * Offering Braziers can accept fuel items in slot 0, and non-fuel and tile entities in slot 1
     *
     * @return The IItemHandler for the Offering Braizier
     */
    @Nonnull
    private IItemHandler createHandler() {
        return new ItemStackHandler(2) {
            @Override
            protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
                return stack.getMaxStackSize();
            }

            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                if (slot == 0) {
                    return AbstractFurnaceTileEntity.isFuel(stack) && super.isItemValid(slot, stack);
                }
                else {
                    final Block blockFromItem = Block.byItem(stack.getItem());
                    return !AbstractFurnaceTileEntity.isFuel(stack) && !blockFromItem.hasTileEntity(blockFromItem.defaultBlockState()) && super.isItemValid(slot, stack);
                }
            }
        };
    }

    public void setUUID(UUID uuid) {
        boundPlayer = Optional.of(uuid);
    }

    public void updateOfferingBrazierInventory() {
        requestModelDataUpdate();
        this.setChanged();
        if (this.getLevel() != null) {
            this.getLevel().sendBlockUpdated(worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.getBlockPos(), -1, this.getUpdateTag());
    }

    @Override
    @Nonnull
    public CompoundNBT getUpdateTag() {
        CompoundNBT updateTag = new CompoundNBT();
        final IItemHandler itemHandler = getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElseGet(this::createHandler);
        CompoundNBT fuelSlot = new CompoundNBT();
        CompoundNBT itemSlot = new CompoundNBT();
        itemHandler.getStackInSlot(0).save(fuelSlot);
        itemHandler.getStackInSlot(1).save(itemSlot);
        updateTag.put("fuel", fuelSlot);
        updateTag.put("item", itemSlot);
        return updateTag;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        final IItemHandler itemHandler = getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElseGet(this::createHandler);
        ((ItemStackHandler) itemHandler).setStackInSlot(0, ItemStack.of(tag.getCompound("fuel")));
        ((ItemStackHandler) itemHandler).setStackInSlot(1, ItemStack.of(tag.getCompound("item")));
    }

    @Override
    public void load(@Nonnull BlockState state, @Nonnull CompoundNBT nbt) {
        super.load(state, nbt);
        burnTicks = nbt.getInt("burnTicks");
        progress = nbt.getInt("progress");
        boolean boundPlayerExists = nbt.getBoolean("boundPlayerExists");
        boundPlayer = boundPlayerExists ? Optional.of(nbt.getUUID("boundPlayer")) : Optional.empty();
        lastStack.deserializeNBT(nbt.getCompound("lastStack"));
        handler.ifPresent(iItemHandler -> {
            if (iItemHandler instanceof ItemStackHandler) {
                ((ItemStackHandler) iItemHandler).deserializeNBT(nbt.getCompound("brazierInventory"));
            }
        });
    }

    @Override
    @Nonnull
    public CompoundNBT save(@Nonnull CompoundNBT nbt) {
        super.save(nbt);
        nbt.putInt("burnTicks", burnTicks);
        nbt.putInt("progress", progress);
        nbt.putBoolean("boundPlayerExists", boundPlayer.isPresent());
        boundPlayer.ifPresent(uuid -> nbt.putUUID("boundPlayer", uuid));
        nbt.put("lastStack", lastStack.serializeNBT());
        handler.ifPresent(iItemHandler -> {
            if (iItemHandler instanceof ItemStackHandler) {
                nbt.put("brazierInventory", ((ItemStackHandler) iItemHandler).serializeNBT());
            }
        });
        return nbt;
    }
}
