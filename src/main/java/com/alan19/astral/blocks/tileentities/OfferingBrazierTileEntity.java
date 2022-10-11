package com.alan19.astral.blocks.tileentities;

import com.alan19.astral.api.AstralAPI;
import com.alan19.astral.network.AstralNetwork;
import com.alan19.astral.recipe.AbstractBrazierRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class OfferingBrazierTileEntity extends BlockEntity {
    private final LazyOptional<IItemHandler> handler = LazyOptional.of(this::createHandler);
    private int burnTicks = 0;
    private int progress = 0;
    private Optional<UUID> boundPlayer = Optional.empty();
    private ItemStack lastStack = ItemStack.EMPTY;

    public OfferingBrazierTileEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(AstralTiles.OFFERING_BRAZIER.get(), pWorldPosition, pBlockState);
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

    public void tick() {
        if (level instanceof ServerLevel) {
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
                else if (burnTicks <= 0 && AbstractFurnaceBlockEntity.isFuel(inventory.getStackInSlot(0)) && !inventory.getStackInSlot(1).isEmpty()) {
                    final ItemStack fuelInSlot = inventory.getStackInSlot(0);
                    burnTicks += AbstractFurnaceBlockEntity.getFuel().get(fuelInSlot.getItem());
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

            AstralAPI.getOverworldPsychicInventory((ServerLevel) level).ifPresent(overworldPsychicInventory -> {
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

    public void extractInsertItem(Player playerEntity, InteractionHand hand) {
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

    public void extractItem(Player playerEntity, IItemHandler inventory) {
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
                    return AbstractFurnaceBlockEntity.isFuel(stack) && super.isItemValid(slot, stack);
                }
                else {
                    final Block blockFromItem = Block.byItem(stack.getItem());
                    return !AbstractFurnaceBlockEntity.isFuel(stack) && !blockFromItem.defaultBlockState().hasBlockEntity() && super.isItemValid(slot, stack);
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

    @Override
    @Nonnull
    public CompoundTag getUpdateTag() {
        CompoundTag updateTag = new CompoundTag();
        final IItemHandler itemHandler = getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElseGet(this::createHandler);
        CompoundTag fuelSlot = new CompoundTag();
        CompoundTag itemSlot = new CompoundTag();
        itemHandler.getStackInSlot(0).save(fuelSlot);
        itemHandler.getStackInSlot(1).save(itemSlot);
        updateTag.put("fuel", fuelSlot);
        updateTag.put("item", itemSlot);
        return updateTag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        final IItemHandler itemHandler = getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElseGet(this::createHandler);
        ((ItemStackHandler) itemHandler).setStackInSlot(0, ItemStack.of(tag.getCompound("fuel")));
        ((ItemStackHandler) itemHandler).setStackInSlot(1, ItemStack.of(tag.getCompound("item")));
    }

    @Override
    public void load(@Nonnull CompoundTag nbt) {
        super.load(nbt);
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
    public void saveAdditional(@Nonnull CompoundTag nbt) {
        super.saveAdditional(nbt);
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
    }
}
