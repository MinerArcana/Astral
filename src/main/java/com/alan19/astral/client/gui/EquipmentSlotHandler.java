package com.alan19.astral.client.gui;

import com.mojang.datafixers.util.Pair;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class EquipmentSlotHandler extends SlotItemHandler {
    public final ResourceLocation emptyArmorSlotHelmet = new ResourceLocation("item/empty_armor_slot_helmet");
    public final ResourceLocation emptyArmorSlotChestplate = new ResourceLocation("item/empty_armor_slot_chestplate");
    public final ResourceLocation emptyArmorSlotLeggings = new ResourceLocation("item/empty_armor_slot_leggings");
    public final ResourceLocation emptyArmorSlotBoots = new ResourceLocation("item/empty_armor_slot_boots");
    public final ResourceLocation emptyArmorSlotShield = new ResourceLocation("item/empty_armor_slot_shield");
    private final ResourceLocation[] armorSlotTextures = new ResourceLocation[]{emptyArmorSlotBoots, emptyArmorSlotLeggings, emptyArmorSlotChestplate, emptyArmorSlotHelmet};
    private final EquipmentSlotType type;
    private final PlayerEntity playerEntity;

    public EquipmentSlotHandler(EquipmentSlotType type, PlayerEntity playerEntity, IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
        this.type = type;
        this.playerEntity = playerEntity;
    }

    /**
     * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1 in
     * the case of armor slots)
     */
    @Override
    public int getSlotStackLimit() {
        return 1;
    }

    /**
     * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
     */
    @Override
    public boolean isItemValid(ItemStack stack) {
        return stack.canEquip(type, playerEntity);
    }

    /**
     * Return whether this slot's stack can be taken from this slot.
     */
    @Override
    public boolean canTakeStack(PlayerEntity playerIn) {
        ItemStack itemstack = this.getStack();
        return (itemstack.isEmpty() || playerIn.isCreative() || !EnchantmentHelper.hasBindingCurse(itemstack)) && super.canTakeStack(playerIn);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Pair<ResourceLocation, ResourceLocation> getBackground() {
        return Pair.of(PlayerContainer.LOCATION_BLOCKS_TEXTURE, armorSlotTextures[type.getIndex()]);
    }
}
