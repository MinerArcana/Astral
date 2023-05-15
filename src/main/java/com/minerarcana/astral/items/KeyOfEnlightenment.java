package com.minerarcana.astral.items;

import com.minerarcana.astral.api.AstralCapabilities;
import com.minerarcana.astral.api.innerrealmcubegen.InnerRealmUtils;
import com.minerarcana.astral.blocks.AstralBlocks;
import com.minerarcana.astral.blocks.AstralMeridian;
import com.minerarcana.astral.world.feature.dimensions.AstralDimensions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.NotNull;

public class KeyOfEnlightenment extends Item {
    public KeyOfEnlightenment() {
        super(new Item.Properties().tab(AstralItems.ASTRAL_ITEMS));
    }

    /**
     * When the key is used on an Astral Meridian block, query the chunk claim capability and attempt to claim the adjacent chunk.
     * Removes 1 key from the stack.
     *
     * @param pContext The context object when the key is used
     * @return CONSUME if the chunk is claimed and the key is consumed, FAIL if the chunk is unable to be claimed
     */
    @Override
    public @NotNull InteractionResult useOn(UseOnContext pContext) {
        if (pContext.getLevel() instanceof ServerLevel level && pContext.getPlayer() instanceof ServerPlayer player && level.dimension() == AstralDimensions.INNER_REALM) {
            ChunkPos meridianChunk = new ChunkPos(pContext.getClickedPos());
            if (level.getBlockState(pContext.getClickedPos()).getBlock() == AstralBlocks.ASTRAL_MERIDIAN.get()) {
                BlockState meridianBlockState = level.getBlockState(pContext.getClickedPos());
                int meridianDirection = meridianBlockState.getValue(AstralMeridian.DIRECTION);
                // Break the wall on the player's side, and let the capability break it on the other side since the other side may be claimed by another player
                InnerRealmUtils.destroyWall(level, meridianChunk, meridianDirection);
                LevelChunk chunkToGenerateBoxIn = InnerRealmUtils.getAdjacentChunk(pContext.getClickedPos(), meridianDirection, level);
                AstralCapabilities.getChunkClaimTracker(level).ifPresent(cap -> cap.claimChunk(player, chunkToGenerateBoxIn.getPos()));
                pContext.getItemInHand().setCount(pContext.getItemInHand().getCount() - 1);
                return InteractionResult.CONSUME;
            }
        }
        return super.useOn(pContext);
    }
}
