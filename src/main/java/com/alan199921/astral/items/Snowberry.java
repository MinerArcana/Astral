package com.alan199921.astral.items;

import com.alan199921.astral.blocks.AstralBlocks;
import com.alan199921.astral.configs.AstralConfig;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class Snowberry extends BlockNamedItem {
    /**
     * Snowberries heal 1 saturation and hunger
     * Gives regeneration 2 and nausea 2 for 15 seconds (depends on configs)
     */
    public Snowberry() {
        super(AstralBlocks.SNOWBERRY_BUSH.get(), new Item.Properties()
                .group(AstralItems.ASTRAL_ITEMS)
                .food(new Food.Builder()
                        .setAlwaysEdible()
                        .saturation(-1F)
                        .hunger(1)
                        .fastToEat()
                        .effect(() -> new EffectInstance(Effects.NAUSEA, AstralConfig.getHerbEffectDurations().getSnowberryNauseaDuration(), 1), 1)
                        .effect(() -> new EffectInstance(Effects.REGENERATION, AstralConfig.getHerbEffectDurations().getSnowberryRegenerationDuration(), 1), 1)
                        .build()));
    }
}
