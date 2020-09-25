package com.alan19.astral.items;

import com.alan19.astral.api.AstralAPI;
import com.alan19.astral.blocks.AstralBlocks;
import com.alan19.astral.blocks.etherealblocks.AstralMeridian;
import com.alan19.astral.dimensions.AstralDimensions;
import com.alan19.astral.dimensions.innerrealm.InnerRealmUtils;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;

public class EnlightenmentKey extends Item {
    public EnlightenmentKey() {
        super(new Item.Properties().group(AstralItems.ASTRAL_ITEMS));
    }

    @Override
    @Nonnull
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        if (world.dimension.getType().equals(DimensionType.byName(AstralDimensions.INNER_REALM)) && world instanceof ServerWorld) {
            IChunk meridianChunk = world.getChunk(context.getPos());
            if (world.getBlockState(context.getPos()).getBlock() == AstralBlocks.ASTRAL_MERIDIAN.get()) {
                BlockState meridianBlockState = world.getBlockState(context.getPos()).getBlockState();
                int meridianDirection = meridianBlockState.get(AstralMeridian.DIRECTION);
                InnerRealmUtils innerRealmUtils = new InnerRealmUtils();
                innerRealmUtils.destroyWall(world, meridianChunk, meridianDirection);
                Chunk chunkToGenerateBoxIn = innerRealmUtils.getAdjacentChunk(context.getPos(), meridianDirection, world);
                AstralAPI.getChunkClaim((ServerWorld) world).ifPresent(chunkClaim -> chunkClaim.handleChunkClaim(context.getPlayer(), chunkToGenerateBoxIn));
                innerRealmUtils.destroyWall(world, chunkToGenerateBoxIn, (meridianDirection + 2) % 4);
                context.getItem().setCount(context.getItem().getCount() - 1);
                // TODO Send block break to client
            }
        }
        return super.onItemUse(context);
    }
}
