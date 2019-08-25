package com.alan199921.astral.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;
import net.minecraft.world.World;
import org.checkerframework.checker.nullness.qual.NonNull;

public class PhysicalBodyEntity extends LivingEntity {

    private PlayerEntity player;

    public PhysicalBodyEntity(EntityType<? extends LivingEntity> type, World p_i48577_2_, PlayerEntity player) {
        super(type, p_i48577_2_);
        this.player = player;
    }

    @Override
    @NonNull
    public Iterable<ItemStack> getArmorInventoryList() {
        return player.getArmorInventoryList();
    }

    @Override
    @NonNull
    public ItemStack getItemStackFromSlot(EquipmentSlotType slotIn) {
        return player.getItemStackFromSlot(slotIn);
    }

    @Override
    public void setItemStackToSlot(EquipmentSlotType slotIn, ItemStack stack) {

    }

    @Override
    public HandSide getPrimaryHand() {
        return player.getPrimaryHand();
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(player.getMaxHealth());
    }
}
