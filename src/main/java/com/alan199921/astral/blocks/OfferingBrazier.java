package com.alan199921.astral.blocks;

import com.alan199921.astral.blocks.tileentities.OfferingBrazierTile;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.stream.IntStream;

public class OfferingBrazier extends Block {

    public OfferingBrazier() {
        super(Block.Properties
                .create(Material.ROCK)
                .hardnessAndResistance(3.5f)
                .lightValue(13)
                .notSolid()
        );
        setDefaultState(this.getStateContainer().getBaseState().with(AbstractFurnaceBlock.LIT, false));
    }

    @Override
    public int getLightValue(BlockState state) {
        return state.getBlockState().get(AbstractFurnaceBlock.LIT) ? 13 : 0;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder.add(AbstractFurnaceBlock.LIT));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new OfferingBrazierTile();
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
        super.onBlockPlacedBy(world, blockPos, blockState, livingEntity, itemStack);
        if (livingEntity instanceof PlayerEntity) {
            TileEntity tileEntity = world.getTileEntity(blockPos);
            if (tileEntity instanceof OfferingBrazierTile) {
                ((OfferingBrazierTile) tileEntity).setUUID(livingEntity.getUniqueID());
            }
        }
    }

    @Override
    public ActionResultType onBlockActivated(@Nonnull BlockState blockState, World world, @Nonnull BlockPos blockPos, @Nonnull PlayerEntity playerEntity, @Nonnull Hand hand, @Nonnull BlockRayTraceResult blockRayTraceResult) {
        TileEntity entity = world.getTileEntity(blockPos);
        if (entity instanceof OfferingBrazierTile) {
            //If the block does not have a bound player, sets the bound player to the player
            if (!((OfferingBrazierTile) entity).getBoundPlayer().isPresent()) {
                ((OfferingBrazierTile) entity).setUUID(playerEntity.getUniqueID());
            }
            //Sneak click to bind a player, regular right click inserts/extracts
            if (!playerEntity.isCrouching()) {
                ((OfferingBrazierTile) entity).extractInsertItem(playerEntity, hand);
                return ActionResultType.CONSUME;
            }
            else if (playerEntity.isCrouching()) {
                ((OfferingBrazierTile) entity).setUUID(playerEntity.getUniqueID());
                return ActionResultType.PASS;
            }
        }
        return super.onBlockActivated(blockState, world, blockPos, playerEntity, hand, blockRayTraceResult);
    }

    @Override
    public void onReplaced(BlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof OfferingBrazierTile) {
                tileentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(itemHandler -> IntStream.range(0, itemHandler.getSlots()).forEach(i -> Block.spawnAsEntity(worldIn, pos, itemHandler.getStackInSlot(i))));

                worldIn.updateComparatorOutputLevel(pos, this);
            }

            super.onReplaced(state, worldIn, pos, newState, isMoving);
        }
    }
}
