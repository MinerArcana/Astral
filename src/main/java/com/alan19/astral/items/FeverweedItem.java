package com.alan19.astral.items;

import com.alan19.astral.blocks.AstralBlocks;
import com.alan19.astral.configs.AstralConfig;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;

public class FeverweedItem extends ItemNameBlockItem {

    /**
     * Feverweed heals 1 saturation and hunger
     * Gives luck 2 and hunger 2 for 15 seconds by default (duration is affected by configs)
     */
    public FeverweedItem() {
        super(AstralBlocks.FEVERWEED_BLOCK.get(), new Item.Properties()
                .tab(AstralItems.ASTRAL_ITEMS)
                .food(new FoodProperties.Builder()
                        .alwaysEat()
                        .saturationMod(-1F)
                        .nutrition(1)
                        .fast()
                        .effect(() -> new MobEffectInstance(MobEffects.LUCK, AstralConfig.getEffectDuration().feverweedLuckDuration.get(), 1), 1)
                        .effect(() -> new MobEffectInstance(MobEffects.HUNGER, AstralConfig.getEffectDuration().feverweedHungerDuration.get(), 1), 1)
                        .build()));
    }
}
