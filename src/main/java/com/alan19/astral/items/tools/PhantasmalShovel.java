package com.alan19.astral.items.tools;

import com.alan19.astral.entity.AstralModifiers;
import com.alan19.astral.items.AstralItems;
import com.alan19.astral.util.Constants;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemTier;
import net.minecraft.item.ShovelItem;

import javax.annotation.Nonnull;

public class PhantasmalShovel extends ShovelItem {

    private static final float attackSpeedIn = -3F;

    public PhantasmalShovel() {
        super(ItemTier.STONE, .75F, attackSpeedIn, new Properties().group(ItemGroup.TOOLS).group(AstralItems.ASTRAL_ITEMS));
    }

    @Override
    @Nonnull
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(@Nonnull EquipmentSlotType equipmentSlot) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", getAttackDamage(), AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", attackSpeedIn, AttributeModifier.Operation.ADDITION));
        builder.put(AstralModifiers.ASTRAL_ATTACK_DAMAGE.get(), new AttributeModifier(Constants.ASTRAL_DAMAGE_BOOST, "Astral weapon modifier", getAttackDamage() * 2, AttributeModifier.Operation.ADDITION));

        if (equipmentSlot == EquipmentSlotType.MAINHAND) {
            return builder.build();
        }
        return ImmutableMultimap.of();
    }

}
