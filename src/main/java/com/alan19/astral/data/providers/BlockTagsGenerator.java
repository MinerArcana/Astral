package com.alan19.astral.data.providers;

import com.alan19.astral.blocks.etherealblocks.Ethereal;
import com.alan19.astral.blocks.etherealblocks.EthericGrowth;
import com.alan19.astral.blocks.etherealblocks.TallEthericGrowth;
import com.alan19.astral.tags.AstralTags;
import net.minecraft.block.Block;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import org.apache.commons.lang3.ArrayUtils;

import java.util.stream.Collectors;

import static com.alan19.astral.blocks.AstralBlocks.*;

public class BlockTagsGenerator extends BlockTagsProvider {
    public BlockTagsGenerator(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerTags() {
        getBuilder(AstralTags.GARDEN_OBJECTS).add(BlockTags.LOGS, BlockTags.LEAVES, BlockTags.getCollection().getOrCreate(new ResourceLocation("forge", "dirt")), AstralTags.ETHERIC_GROWTHS);
        getBuilder(AstralTags.GARDEN_PLANTS).add(BlockTags.BEE_GROWABLES, BlockTags.FLOWER_POTS, BlockTags.FLOWERS, AstralTags.ETHERIC_GROWTHS);
        getBuilder(BlockTags.BEE_GROWABLES).add(SNOWBERRY_BUSH.get());
        getBuilder(AstralTags.ETHEREAL_VEGETATION_PLANTABLE_ON).add(ETHER_DIRT.get(), ETHER_GRASS.get());
        getBuilder(AstralTags.SMALL_ETHERIC_GROWTHS).add(getAllBlocksOfType(EthericGrowth.class));
        getBuilder(AstralTags.LARGE_ETHERIC_GROWTHS).add(getAllBlocksOfType(TallEthericGrowth.class));
        getBuilder(AstralTags.ETHERIC_GROWTHS).add(AstralTags.SMALL_ETHERIC_GROWTHS, AstralTags.LARGE_ETHERIC_GROWTHS);
        getBuilder(AstralTags.ASTRAL_INTERACT).add(ArrayUtils.add(getAllBlocksOfType(Ethereal.class), ETHERIC_POWDER.get()));
    }

    private Block[] getAllBlocksOfType(Class<?> aClass) {
        return BLOCKS.getEntries().stream().map(RegistryObject::get).filter(aClass::isInstance).collect(Collectors.toList()).toArray(new Block[]{});
    }
}
