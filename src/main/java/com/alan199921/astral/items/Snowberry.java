package com.alan199921.astral.items;

import com.alan199921.astral.Astral;
import com.alan199921.astral.configs.AstralConfig;
import com.alan199921.astral.blocks.ModBlocks;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class Snowberry extends BlockNamedItem {
    public Snowberry() {
        super(ModBlocks.snowberryBush.getBlock(), new Item.Properties()
                .group(Astral.setup.astralItems)
                .food(new Food.Builder()
                        .setAlwaysEdible()
                        .saturation(1)
                        .hunger(1)
                        .fastToEat()
                        .effect(new EffectInstance(Effects.NAUSEA, AstralConfig.getHerbEffectDurations().getSnowberryNauseaDuration(), 1), 1)
                        .effect(new EffectInstance(Effects.REGENERATION, AstralConfig.getHerbEffectDurations().getSnowberryRegenerationDuration(), 1), 1)
                        .build()));
        setRegistryName("snowberry");
    }


}
