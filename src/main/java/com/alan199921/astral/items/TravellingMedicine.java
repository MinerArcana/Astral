package com.alan199921.astral.items;

import com.alan199921.astral.Astral;
import com.alan199921.astral.effects.ModEffects;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.potion.EffectInstance;

public class TravellingMedicine extends Item {

    /**
     * Gives the Astral Travel potion effect when consumed for 60 seconds //TODO Make the effect be dependent on config
     */
    public TravellingMedicine() {
        super(new Item.Properties()
                .group(Astral.setup.astralItems)
                .food(new Food.Builder()
                        .fastToEat()
                        .setAlwaysEdible()
                        .effect(new EffectInstance(ModEffects.astralEffect, 1200, 1), 1)
                        .build()));
    }
}
