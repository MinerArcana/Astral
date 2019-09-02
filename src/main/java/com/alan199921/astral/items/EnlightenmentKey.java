package com.alan199921.astral.items;

import com.alan199921.astral.Astral;
import com.alan199921.astral.blocks.AstralMeridian;
import com.alan199921.astral.blocks.ModBlocks;
import com.alan199921.astral.capabilities.inner_realm_chunk_claim.InnerRealmChunkClaimProvider;
import com.alan199921.astral.dimensions.innerrealm.InnerRealmUtils;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunk;
import org.checkerframework.checker.nullness.qual.NonNull;

public class EnlightenmentKey extends Item {
    public EnlightenmentKey() {
        super(new Item.Properties().group(Astral.setup.astralItems));
        setRegistryName("enlightenment_key");
    }

    @Override
    @NonNull
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        IChunk meridianChunk = world.getChunk(context.getPos());
        if (world.getBlockState(context.getPos()).getBlock() == ModBlocks.astralMeridian) {
            BlockState meridianBlockState = world.getBlockState(context.getPos()).getBlockState();
            int meridianDirection = meridianBlockState.get(AstralMeridian.DIRECTION);
            InnerRealmUtils innerRealmUtils = new InnerRealmUtils();
            innerRealmUtils.destroyPlaneMeridian(world, meridianChunk, meridianDirection);
            IChunk chunkToGenerateBoxIn = innerRealmUtils.getAdjacentChunk(context.getPos(), meridianDirection, world);
            context.getWorld().getCapability(InnerRealmChunkClaimProvider.CHUNK_CLAIM_CAPABILITY).ifPresent(cap -> cap.handleChunkClaim(context.getPlayer(), chunkToGenerateBoxIn));
            innerRealmUtils.destroyPlaneMeridian(world, chunkToGenerateBoxIn, (meridianDirection + 2) % 4);
        }
        return super.onItemUse(context);
    }




}
