package com.alan199921.astral.entities;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class PhysicalBodyEntity extends LivingEntity implements IItemHandler {
    private final NonNullList<ItemStack> mainInventory = NonNullList.withSize(6*7, ItemStack.EMPTY);

    protected PhysicalBodyEntity(EntityType<? extends LivingEntity> p_i48577_1_, World p_i48577_2_) {
        super(p_i48577_1_, p_i48577_2_);
    }

    @Override
    public Iterable<ItemStack> getArmorInventoryList() {
        return new ArrayList<>();
    }

    @Override
    public ItemStack getItemStackFromSlot(EquipmentSlotType slotIn) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemStackToSlot(EquipmentSlotType slotIn, ItemStack stack) {

    }

    @Nonnull
    @Override
    public HandSide getPrimaryHand() {
        return HandSide.RIGHT;
    }


    public void setName(String name){
        this.setCustomName(new StringTextComponent(name + "'s Body"));
    }

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

    public NonNullList<ItemStack> getInventory() {
        return mainInventory;
    }

    public GameProfile getGameProfile() {
        return null;
    }
}
