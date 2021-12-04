package com.alan19.astral.items;

import com.alan19.astral.api.AstralAPI;
import com.alan19.astral.effects.AstralEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public class IntrospectionMedicine extends Item {
    public IntrospectionMedicine() {
        super(new Properties()
                .food(new FoodProperties.Builder()
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
    public ItemStack finishUsingItem(@Nonnull ItemStack stack, @Nonnull Level worldIn, @Nonnull LivingEntity entityLiving) {
        if (entityLiving instanceof Player) {
            Player playerEntity = (Player) entityLiving;
            playerEntity.addItem(new ItemStack(Items.BOWL));
            playerEntity.addEffect(new MobEffectInstance(AstralEffects.ASTRAL_TRAVEL.get(), Integer.MAX_VALUE));
            playerEntity.getCapability(AstralAPI.sleepManagerCapability).ifPresent(cap -> cap.setGoingToInnerRealm(true));
        }
        super.finishUsingItem(stack, worldIn, entityLiving);
        return ItemStack.EMPTY;
    }

    @Override
    @Nonnull
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }
}
