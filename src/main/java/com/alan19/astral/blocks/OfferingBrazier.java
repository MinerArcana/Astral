package com.alan19.astral.blocks;

import com.alan19.astral.blocks.tileentities.OfferingBrazierTileEntity;
import com.alan19.astral.particle.AstralParticles;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;
import java.util.stream.IntStream;

import static net.minecraft.block.AbstractFurnaceBlock.LIT;

@SuppressWarnings("deprecated")
public class OfferingBrazier extends Block {

    protected static final VoxelShape BASE_SHAPE = Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 2.0D, 14.0D);
    protected static final VoxelShape COLUMN_SHAPE = Block.makeCuboidShape(5.0D, 2.0D, 5.0D, 11.0D, 4.0D, 10.0D);
    protected static final VoxelShape TOP_SHAPE = Block.makeCuboidShape(0, 4, 0, 15, 6, 15);
    protected static final VoxelShape NORTH = Block.makeCuboidShape(0, 6, 0, 16, 8, 2);
    protected static final VoxelShape SOUTH = Block.makeCuboidShape(0, 6, 14, 16, 8, 16);
    protected static final VoxelShape EAST = Block.makeCuboidShape(14, 6, 0, 16, 8, 16);
    protected static final VoxelShape WEST = Block.makeCuboidShape(0, 6, 0, 2, 8, 16);
    protected static final VoxelShape SHAPE = VoxelShapes.or(BASE_SHAPE, COLUMN_SHAPE, TOP_SHAPE, NORTH, SOUTH, EAST, WEST);

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
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
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
    public void onBlockPlacedBy(@Nonnull World world, @Nonnull BlockPos blockPos, @Nonnull BlockState blockState, @Nullable LivingEntity livingEntity, @Nonnull ItemStack itemStack) {
        super.onBlockPlacedBy(world, blockPos, blockState, livingEntity, itemStack);
        if (livingEntity instanceof PlayerEntity) {
            TileEntity tileEntity = world.getTileEntity(blockPos);
            if (tileEntity instanceof OfferingBrazierTileEntity) {
                ((OfferingBrazierTileEntity) tileEntity).setUUID(livingEntity.getUniqueID());
            }
        }
    }

    @Nonnull
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
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        if (!entityIn.isImmuneToFire() && state.get(LIT) && entityIn instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity) entityIn)) {
            entityIn.attackEntityFrom(DamageSource.IN_FIRE, 1.0F);
        }

        super.onEntityCollision(state, worldIn, pos, entityIn);

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
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            boolean lastItem = false;
            if (tileEntity instanceof OfferingBrazierTileEntity) {
                final LazyOptional<IItemHandler> capability = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
                final IItemHandler iItemHandler = capability.orElseGet(ItemStackHandler::new);
                lastItem = iItemHandler.getStackInSlot(0).isEmpty();
            }
            for (int i = 0; i < (lastItem ? 4 : 2); i++) {
                final double x = pos.getX() + 0.125 + rand.nextDouble() * 0.75;
                final double y = pos.getY() + .5;
                final double z = pos.getZ() + +0.125 + rand.nextDouble() * 0.75;
                worldIn.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, 0.0D, 0.0D);
                worldIn.addParticle(AstralParticles.ETHEREAL_FLAME.get(), x, y, z, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    public boolean hasComparatorInputOverride(@Nonnull BlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(@Nonnull BlockState blockState, World worldIn, @Nonnull BlockPos pos) {
        final TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof OfferingBrazierTileEntity) {
            final IItemHandler brazierInventory = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElseGet(ItemStackHandler::new);
            int i = 0;
            float f = 0.0F;

            for (int j = 0; j < brazierInventory.getSlots(); ++j) {
                ItemStack itemstack = brazierInventory.getStackInSlot(j);
                if (!itemstack.isEmpty()) {
                    f += (float) itemstack.getCount() / (float) Math.min(brazierInventory.getSlotLimit(j), itemstack.getMaxStackSize());
                    i++;
                }
            }

            f /= (float) brazierInventory.getSlots();
            return MathHelper.floor(f * 14.0F) + (i > 0 ? 1 : 0);
        }
        return 0;
    }
}
