package com.alan19.astral.api.intentiontracker.intentiontrackerbehaviors;

import com.alan19.astral.blocks.EthericPowder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class EthericPowderIntentionTrackerBehavior implements IIntentionTrackerBehavior {
    @Override
    public void onIntentionBeamHit(Player playerEntity, int beamLevel, BlockHitResult result, BlockState blockState) {
        EthericPowder ethericPowder = (EthericPowder) blockState.getBlock();
        ethericPowder.use(blockState, playerEntity.getCommandSenderWorld(), result.getBlockPos(), playerEntity, InteractionHand.MAIN_HAND, result);
    }
}
