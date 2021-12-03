package com.alan19.astral.items;

import com.alan19.astral.api.AstralAPI;
import com.alan19.astral.effects.AstralEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

import net.minecraft.item.Item.Properties;

public class IntrospectionMedicine extends Item {
    public IntrospectionMedicine() {
        super(new Properties()
                .food(new Food.Builder()
                        .alwaysEat()
                        .nutrition(1)
                        .saturationMod(-2F)
                        .build())
                .tab(AstralItems.ASTRAL_ITEMS)
                .stacksTo(1)
        );
    }

    @Nonnull
    @Override
    public ItemStack finishUsingItem(@Nonnull ItemStack stack, @Nonnull World worldIn, @Nonnull LivingEntity entityLiving) {
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity) entityLiving;
            playerEntity.addItem(new ItemStack(Items.BOWL));
            playerEntity.addEffect(new EffectInstance(AstralEffects.ASTRAL_TRAVEL.get(), Integer.MAX_VALUE));
            playerEntity.getCapability(AstralAPI.sleepManagerCapability).ifPresent(cap -> cap.setGoingToInnerRealm(true));
        }
        super.finishUsingItem(stack, worldIn, entityLiving);
        return ItemStack.EMPTY;
    }

    @Override
    @Nonnull
    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.DRINK;
    }
}
