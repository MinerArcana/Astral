package com.alan19.astral.world.features;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.Random;

public class StickyRandomPlacerFeature extends Feature<RandomPatchConfiguration> {
    public StickyRandomPlacerFeature(Codec<RandomPatchConfiguration> codec) {
        super(codec);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean place(WorldGenLevel reader, ChunkGenerator generator, Random rand, BlockPos pos, RandomPatchConfiguration config) {
        BlockPos blockpos;
        if (config.project) {
            blockpos = reader.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, pos);
        }
        else {
            blockpos = pos;
        }

        int i = 0;
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        for (int j = 0; j < config.tries; ++j) {
            mutablePos.setWithOffset(blockpos, rand.nextInt(config.xspread + 1) - rand.nextInt(config.xspread + 1), rand.nextInt(config.yspread + 1) - rand.nextInt(config.yspread + 1), rand.nextInt(config.zspread + 1) - rand.nextInt(config.zspread + 1));
            BlockPos downPos = mutablePos.below();
            BlockState downState = reader.getBlockState(downPos);
            BlockState blockstate = config.stateProvider.getState(rand, pos);
            if ((reader.isEmptyBlock(mutablePos) || config.canReplace && reader.getBlockState(mutablePos).getMaterial().isReplaceable()) && blockstate.canSurvive(reader, mutablePos) && Arrays.stream(Direction.values()).anyMatch(direction -> !reader.isEmptyBlock(mutablePos.relative(direction))) && (config.whitelist.isEmpty() || config.whitelist.contains(downState.getBlock())) && !config.blacklist.contains(downState) && (!config.needWater || reader.getFluidState(downPos.west()).is(FluidTags.WATER) || reader.getFluidState(downPos.east()).is(FluidTags.WATER) || reader.getFluidState(downPos.north()).is(FluidTags.WATER) || reader.getFluidState(downPos.south()).is(FluidTags.WATER))) {
                config.blockPlacer.place(reader, mutablePos, blockstate, rand);
                ++i;
            }
        }
        return i > 0;
    }
}
