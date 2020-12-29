package com.alan19.astral.world.features;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.Random;

public class StickyRandomPlacerFeature extends Feature<BlockClusterFeatureConfig> {
    public StickyRandomPlacerFeature(Codec<BlockClusterFeatureConfig> codec) {
        super(codec);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, BlockClusterFeatureConfig config) {
        BlockPos blockpos;
        if (config.field_227298_k_) {
            blockpos = reader.getHeight(Heightmap.Type.WORLD_SURFACE_WG, pos);
        }
        else {
            blockpos = pos;
        }

        int i = 0;
        BlockPos.Mutable mutablePos = new BlockPos.Mutable();

        for (int j = 0; j < config.tryCount; ++j) {
            mutablePos.setAndOffset(blockpos, rand.nextInt(config.xSpread + 1) - rand.nextInt(config.xSpread + 1), rand.nextInt(config.ySpread + 1) - rand.nextInt(config.ySpread + 1), rand.nextInt(config.zSpread + 1) - rand.nextInt(config.zSpread + 1));
            BlockPos downPos = mutablePos.down();
            BlockState downState = reader.getBlockState(downPos);
            BlockState blockstate = config.stateProvider.getBlockState(rand, pos);
            if ((reader.isAirBlock(mutablePos) || config.isReplaceable && reader.getBlockState(mutablePos).getMaterial().isReplaceable()) && blockstate.isValidPosition(reader, mutablePos) && Arrays.stream(Direction.values()).anyMatch(direction -> !reader.isAirBlock(mutablePos.offset(direction))) && (config.whitelist.isEmpty() || config.whitelist.contains(downState.getBlock())) && !config.blacklist.contains(downState) && (!config.requiresWater || reader.getFluidState(downPos.west()).isTagged(FluidTags.WATER) || reader.getFluidState(downPos.east()).isTagged(FluidTags.WATER) || reader.getFluidState(downPos.north()).isTagged(FluidTags.WATER) || reader.getFluidState(downPos.south()).isTagged(FluidTags.WATER))) {
                config.blockPlacer.place(reader, mutablePos, blockstate, rand);
                ++i;
            }
        }
        return i > 0;
    }
}
