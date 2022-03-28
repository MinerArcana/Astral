package com.alan19.astral.items;

import com.alan19.astral.configs.AstralConfig;
import com.alan19.astral.effects.AstralEffects;
import net.minecraft.item.*;
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

public class TravelingMedicine extends Item {

    /**
     * Gives the Astral Travel potion effect when consumed for 60 seconds
     */
    public TravelingMedicine() {
        super(new Item.Properties()
                .tab(AstralItems.ASTRAL_ITEMS)
                .food(new FoodProperties.Builder()
                        .fast()
                        .alwaysEat()
                        .saturationMod(-2F)
                        .nutrition(1)
                        .effect(() -> new MobEffectInstance(AstralEffects.ASTRAL_TRAVEL.get(), AstralConfig.getEffectDuration().travelingMedicineDuration.get(), 0), 1)
                        .build())
                .stacksTo(1));
    }

    @Override
    @Nonnull
    public UseAnim getUseAnimation(@Nonnull ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    @Nonnull
    public ItemStack finishUsingItem(@Nonnull ItemStack stack, @Nonnull Level worldIn, @Nonnull LivingEntity entityLiving) {
        if (entityLiving instanceof Player) {
            Player playerEntity = (Player) entityLiving;
            playerEntity.addItem(new ItemStack(Items.BOWL));
        }
        super.finishUsingItem(stack, worldIn, entityLiving);
        return ItemStack.EMPTY;
    }
}
