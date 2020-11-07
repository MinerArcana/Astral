package com.alan19.astral.items.tools;

import com.alan19.astral.blocks.AstralBlocks;
import com.alan19.astral.items.AstralItems;
import com.alan19.astral.util.Constants;
import com.google.common.collect.Multimap;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemTier;
import net.minecraft.item.SwordItem;

import javax.annotation.Nonnull;

public class PhantasmalSword extends SwordItem {
    public PhantasmalSword() {
        super(ItemTier.STONE, 2, -2.4f, new Properties().group(ItemGroup.COMBAT).group(AstralItems.ASTRAL_ITEMS));
    }

    @Override
    @Nonnull
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(@Nonnull EquipmentSlotType equipmentSlot) {
        final Multimap<Attribute, AttributeModifier> defaultAttributeModifiers = super.getAttributeModifiers(equipmentSlot);
        if (equipmentSlot == EquipmentSlotType.MAINHAND) {
            defaultAttributeModifiers.put(Constants.ASTRAL_ATTACK_DAMAGE, new AttributeModifier(Constants.ASTRAL_EFFECT_DAMAGE_BOOST, "Astral weapon modifier", getAttackDamage() * 2, AttributeModifier.Operation.ADDITION));
        }
        return defaultAttributeModifiers;
    }

    @Override
    public boolean canHarvestBlock(@Nonnull BlockState blockIn) {
        return super.canHarvestBlock(blockIn) || blockIn.getBlock() == AstralBlocks.CRYSTAL_WEB.get();
    }
}
