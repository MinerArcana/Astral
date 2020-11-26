package com.alan19.astral.data.providers;

import com.alan19.astral.Astral;
import com.alan19.astral.blocks.etherealblocks.Ethereal;
import com.alan19.astral.blocks.etherealblocks.EthericGrowth;
import com.alan19.astral.blocks.etherealblocks.TallEthericGrowth;
import com.alan19.astral.tags.AstralTags;
import net.minecraft.block.Block;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nullable;
import java.util.stream.Collectors;

import static com.alan19.astral.blocks.AstralBlocks.*;

@SuppressWarnings("unchecked")
public class BlockTagsGenerator extends BlockTagsProvider {

    public BlockTagsGenerator(DataGenerator generatorIn, @Nullable ExistingFileHelper existingFileHelper) {
        super(generatorIn, Astral.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerTags() {
        getOrCreateBuilder(AstralTags.GARDEN_OBJECTS).addTags(BlockTags.LOGS, BlockTags.LEAVES, Tags.Blocks.DIRT, AstralTags.ETHERIC_GROWTHS);
        getOrCreateBuilder(AstralTags.GARDEN_PLANTS).addTags(BlockTags.BEE_GROWABLES, BlockTags.FLOWER_POTS, BlockTags.FLOWERS, AstralTags.ETHERIC_GROWTHS);
        getOrCreateBuilder(BlockTags.BEE_GROWABLES).add(SNOWBERRY_BUSH.get());
        getOrCreateBuilder(AstralTags.ETHEREAL_VEGETATION_PLANTABLE_ON).add(ETHER_DIRT.get(), ETHER_GRASS.get());
        getOrCreateBuilder(AstralTags.SMALL_ETHERIC_GROWTHS).add(getAllBlocksOfType(EthericGrowth.class));
        getOrCreateBuilder(AstralTags.LARGE_ETHERIC_GROWTHS).add(getAllBlocksOfType(TallEthericGrowth.class));
        getOrCreateBuilder(AstralTags.ETHERIC_GROWTHS).addTags(AstralTags.SMALL_ETHERIC_GROWTHS, AstralTags.LARGE_ETHERIC_GROWTHS);
        getOrCreateBuilder(BlockTags.LOGS).add(ETHEREAL_LOG.get(), ETHEREAL_WOOD.get(), STRIPPED_ETHEREAL_LOG.get(), STRIPPED_ETHEREAL_WOOD.get());
        getOrCreateBuilder(BlockTags.PLANKS).add(ETHEREAL_PLANKS.get());
        getOrCreateBuilder(BlockTags.LEAVES).add(ETHEREAL_LEAVES.get());
        getOrCreateBuilder(BlockTags.SAPLINGS).add(ETHEREAL_SAPLING.get());
        getOrCreateBuilder(BlockTags.FLOWERS).addTags(AstralTags.SMALL_ETHERIC_GROWTHS, AstralTags.LARGE_ETHERIC_GROWTHS);
        getOrCreateBuilder(BlockTags.SMALL_FLOWERS).addTags(AstralTags.SMALL_ETHERIC_GROWTHS);
        getOrCreateBuilder(BlockTags.TALL_FLOWERS).addTags(AstralTags.LARGE_ETHERIC_GROWTHS);
        getOrCreateBuilder(BlockTags.BUTTONS).add(ETHERIC_POWDER.get());
        getOrCreateBuilder(AstralTags.ASTRAL_INTERACT).add(ArrayUtils.addAll(getAllBlocksOfType(Ethereal.class), ETHERIC_POWDER.get()));
    }

    private Block[] getAllBlocksOfType(Class<?> aClass) {
        return BLOCKS.getEntries().stream().map(RegistryObject::get).filter(aClass::isInstance).collect(Collectors.toList()).toArray(new Block[]{});
    }
}
