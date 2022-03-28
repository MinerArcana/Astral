package com.alan19.astral.items;

import com.alan19.astral.api.AstralAPI;
import com.alan19.astral.blocks.AstralBlocks;
import com.alan19.astral.blocks.etherealblocks.AstralMeridian;
import com.alan19.astral.dimensions.AstralDimensions;
import com.alan19.astral.dimensions.innerrealm.InnerRealmUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;

import javax.annotation.Nonnull;

public class EnlightenmentKey extends Item {
    public EnlightenmentKey() {
        super(new Item.Properties().tab(AstralItems.ASTRAL_ITEMS));
    }

    /**
     * When the key is used on an Astral Meridian block, query the chunk claim capability and attempt to claim the adjacent chunk.
     * Removes 1 key from the stack.
     *
     * @param context The context object when the key is used
     * @return CONSUME if the chunk is claimed and the key is consumed, FAIL if the chunk is unable to be claimed
     */
    @Override
    @Nonnull
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        if (world.dimension() == AstralDimensions.INNER_REALM && world instanceof ServerLevel) {
            ServerLevel serverWorld = (ServerLevel) world;
            ChunkPos meridianChunk = new ChunkPos(context.getClickedPos());
            if (world.getBlockState(context.getClickedPos()).getBlock() == AstralBlocks.ASTRAL_MERIDIAN.get()) {
                BlockState meridianBlockState = world.getBlockState(context.getClickedPos()).getBlockState();
                int meridianDirection = meridianBlockState.getValue(AstralMeridian.DIRECTION);
                // Break the wall on the player's side, and let the capability break it on the other side since the other side may be claimed by another player
                InnerRealmUtils.destroyWall(serverWorld, meridianChunk, meridianDirection);
                LevelChunk chunkToGenerateBoxIn = InnerRealmUtils.getAdjacentChunk(context.getClickedPos(), meridianDirection, world);
                AstralAPI.getChunkClaimTracker(serverWorld).ifPresent(cap -> cap.claimChunk((ServerPlayer) context.getPlayer(), chunkToGenerateBoxIn.getPos()));
                context.getItemInHand().setCount(context.getItemInHand().getCount() - 1);
                return InteractionResult.CONSUME;
            }
        }
        return super.useOn(context);
    }
}
