package com.alan199921.astral.items;

import com.alan199921.astral.potions.Travelling;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.potion.EffectInstance;

public class TravellingMedicine extends Item {
    public TravellingMedicine(Properties properties) {
        super(new Item.Properties().food(new Food.Builder()
                .fastToEat()
                .setAlwaysEdible()
                .effect(new EffectInstance(Travelling.get(8)), 1).build()));
    }
}
