package com.alan19.astral.data.providers;

import com.alan19.astral.Astral;
import com.alan19.astral.blocks.etherealblocks.Ethereal;
import com.alan19.astral.blocks.etherealblocks.EthericGrowth;
import com.alan19.astral.blocks.etherealblocks.TallEthericGrowth;
import com.alan19.astral.tags.AstralTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
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
    protected void addTags() {
        tag(AstralTags.GARDEN_OBJECTS).addTags(BlockTags.LOGS, BlockTags.LEAVES, Tags.Blocks.DIRT, AstralTags.ETHERIC_GROWTHS);
        tag(AstralTags.GARDEN_PLANTS).addTags(BlockTags.BEE_GROWABLES, BlockTags.FLOWER_POTS, BlockTags.FLOWERS, AstralTags.ETHERIC_GROWTHS);
        tag(BlockTags.BEE_GROWABLES).add(SNOWBERRY_BUSH.get());
        tag(AstralTags.ETHEREAL_VEGETATION_PLANTABLE_ON).add(ETHER_DIRT.get(), ETHER_GRASS.get());
        tag(AstralTags.SMALL_ETHERIC_GROWTHS).add(getAllBlocksOfType(EthericGrowth.class));
        tag(AstralTags.LARGE_ETHERIC_GROWTHS).add(getAllBlocksOfType(TallEthericGrowth.class));
        tag(AstralTags.ETHERIC_GROWTHS).addTags(AstralTags.SMALL_ETHERIC_GROWTHS, AstralTags.LARGE_ETHERIC_GROWTHS);
        tag(BlockTags.LOGS).add(ETHEREAL_LOG.get(), ETHEREAL_WOOD.get(), STRIPPED_ETHEREAL_LOG.get(), STRIPPED_ETHEREAL_WOOD.get());
        tag(BlockTags.PLANKS).add(ETHEREAL_PLANKS.get());
        tag(BlockTags.LEAVES).add(ETHEREAL_LEAVES.get());
        tag(BlockTags.SAPLINGS).add(ETHEREAL_SAPLING.get());
        tag(BlockTags.FLOWERS).addTags(AstralTags.SMALL_ETHERIC_GROWTHS, AstralTags.LARGE_ETHERIC_GROWTHS);
        tag(BlockTags.SMALL_FLOWERS).addTags(AstralTags.SMALL_ETHERIC_GROWTHS);
        tag(BlockTags.TALL_FLOWERS).addTags(AstralTags.LARGE_ETHERIC_GROWTHS);
        tag(BlockTags.BUTTONS).add(ETHERIC_POWDER.get());
        tag(AstralTags.ASTRAL_INTERACT).add(ArrayUtils.addAll(getAllBlocksOfType(Ethereal.class), ETHERIC_POWDER.get()));
        tag(Tags.Blocks.DIRT).add(ETHER_DIRT.get(), ETHER_GRASS.get());
        tag(AstralTags.SNOWBERRY_SUSTAIN).add(Blocks.SNOW_BLOCK, Blocks.COARSE_DIRT, Blocks.GRAVEL, Blocks.PACKED_ICE, Blocks.DIRT, Blocks.GRASS_BLOCK);
        tag(AstralTags.FEVERWEED_SUSTAIN).add(Blocks.PODZOL, Blocks.MYCELIUM);
    }

    private Block[] getAllBlocksOfType(Class<?> aClass) {
        return BLOCKS.getEntries().stream().map(RegistryObject::get).filter(aClass::isInstance).collect(Collectors.toList()).toArray(new Block[]{});
    }
}
