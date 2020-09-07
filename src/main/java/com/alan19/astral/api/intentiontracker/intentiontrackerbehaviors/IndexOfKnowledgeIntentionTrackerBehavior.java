package com.alan19.astral.api.intentiontracker.intentiontrackerbehaviors;

import com.alan19.astral.blocks.IndexOfKnowledge;
import com.alan19.astral.util.Constants;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockRayTraceResult;

public class IndexOfKnowledgeIntentionTrackerBehavior implements IIntentionTrackerBehavior {
    @Override
    public void onIntentionBeamHit(PlayerEntity playerEntity, int beamLevel, BlockRayTraceResult result, BlockState blockState) {
        IndexOfKnowledge indexOfKnowledge = (IndexOfKnowledge) blockState.getBlock();
        if (playerEntity.experienceLevel < Math.min(indexOfKnowledge.calculateLevel(playerEntity.getEntityWorld(), result.getPos()), blockState.get(Constants.LIBRARY_LEVEL))) {
            playerEntity.addExperienceLevel(1);
            playerEntity.experience = 0;
        }
    }
}
