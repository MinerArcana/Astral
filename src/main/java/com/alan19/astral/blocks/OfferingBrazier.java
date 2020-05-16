package com.alan19.astral.blocks;

import com.alan19.astral.blocks.tileentities.OfferingBrazierTileEntity;
import com.alan19.astral.particle.AstralParticles;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;
import java.util.stream.IntStream;

import static net.minecraft.block.AbstractFurnaceBlock.LIT;

public class OfferingBrazier extends Block {

    public OfferingBrazier() {
        super(Block.Properties
                .create(Material.ROCK)
                .hardnessAndResistance(3.5f)
                .lightValue(13)
                .notSolid()
        );
        setDefaultState(this.getStateContainer().getBaseState().with(LIT, false));
    }

    @Override
    public int getLightValue(BlockState state) {
        return state.getBlockState().get(LIT) ? 13 : 0;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder.add(LIT));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new OfferingBrazierTileEntity();
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
        super.onBlockPlacedBy(world, blockPos, blockState, livingEntity, itemStack);
        if (livingEntity instanceof PlayerEntity) {
            TileEntity tileEntity = world.getTileEntity(blockPos);
            if (tileEntity instanceof OfferingBrazierTileEntity) {
                ((OfferingBrazierTileEntity) tileEntity).setUUID(livingEntity.getUniqueID());
            }
        }
    }

    @Override
    public ActionResultType onBlockActivated(@Nonnull BlockState blockState, World world, @Nonnull BlockPos blockPos, @Nonnull PlayerEntity playerEntity, @Nonnull Hand hand, @Nonnull BlockRayTraceResult blockRayTraceResult) {
        TileEntity entity = world.getTileEntity(blockPos);
        if (entity instanceof OfferingBrazierTileEntity) {
            //If the block does not have a bound player, sets the bound player to the player
            if (!((OfferingBrazierTileEntity) entity).getBoundPlayer().isPresent()) {
                ((OfferingBrazierTileEntity) entity).setUUID(playerEntity.getUniqueID());
            }
            //Sneak click to bind a player, regular right click inserts/extracts
            if (!playerEntity.isCrouching()) {
                ((OfferingBrazierTileEntity) entity).extractInsertItem(playerEntity, hand);
                return ActionResultType.SUCCESS;
            }
            else if (playerEntity.isCrouching()) {
                ((OfferingBrazierTileEntity) entity).setUUID(playerEntity.getUniqueID());
                return ActionResultType.SUCCESS;
            }
        }
        return super.onBlockActivated(blockState, world, blockPos, playerEntity, hand, blockRayTraceResult);
    }

    @Override
    public void onReplaced(BlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof OfferingBrazierTileEntity) {
                tileentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(itemHandler -> IntStream.range(0, itemHandler.getSlots()).forEach(i -> Block.spawnAsEntity(worldIn, pos, itemHandler.getStackInSlot(i))));

                worldIn.updateComparatorOutputLevel(pos, this);
            }

            super.onReplaced(state, worldIn, pos, newState, isMoving);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull Random rand) {
        if (stateIn.get(LIT)) {
            double d0 = (double) pos.getX() + 0.5D;
            double d1 = pos.getY();
            double d2 = (double) pos.getZ() + 0.5D;
            if (rand.nextDouble() < 0.1D) {
                worldIn.playSound(d0, d1, d2, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            }

            final double x = pos.getX() + 0.125 + rand.nextDouble() * 0.75;
            final double y = pos.getY() + .5;
            final double z = pos.getZ() + +0.125 + rand.nextDouble() * 0.75;
            worldIn.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, 0.0D, 0.0D);
            worldIn.addParticle(AstralParticles.ETHEREAL_FLAME.get(), x, y, z, 0.0D, 0.0D, 0.0D);
        }
    }

}
