package com.alan19.astral.blocks;

import com.alan19.astral.api.AstralAPI;
import com.alan19.astral.dimensions.AstralDimensions;
import com.alan19.astral.mentalconstructs.MentalConstructType;
import com.alan19.astral.util.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

public interface MentalConstructController {

    static int getComparatorInputOverride(BlockState blockState, int redstoneLevel, int defaultOverrideValue) {
        return blockState.getValue(Constants.TRACKED_CONSTRUCT) ? redstoneLevel : defaultOverrideValue;
    }

    int calculateLevel(Level world, BlockPos pos);

    static void tick(BlockState state, @Nonnull ServerLevel worldIn, @Nonnull BlockPos pos, int level, MentalConstructType type) {
        if (state.getValue(Constants.TRACKED_CONSTRUCT) && worldIn.dimension() == AstralDimensions.INNER_REALM) {
            AstralAPI.getConstructTracker(worldIn).ifPresent(tracker -> tracker.updateAllPlayers(type, worldIn, pos, level));
        }
    }

    /**
     * When the block is replaced, look into the mental construct tracker and check if this construct is enabling a mental construct bonus. If it is, reset the BlockPos of the PlayerTracker Triple and set the level of that mental construct bonus to -1. Alsop updates the comparator output level.
     *
     * @param worldIn The world the block is in
     * @param pos     The pos of the block
     * @param block   The instance of the block
     */
    static void onReplaced(@Nonnull Level worldIn, @Nonnull BlockPos pos, Block block, MentalConstructType type) {
        if (worldIn instanceof ServerLevel) {
            AstralAPI.getConstructTracker((ServerLevel) worldIn).ifPresent(tracker -> tracker.resetConstructEffect(type, worldIn, pos));
            worldIn.updateNeighbourForOutputSignal(pos, block);
        }
    }
}
