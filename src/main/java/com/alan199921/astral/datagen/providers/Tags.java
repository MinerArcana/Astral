package com.alan199921.astral.datagen.providers;

import com.alan199921.astral.blocks.AstralBlocks;
import com.alan199921.astral.tags.AstralTags;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;

public class Tags extends BlockTagsProvider {
    public Tags(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerTags() {
        getBuilder(AstralTags.GARDEN_OBJECTS).add(BlockTags.LOGS, BlockTags.LEAVES, BlockTags.getCollection().getOrCreate(new ResourceLocation("forge", "dirt")));
        getBuilder(AstralTags.GARDEN_PLANTS).add(BlockTags.BEE_GROWABLES, BlockTags.FLOWER_POTS, BlockTags.FLOWERS);
        getBuilder(BlockTags.BEE_GROWABLES).add(AstralBlocks.SNOWBERRY_BUSH.get());
    }
}
