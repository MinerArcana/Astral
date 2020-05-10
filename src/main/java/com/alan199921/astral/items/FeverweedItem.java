package com.alan199921.astral.items;

import com.alan199921.astral.blocks.AstralBlocks;
import com.alan199921.astral.configs.AstralConfig;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class FeverweedItem extends BlockNamedItem {

    /**
     * Feverweed heals 1 saturation and hunger
     * Gives luck 2 and hunger 2 for 15 seconds by default (duration is affected by configs)
     */
    public FeverweedItem() {
        super(AstralBlocks.FEVERWEED_BLOCK.get(), new Item.Properties()
                .group(AstralItems.ASTRAL_ITEMS)
                .food(new Food.Builder()
                        .setAlwaysEdible()
                        .saturation(-1F)
                        .hunger(1)
                        .fastToEat()
                        .effect(() -> new EffectInstance(Effects.LUCK, AstralConfig.getHerbEffectDurations().getFeverweedLuckDuration(), 1), 1)
                        .effect(() -> new EffectInstance(Effects.HUNGER, AstralConfig.getHerbEffectDurations().getFeverweedHungerDuration(), 1), 1)
                        .build()));
    }
}
