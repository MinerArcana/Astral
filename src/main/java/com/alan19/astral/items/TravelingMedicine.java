package com.alan19.astral.items;

import com.alan19.astral.configs.AstralConfig;
import com.alan19.astral.effects.AstralEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class TravelingMedicine extends Item {

    /**
     * Gives the Astral Travel potion effect when consumed for 60 seconds
     */
    public TravelingMedicine() {
        super(new Item.Properties()
                .group(AstralItems.ASTRAL_ITEMS)
                .food(new Food.Builder()
                        .fastToEat()
                        .setAlwaysEdible()
                        .saturation(-2F)
                        .hunger(1)
                        .effect(new EffectInstance(AstralEffects.ASTRAL_TRAVEL.get(), AstralConfig.getHerbEffectDurations().getTravellingMedicineDuration(), 0), 1)
                        .build())
                .maxStackSize(1));
    }

    @Override
    @Nonnull
    public UseAction getUseAction(@Nonnull ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    @Nonnull
    public ItemStack onItemUseFinish(@Nonnull ItemStack stack, @Nonnull World worldIn, @Nonnull LivingEntity entityLiving) {
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity) entityLiving;
            playerEntity.addItemStackToInventory(new ItemStack(Items.BOWL));
        }
        super.onItemUseFinish(stack, worldIn, entityLiving);
        return ItemStack.EMPTY;
    }
}
