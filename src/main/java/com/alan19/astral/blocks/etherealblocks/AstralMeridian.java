package com.alan19.astral.blocks.etherealblocks;

import com.alan19.astral.api.AstralAPI;
import com.alan19.astral.blocks.AstralBlocks;
import com.alan19.astral.dimensions.innerrealm.InnerRealmUtils;
import com.alan19.astral.effects.AstralEffects;
import com.alan19.astral.items.AstralItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class AstralMeridian extends Block implements Ethereal {

    /**
     * Direction IntegerProperty:
     * 0 = North
     * 1 = East
     * 2 = South
     * 3 = West
     * <p>
     * Each Astral Meridian have a cardinal direction associated with it when it is right clicked with a Key of
     * Enlightenment for the direction to break blocks in
     */
    public static final IntegerProperty DIRECTION = IntegerProperty.create("direction", 0, 3);

    public AstralMeridian() {
        super(Properties.create(Material.PORTAL)
                .hardnessAndResistance(-1.0F, 3600000.0F)
                .noDrops()
                .lightValue(14));

        this.setDefaultState(this.getStateContainer().getBaseState().with(DIRECTION, 0));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder.add(DIRECTION));
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        worldIn.setBlockState(pos, AstralBlocks.EGO_MEMBRANE.get().getDefaultState(), 2);
    }

    @Nonnull
    @Override
    @ParametersAreNonnullByDefault
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn instanceof ServerWorld) {
            if (player.getHeldItem(Hand.MAIN_HAND).isEmpty() && player.getHeldItem(Hand.OFF_HAND).isEmpty()) {
                player.removePotionEffect(AstralEffects.ASTRAL_TRAVEL.get());
                return ActionResultType.SUCCESS;
            }
            else {
                final ItemStack heldItem = player.getHeldItem(handIn);
                if (heldItem.getItem() == AstralItems.ENLIGHTENMENT_KEY.get()) {
                    IChunk meridianChunk = worldIn.getChunk(pos);
                    int meridianDirection = state.get(AstralMeridian.DIRECTION);
                    Chunk chunkToGenerateBoxIn = InnerRealmUtils.getAdjacentChunk(pos, meridianDirection, worldIn);
                    AstralAPI.getChunkClaim((ServerWorld) worldIn).ifPresent(chunkClaim -> chunkClaim.handleChunkClaim(player, chunkToGenerateBoxIn));
                    InnerRealmUtils.getFace(meridianChunk, meridianDirection, worldIn).forEach(blockPos -> worldIn.destroyBlock(blockPos, false));
                    InnerRealmUtils.destroyWall(worldIn, meridianChunk, meridianDirection);
                    heldItem.setCount(heldItem.getCount() - 1);
                }
            }
        }
        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }

    @Override
    public PushReaction getPushReaction(BlockState state) {
        return PushReaction.IGNORE;
    }
}
