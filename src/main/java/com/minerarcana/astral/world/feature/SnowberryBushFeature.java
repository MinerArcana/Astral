package com.minerarcana.astral.world.feature;

import com.minerarcana.astral.blocks.AstralBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SnowberryBushFeature extends Feature<SnowberryPatchConfig> {
    public SnowberryBushFeature() {
        super(SnowberryPatchConfig.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<SnowberryPatchConfig> pContext) {
        SnowberryPatchConfig snowberryPatchConfig = pContext.config();
        RandomSource randomsource = pContext.random();
        BlockPos blockpos = pContext.origin();
        WorldGenLevel worldgenlevel = pContext.level();
        int generatedCount = 0;
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        int j = snowberryPatchConfig.xzSpread() + 1;
        int k = snowberryPatchConfig.ySpread() + 1;

        int generated = 0;
        int tryCount = 0;
        while (tryCount < snowberryPatchConfig.tries() && generated < snowberryPatchConfig.maxCount()) {
            pos.setWithOffset(blockpos, randomsource.nextInt(j) - randomsource.nextInt(j), randomsource.nextInt(k) - randomsource.nextInt(k), randomsource.nextInt(j) - randomsource.nextInt(j));
            if (AstralBlocks.SNOWBERRY_BUSH.get().canSurvive(worldgenlevel.getBlockState(pos), worldgenlevel, pos) && spawnSnowberries(worldgenlevel, randomsource, generated, pos) > 0) {
                ++generatedCount;
            }
            ++tryCount;
        }

        return generatedCount > 0;
    }

    private List<BlockPos> getAdjacentBlocks(BlockPos blockpos) {
        return new ArrayList<>(Arrays.asList(blockpos.east(), blockpos.west(), blockpos.north(), blockpos.south(), blockpos.north().east(), blockpos.north().west(), blockpos.south().east(), blockpos.south().west()));
    }

    private int spawnSnowberries(@Nonnull LevelAccessor worldIn, @Nonnull RandomSource rand, int spawned, BlockPos generatingPos) {
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
