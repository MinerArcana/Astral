package com.alan19.astral.client.gui;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class EquipmentSlotHandler extends SlotItemHandler {
    public final ResourceLocation emptyArmorSlotHelmet = new ResourceLocation("item/empty_armor_slot_helmet");
    public final ResourceLocation emptyArmorSlotChestplate = new ResourceLocation("item/empty_armor_slot_chestplate");
    public final ResourceLocation emptyArmorSlotLeggings = new ResourceLocation("item/empty_armor_slot_leggings");
    public final ResourceLocation emptyArmorSlotBoots = new ResourceLocation("item/empty_armor_slot_boots");
    public final ResourceLocation emptyArmorSlotShield = new ResourceLocation("item/empty_armor_slot_shield");
    private final ResourceLocation[] armorSlotTextures = new ResourceLocation[]{emptyArmorSlotBoots, emptyArmorSlotLeggings, emptyArmorSlotChestplate, emptyArmorSlotHelmet};
    private final EquipmentSlot type;
    private final Player playerEntity;

    public EquipmentSlotHandler(EquipmentSlot type, Player playerEntity, IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
        this.type = type;
        this.playerEntity = playerEntity;
    }

    /**
     * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1 in
     * the case of armor slots)
     */
    @Override
    public int getMaxStackSize() {
        return type == EquipmentSlot.OFFHAND ? 64 : 1;
    }

    /**
     * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
     */
    @Override
    public boolean mayPlace(@Nonnull ItemStack stack) {
        return type == EquipmentSlot.OFFHAND || stack.canEquip(type, playerEntity);
    }

    /**
     * Return whether this slot's stack can be taken from this slot.
     */
    @Override
    public boolean mayPickup(Player playerIn) {
        ItemStack itemstack = this.getItem();
        return (itemstack.isEmpty() || playerIn.isCreative() || !EnchantmentHelper.hasBindingCurse(itemstack)) && super.mayPickup(playerIn);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
        return Pair.of(InventoryMenu.BLOCK_ATLAS, type == EquipmentSlot.OFFHAND ? emptyArmorSlotShield : armorSlotTextures[type.getIndex()]);
    }
}
