package com.alan19.astral.world.features;

import com.alan19.astral.blocks.AstralBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SnowberryFeature extends Feature<SnowberryFeatureConfig> {

    public SnowberryFeature() {
        super(SnowberryFeatureConfig.CODEC);
    }

    private List<BlockPos> getAdjacentBlocks(BlockPos blockpos) {
        return new ArrayList<>(Arrays.asList(blockpos.east(), blockpos.west(), blockpos.north(), blockpos.south(), blockpos.north().east(), blockpos.north().west(), blockpos.south().east(), blockpos.south().west()));
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean place(WorldGenLevel worldIn, ChunkGenerator generator, Random rand, BlockPos pos, SnowberryFeatureConfig config) {
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

            BlockState snowberries = AstralBlocks.SNOWBERRY_BUSH.get().defaultBlockState();
            for (int tries = 0; tries < 40 && spawned < numberOfPlants; tries++) {
                int dist = (int) Math.ceil(Math.sqrt(config.getMaxPatchSize())) / 2 + 1;
                int x = centerX + rand.nextInt(dist * 2) - dist;
                int z = centerZ + rand.nextInt(dist * 2) - dist;
                int y = worldIn.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
                BlockPos generatingPos = new BlockPos(x, y, z);
                if (worldIn.isEmptyBlock(generatingPos) && (worldIn.getLevel().dimension() != Level.NETHER || generatingPos.getY() < worldIn.getLevel().getMaxBuildHeight()) && snowberries.canSurvive(worldIn, generatingPos)) {
                    spawned = spawnSnowberries(worldIn, rand, spawned, generatingPos);
                    generated = true;
                }
                else if ((worldIn.getLevel().dimension() != Level.NETHER || (generatingPos.getY() < worldIn.getLevel().getMaxBuildHeight())) && (snowberries.canSurvive(worldIn, generatingPos.below()) || worldIn.getBlockState(pos).getBlock().equals(Blocks.SNOW))) {
                    spawned = spawnSnowberries(worldIn, rand, spawned, generatingPos);
                    generated = true;
                }
            }
        }
        return generated;
    }

    private int spawnSnowberries(@Nonnull LevelAccessor worldIn, @Nonnull Random rand, int spawned, BlockPos generatingPos) {
        worldIn.setBlock(generatingPos.below(), Blocks.SNOW_BLOCK.defaultBlockState(), 2);
        worldIn.setBlock(generatingPos, AstralBlocks.SNOWBERRY_BUSH.get().defaultBlockState(), 2);
        spawned++;
        for (BlockPos adjacentPos : getAdjacentBlocks(generatingPos)) {
            int layerLevel = rand.nextInt(4);
            if (worldIn.isEmptyBlock(adjacentPos) && layerLevel > 0 && Blocks.SNOW.defaultBlockState().canSurvive(worldIn, adjacentPos)) {
                worldIn.setBlock(adjacentPos, Blocks.SNOW.getStateDefinition().any().setValue(BlockStateProperties.LAYERS, layerLevel), 2);
            }
        }
        return spawned;
    }
}
