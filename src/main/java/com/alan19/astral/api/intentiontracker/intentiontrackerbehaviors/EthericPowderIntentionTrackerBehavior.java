package com.alan19.astral.api.intentiontracker.intentiontrackerbehaviors;

import com.alan19.astral.blocks.EthericPowder;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;

public class EthericPowderIntentionTrackerBehavior implements IIntentionTrackerBehavior {
    @Override
    public void onIntentionBeamHit(PlayerEntity playerEntity, int beamLevel, BlockRayTraceResult result, BlockState blockState) {
        EthericPowder ethericPowder = (EthericPowder) blockState.getBlock();
        ethericPowder.use(blockState, playerEntity.getCommandSenderWorld(), result.getBlockPos(), playerEntity, Hand.MAIN_HAND, result);
    }
}
