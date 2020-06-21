package com.alan19.astral.items.tools;

import com.alan19.astral.items.AstralItems;
import com.alan19.astral.util.Constants;
import com.google.common.collect.Multimap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemTier;

import javax.annotation.Nonnull;

public class PhantasmalAxe extends AxeItem {
    public PhantasmalAxe() {
        super(ItemTier.STONE, 3F, -3.2F, new Properties().group(ItemGroup.TOOLS).group(AstralItems.ASTRAL_ITEMS));
    }

    @Override
    @Nonnull
    public Multimap<String, AttributeModifier> getAttributeModifiers(@Nonnull EquipmentSlotType equipmentSlot) {
        final Multimap<String, AttributeModifier> defaultAttributeModifiers = super.getAttributeModifiers(equipmentSlot);
        if (equipmentSlot == EquipmentSlotType.MAINHAND) {
            defaultAttributeModifiers.put(Constants.ASTRAL_ATTACK_DAMAGE.getName(), new AttributeModifier(Constants.ASTRAL_EFFECT_DAMAGE_BOOST, "Astral weapon modifier", attackDamage * 2, AttributeModifier.Operation.ADDITION));
        }
        return defaultAttributeModifiers;
    }


}
