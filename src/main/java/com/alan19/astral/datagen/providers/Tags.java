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
        getBuilder(AstralTags.GARDEN_OBJECTS).add(BlockTags.LOGS, BlockTags.LEAVES, BlockTags.getCollection().getOrCreate(new ResourceLocation("forge", "dirt")), AstralTags.ETHERIC_GROWTHS);
        getBuilder(AstralTags.GARDEN_PLANTS).add(BlockTags.BEE_GROWABLES, BlockTags.FLOWER_POTS, BlockTags.FLOWERS);
        getBuilder(BlockTags.BEE_GROWABLES).add(SNOWBERRY_BUSH.get());
        getBuilder(AstralTags.ETHEREAL_VEGETATION_PLANTABLE_ON).add(ETHER_DIRT.get(), ETHER_GRASS.get());
        getBuilder(AstralTags.SMALL_ETHERIC_GROWTHS).add(CYAN_BELLEVINE.get(), CYAN_BLISTERWART.get(), CYAN_KLORID.get(), CYAN_MORKEL.get(), CYAN_PODS.get(), CYAN_CYST.get());
        getBuilder(AstralTags.LARGE_ETHERIC_GROWTHS).add(LARGE_CYAN_CYST.get(), TALL_CYAN_SWARD.get());
        getBuilder(AstralTags.ETHERIC_GROWTHS).add(AstralTags.SMALL_ETHERIC_GROWTHS, AstralTags.LARGE_ETHERIC_GROWTHS);
    }
}
