package com.alan19.astral.data.providers;

import com.alan19.astral.blocks.etherealblocks.Ethereal;
import com.alan19.astral.tags.AstralTags;
import net.minecraft.block.Block;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;

import java.util.stream.Collectors;

import static com.alan19.astral.blocks.AstralBlocks.*;

public class BlockTagsGenerator extends BlockTagsProvider {
    public BlockTagsGenerator(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerTags() {
        getBuilder(AstralTags.GARDEN_OBJECTS).add(net.minecraft.tags.BlockTags.LOGS, net.minecraft.tags.BlockTags.LEAVES, net.minecraft.tags.BlockTags.getCollection().getOrCreate(new ResourceLocation("forge", "dirt")), AstralTags.ETHERIC_GROWTHS);
        getBuilder(AstralTags.GARDEN_PLANTS).add(net.minecraft.tags.BlockTags.BEE_GROWABLES, net.minecraft.tags.BlockTags.FLOWER_POTS, net.minecraft.tags.BlockTags.FLOWERS);
        getBuilder(net.minecraft.tags.BlockTags.BEE_GROWABLES).add(SNOWBERRY_BUSH.get());
        getBuilder(AstralTags.ETHEREAL_VEGETATION_PLANTABLE_ON).add(ETHER_DIRT.get(), ETHER_GRASS.get());
        getBuilder(AstralTags.SMALL_ETHERIC_GROWTHS).add(CYAN_BELLEVINE.get(), CYAN_BLISTERWART.get(), CYAN_KLORID.get(), CYAN_MORKEL.get(), CYAN_PODS.get(), CYAN_CYST.get());
        getBuilder(AstralTags.LARGE_ETHERIC_GROWTHS).add(LARGE_CYAN_CYST.get(), TALL_CYAN_SWARD.get());
        getBuilder(AstralTags.ETHERIC_GROWTHS).add(AstralTags.SMALL_ETHERIC_GROWTHS, AstralTags.LARGE_ETHERIC_GROWTHS);
        final Block[] etherealBlocks = BLOCKS.getEntries().stream().map(RegistryObject::get).filter(block -> block instanceof Ethereal).collect(Collectors.toList()).toArray(new Block[]{});
        getBuilder(AstralTags.ASTRAL_INTERACT).add(etherealBlocks);
        getBuilder(AstralTags.ASTRAL_INTERACT).add(ETHERIC_POWDER.get());
    }
}
