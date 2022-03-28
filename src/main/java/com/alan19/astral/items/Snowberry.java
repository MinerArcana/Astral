package com.alan19.astral.items;

import com.alan19.astral.blocks.AstralBlocks;
import com.alan19.astral.configs.AstralConfig;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;

public class Snowberry extends ItemNameBlockItem {
    /**
     * Snowberries heal 1 saturation and hunger
     * Gives regeneration 2 and nausea 2 for 15 seconds (depends on configs)
     */
    public Snowberry() {
        super(AstralBlocks.SNOWBERRY_BUSH.get(), new Item.Properties()
                .tab(AstralItems.ASTRAL_ITEMS)
                .food(new FoodProperties.Builder()
                        .alwaysEat()
                        .saturationMod(-1F)
                        .nutrition(1)
                        .fast()
                        .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, AstralConfig.getEffectDuration().snowberryNauseaDuration.get(), 1), 1)
                        .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, AstralConfig.getEffectDuration().snowberryRegenerationDuration.get(), 1), 1)
                        .build()));
    }
}
