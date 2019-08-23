package com.alan199921.astral.items;

import com.alan199921.astral.effects.ModEffects;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.potion.EffectInstance;

public class TravellingMedicine extends Item {
    public TravellingMedicine(Properties properties) {
        super(new Item.Properties().food(new Food.Builder()
                .fastToEat()
                .setAlwaysEdible()
                .effect(new EffectInstance(ModEffects.astralEffect, 100, 1), 1).build()));
    }
}
