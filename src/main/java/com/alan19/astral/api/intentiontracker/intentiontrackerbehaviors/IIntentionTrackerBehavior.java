package com.alan19.astral.api.intentiontracker.intentiontrackerbehaviors;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockRayTraceResult;

public interface IIntentionTrackerBehavior {
    void onIntentionBeamHit(PlayerEntity playerEntity, int beamLevel, BlockRayTraceResult result, BlockState blockState);
}
