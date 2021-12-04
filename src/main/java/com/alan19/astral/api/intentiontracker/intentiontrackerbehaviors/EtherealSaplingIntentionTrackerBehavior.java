package com.alan19.astral.api.intentiontracker.intentiontrackerbehaviors;

import com.alan19.astral.util.ExperienceHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class EtherealSaplingIntentionTrackerBehavior implements IIntentionTrackerBehavior {
    @Override
    public void onIntentionBeamHit(Player playerEntity, int beamLevel, BlockHitResult result, BlockState blockState) {
        if (playerEntity instanceof ServerPlayer && ExperienceHelper.getPlayerXP(playerEntity) >= 10) {
            playerEntity.totalExperience -= 10;
            final ServerPlayer serverPlayerEntity = (ServerPlayer) playerEntity;
            SaplingBlock block = (SaplingBlock) blockState.getBlock();
            block.performBonemeal(serverPlayerEntity.getLevel(), serverPlayerEntity.getLevel().getRandom(), result.getBlockPos(), blockState);
        }
    }
}
