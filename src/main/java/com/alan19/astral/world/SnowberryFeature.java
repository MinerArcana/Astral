package com.alan19.astral.world;

import com.alan19.astral.blocks.AstralBlocks;
import com.mojang.datafixers.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class SnowberryFeature extends Feature<SnowberryFeatureConfig> {

    public SnowberryFeature(Function<Dynamic<?>, ? extends SnowberryFeatureConfig> configFactoryIn) {
        super(configFactoryIn);
    }

    private List<BlockPos> getAdjacentBlocks(BlockPos blockpos) {
        return new ArrayList<>(Arrays.asList(blockpos.east(), blockpos.west(), blockpos.north(), blockpos.south(), blockpos.north().east(), blockpos.north().west(), blockpos.south().east(), blockpos.south().west()));
    }

    @Override
    public boolean place(@Nonnull IWorld worldIn, @Nonnull ChunkGenerator<? extends GenerationSettings> generator, @Nonnull Random rand, @Nonnull BlockPos pos, SnowberryFeatureConfig config) {
        /*
            Attempt to pick positions for a snowberry bush 16 times. Adds those positions to an ArrayList and remove duplicates. Then trim the list so there are only 2 to 5 elements. Then place the bushes and add snow around them.
         */
        boolean generated = false;
        if (rand.nextInt(config.getPatchChance()) == 0) {
            int spawned = 0;
            final int numberOfPlants = rand.nextInt(config.getMaxPatchSize() - config.getMinPatchSize()) + config.getMinPatchSize();
            //Choose random spot in chunk as the center for generating Snowberry bushes
            int centerX = pos.getX() + rand.nextInt(16);
            int centerZ = pos.getZ() + rand.nextInt(16);

            BlockState snowberries = AstralBlocks.SNOWBERRY_BUSH.get().getDefaultState();
            for (int tries = 0; tries < 40 && spawned < numberOfPlants; tries++) {
                int dist = (int) Math.ceil(Math.sqrt(config.getMaxPatchSize())) / 2 + 1;
                int x = centerX + rand.nextInt(dist * 2) - dist;
                int z = centerZ + rand.nextInt(dist * 2) - dist;
                int y = worldIn.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, x, z);
                BlockPos generatingPos = new BlockPos(x, y, z);
                if (worldIn.isAirBlock(generatingPos) && (!worldIn.getDimension().isNether() || generatingPos.getY() < worldIn.getWorld().getDimension().getHeight()) && snowberries.isValidPosition(worldIn, generatingPos)) {
                    worldIn.setBlockState(generatingPos.down(), Blocks.SNOW_BLOCK.getDefaultState(), 2);
                    worldIn.setBlockState(generatingPos, AstralBlocks.SNOWBERRY_BUSH.get().getDefaultState(), 2);
                    spawned++;
                    for (BlockPos adjacentPos : getAdjacentBlocks(generatingPos)) {
                        int layerLevel = rand.nextInt(4);
                        if (worldIn.isAirBlock(adjacentPos) && layerLevel > 0 && Blocks.SNOW.getDefaultState().isValidPosition(worldIn, adjacentPos)) {
                            worldIn.setBlockState(adjacentPos, Blocks.SNOW.getStateContainer().getBaseState().with(BlockStateProperties.LAYERS_1_8, layerLevel), 2);
                        }
                    }
                    generated = true;
                }
                else if ((!worldIn.getDimension().isNether() || (generatingPos.getY() < worldIn.getWorld().getDimension().getHeight())) && (snowberries.isValidPosition(worldIn, generatingPos.down()) || worldIn.getBlockState(pos).getBlock().equals(Blocks.SNOW))) {
                    worldIn.setBlockState(generatingPos.down(), Blocks.SNOW_BLOCK.getDefaultState(), 2);
                    worldIn.setBlockState(generatingPos, AstralBlocks.SNOWBERRY_BUSH.get().getDefaultState(), 2);
                    spawned++;
                    for (BlockPos adjacentPos : getAdjacentBlocks(generatingPos)) {
                        int layerLevel = rand.nextInt(4);
                        if (worldIn.isAirBlock(adjacentPos) && layerLevel > 0 && Blocks.SNOW.getDefaultState().isValidPosition(worldIn, adjacentPos)) {
                            worldIn.setBlockState(adjacentPos, Blocks.SNOW.getStateContainer().getBaseState().with(BlockStateProperties.LAYERS_1_8, layerLevel), 2);
                        }
                    }
                    generated = true;
                }
            }
        }
        return generated;
    }
}
