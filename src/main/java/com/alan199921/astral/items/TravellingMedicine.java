package com.alan199921.astral.items;

import com.alan199921.astral.Astral;
import com.alan199921.astral.configs.AstralConfig;
import com.alan199921.astral.effects.AstralEffects;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.potion.EffectInstance;

public class TravellingMedicine extends Item {

    /**
     * Gives the Astral Travel potion effect when consumed for 60 seconds
     */
    public TravellingMedicine() {
        super(new Item.Properties()
                .group(Astral.setup.astralItems)
                .food(new Food.Builder()
                        .fastToEat()
                        .setAlwaysEdible()
                        .effect(new EffectInstance(AstralEffects.astralTravelEffect, AstralConfig.getHerbEffectDurations().getTravellingMedicineDuration(), 1), 1)
                        .build()));
    }
}
