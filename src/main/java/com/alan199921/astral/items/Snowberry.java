package com.alan199921.astral.items;

import com.alan199921.astral.Astral;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class Snowberry extends Item {
    public Snowberry() {
        super(new Properties()
                .group(Astral.setup.itemGroup)
                .food(new Food.Builder()
                        .setAlwaysEdible()
                        .saturation(1)
                        .hunger(1)
                        .fastToEat()
                        .effect(new EffectInstance(Effects.NAUSEA, 15 * 20, 1), 1)
                        .effect(new EffectInstance(Effects.REGENERATION, 15 * 20, 1), 1)
                        .build()));
        setRegistryName("snowberry");
    }
}
