package com.alan19.astral.api.intentiontracker.intentiontrackerbehaviors;

import com.alan19.astral.blocks.IndexOfKnowledge;
import com.alan19.astral.util.Constants;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class IndexOfKnowledgeIntentionTrackerBehavior implements IIntentionTrackerBehavior {
    @Override
    public void onIntentionBeamHit(Player playerEntity, int beamLevel, BlockHitResult result, BlockState blockState) {
        IndexOfKnowledge indexOfKnowledge = (IndexOfKnowledge) blockState.getBlock();
        if (playerEntity.experienceLevel < Math.min(indexOfKnowledge.calculateLevel(playerEntity.getCommandSenderWorld(), result.getBlockPos()), blockState.getValue(Constants.LIBRARY_LEVEL))) {
            playerEntity.giveExperienceLevels(1);
            playerEntity.experienceProgress = 0;
        }
    }
}
