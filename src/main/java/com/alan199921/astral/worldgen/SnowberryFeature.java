package com.alan199921.astral.worldgen;

import com.alan199921.astral.blocks.ModBlocks;
import com.mojang.datafixers.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class SnowberryFeature extends Feature<NoFeatureConfig> {
    public SnowberryFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn) {
        super(configFactoryIn);
    }

    @Override
    public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        /*
            Attempt to pick positions for a snowberry bush 16 times. Adds those positions to an ArrayList and remove duplicates. Then trim the list so there are only 2 to 5 elements. Then place the bushes and add snow around them.
         */
        boolean generatedSomething = false;
        ArrayList<BlockPos> positionsToGen = new ArrayList<>();
        BlockState blockstate = ModBlocks.snowberryBush.getDefaultState();
        for(int tries = 0; tries < 32; ++tries) {
            BlockPos blockpos = pos.add(rand.nextInt(4) - rand.nextInt(4), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(4) - rand.nextInt(4));
            if (worldIn.isAirBlock(blockpos) && (!worldIn.getDimension().isNether() || blockpos.getY() < worldIn.getWorld().getDimension().getHeight()) && blockstate.isValidPosition(worldIn, blockpos)) {
                positionsToGen.add(blockpos);
            }
        }

        int numberOfBushesInFeature = Math.min(positionsToGen.size(), rand.nextInt(3) + 2);
        for (int i = 0; i < numberOfBushesInFeature; i++){
            worldIn.setBlockState(positionsToGen.get(i), ModBlocks.snowberryBush.getDefaultState(), 2);
            worldIn.setBlockState(positionsToGen.get(i).down(), Blocks.SNOW_BLOCK.getDefaultState(), 2);
            generatedSomething = true;
        }
        for (int i = 0; i < numberOfBushesInFeature; i++){
            for (BlockPos adjacentPos : getAdjacentBlocks(positionsToGen.get(i))) {
                int layerLevel = rand.nextInt(8);
                if (worldIn.isAirBlock(adjacentPos) && layerLevel > 0 && Blocks.SNOW.getDefaultState().isValidPosition(worldIn, adjacentPos)){
                    worldIn.setBlockState(adjacentPos, Blocks.SNOW.getStateContainer().getBaseState().with(BlockStateProperties.LAYERS_1_8, layerLevel), 2);
                }
            }
        }
        return generatedSomething;
    }

    private List<BlockPos> getAdjacentBlocks(BlockPos blockpos){
        return new ArrayList<>(Arrays.asList(blockpos.east(), blockpos.west(), blockpos.north(), blockpos.south(), blockpos.north().east(), blockpos.north().west(), blockpos.south().east(), blockpos.south().west()));
    }
}
