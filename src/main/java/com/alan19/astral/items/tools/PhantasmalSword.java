package com.alan19.astral.items.tools;

import com.alan19.astral.blocks.AstralBlocks;
import com.alan19.astral.entity.AstralModifiers;
import com.alan19.astral.items.AstralItems;
import com.alan19.astral.util.Constants;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

public class PhantasmalSword extends SwordItem {

    private static final float attackSpeedIn = -2.4f;

    public PhantasmalSword() {
        super(Tiers.STONE, 2, attackSpeedIn, new Properties().tab(CreativeModeTab.TAB_COMBAT).tab(AstralItems.ASTRAL_ITEMS));
    }

    @Override
    @Nonnull
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@Nonnull EquipmentSlot equipmentSlot) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", getDamage(), AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", attackSpeedIn, AttributeModifier.Operation.ADDITION));
        builder.put(AstralModifiers.ASTRAL_ATTACK_DAMAGE.get(), new AttributeModifier(Constants.ASTRAL_DAMAGE_BOOST, "Astral weapon modifier", getDamage() * 2, AttributeModifier.Operation.ADDITION));

        if (equipmentSlot == EquipmentSlot.MAINHAND) {
            return builder.build();
        }
        return ImmutableMultimap.of();
    }

    @Override
    public boolean isCorrectToolForDrops(@Nonnull BlockState blockIn) {
        return super.isCorrectToolForDrops(blockIn) || blockIn.getBlock() == AstralBlocks.CRYSTAL_WEB.get();
    }
}
