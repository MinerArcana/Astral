package com.alan199921.astral.items;

import net.minecraft.item.Food;
import net.minecraft.item.Item;

public class TravellingMedicine extends Item {
    public TravellingMedicine(Properties properties) {
        new Item.Properties().food(new Food.Builder().fastToEat().setAlwaysEdible().effect())
    }
}
