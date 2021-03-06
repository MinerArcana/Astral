package com.alan19.astral.items;

import com.alan19.astral.api.AstralAPI;
import com.alan19.astral.blocks.AstralBlocks;
import com.alan19.astral.blocks.etherealblocks.AstralMeridian;
import com.alan19.astral.dimensions.AstralDimensions;
import com.alan19.astral.dimensions.innerrealm.InnerRealmUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;

public class EnlightenmentKey extends Item {
    public EnlightenmentKey() {
        super(new Item.Properties().group(AstralItems.ASTRAL_ITEMS));
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
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        if (world.getDimensionKey() == AstralDimensions.INNER_REALM && world instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) world;
            ChunkPos meridianChunk = new ChunkPos(context.getPos());
            if (world.getBlockState(context.getPos()).getBlock() == AstralBlocks.ASTRAL_MERIDIAN.get()) {
                BlockState meridianBlockState = world.getBlockState(context.getPos()).getBlockState();
                int meridianDirection = meridianBlockState.get(AstralMeridian.DIRECTION);
                // Break the wall on the player's side, and let the capability break it on the other side since the other side may be claimed by another player
                InnerRealmUtils.destroyWall(serverWorld, meridianChunk, meridianDirection);
                Chunk chunkToGenerateBoxIn = InnerRealmUtils.getAdjacentChunk(context.getPos(), meridianDirection, world);
                AstralAPI.getChunkClaimTracker(serverWorld).ifPresent(cap -> cap.claimChunk((ServerPlayerEntity) context.getPlayer(), chunkToGenerateBoxIn.getPos()));
                context.getItem().setCount(context.getItem().getCount() - 1);
                return ActionResultType.CONSUME;
            }
        }
        return super.onItemUse(context);
    }
}
