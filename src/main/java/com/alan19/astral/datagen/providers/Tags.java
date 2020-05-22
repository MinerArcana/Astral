package com.alan19.astral.datagen.providers;

import com.alan19.astral.tags.AstralTags;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;

import static com.alan19.astral.blocks.AstralBlocks.*;

public class Tags extends BlockTagsProvider {
    public Tags(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerTags() {
        getBuilder(AstralTags.GARDEN_OBJECTS).add(BlockTags.LOGS, BlockTags.LEAVES, BlockTags.getCollection().getOrCreate(new ResourceLocation("forge", "dirt")));
        getBuilder(AstralTags.GARDEN_PLANTS).add(BlockTags.BEE_GROWABLES, BlockTags.FLOWER_POTS, BlockTags.FLOWERS);
        getBuilder(BlockTags.BEE_GROWABLES).add(SNOWBERRY_BUSH.get());
        getBuilder(AstralTags.ETHEREAL_GROWTH_PLANTABLE_ON).add(ETHER_DIRT.get(), ETHER_GRASS.get());
    }
}
