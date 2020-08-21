package com.alan19.astral.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockRayTraceResult;

public interface IntentionBlock {
    boolean onIntentionTrackerHit(PlayerEntity playerEntity, int beamLevel, BlockRayTraceResult result, BlockState blockState);
}
