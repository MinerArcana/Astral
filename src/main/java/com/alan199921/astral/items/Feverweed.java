package com.alan199921.astral.items;

import com.alan199921.astral.Astral;
import com.alan199921.astral.Config;
import com.alan199921.astral.blocks.ModBlocks;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class Feverweed extends BlockNamedItem {
    public Feverweed() {
        super(ModBlocks.feverweedBlock.getBlock(), new Item.Properties()
                .group(Astral.setup.astralItems)
                .food(new Food.Builder()
                        .setAlwaysEdible()
                        .saturation(1)
                        .hunger(1)
                        .fastToEat()
                        .effect(new EffectInstance(Effects.LUCK, Config.COMMON.feverweedLuckDuration.get(), 1), 1)
                        .effect(new EffectInstance(Effects.HUNGER, Config.COMMON.feverweedHungerDuration.get(), 1), 1)
                        .build()));
        setRegistryName("feverweed");
    }
}
