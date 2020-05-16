package com.alan19.astral.blocks;

import com.alan19.astral.api.AstralAPI;
import com.alan19.astral.dimensions.AstralDimensions;
import com.alan19.astral.mentalconstructs.AstralMentalConstructs;
import com.alan19.astral.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;

public interface MentalConstructController {

    static int getComparatorInputOverride(BlockState blockState, int redstoneLevel, int defaultOverrideValue) {
        return blockState.get(Constants.TRACKED_CONSTRUCT) ? redstoneLevel : defaultOverrideValue;
    }

    int calculateLevel(World world, BlockPos pos);

    static void tick(BlockState state, @Nonnull ServerWorld worldIn, @Nonnull BlockPos pos, int level) {
        if (state.get(Constants.TRACKED_CONSTRUCT) && worldIn.getDimension().getType() == DimensionType.byName(AstralDimensions.INNER_REALM)) {
            AstralAPI.getConstructTracker(worldIn).ifPresent(tracker -> tracker.updateAllPlayers(AstralMentalConstructs.GARDEN.get(), worldIn, pos, level));
        }
    }

    /**
     * When the block is replaced, look into the mental construct tracker and check if this construct is enabling a mental construct bonus. If it is, reset the BlockPos of the PlayerTracker Triple and set the level of that mental construct bonus to -1. Alsop updates the comparator output level.
     *
     * @param worldIn The world the block is in
     * @param pos     The pos of the block
     * @param block   The instance of the block
     */
    static void onReplaced(@Nonnull World worldIn, @Nonnull BlockPos pos, Block block) {
        if (worldIn instanceof ServerWorld) {
            AstralAPI.getConstructTracker((ServerWorld) worldIn).ifPresent(tracker -> tracker.resetConstructEffect(AstralMentalConstructs.GARDEN.get(), worldIn, pos));
            worldIn.updateComparatorOutputLevel(pos, block);
        }
    }
}
