package com.alan19.astral.api.intentiontracker.intentiontrackerbehaviors;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public interface IIntentionTrackerBehavior {
    void onIntentionBeamHit(Player playerEntity, int beamLevel, BlockHitResult result, BlockState blockState);
}
