package com.alan19.astral.api.intentiontracker.intentiontrackerbehaviors;

import com.alan19.astral.util.ExperienceHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.SaplingBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockRayTraceResult;

public class EtherealSaplingIntentionTrackerBehavior implements IIntentionTrackerBehavior {
    @Override
    public void onIntentionBeamHit(PlayerEntity playerEntity, int beamLevel, BlockRayTraceResult result, BlockState blockState) {
        if (playerEntity instanceof ServerPlayerEntity && ExperienceHelper.getPlayerXP(playerEntity) >= 10) {
            playerEntity.experienceTotal -= 10;
            final ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) playerEntity;
            SaplingBlock block = (SaplingBlock) blockState.getBlock();
            block.grow(serverPlayerEntity.getServerWorld(), serverPlayerEntity.getServerWorld().getRandom(), result.getPos(), blockState);
        }
    }
}
