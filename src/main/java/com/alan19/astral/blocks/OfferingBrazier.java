package com.alan19.astral.blocks;

import com.alan19.astral.blocks.tileentities.OfferingBrazierTileEntity;
import com.alan19.astral.particle.AstralParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;
import java.util.stream.IntStream;

import static net.minecraft.world.level.block.AbstractFurnaceBlock.LIT;

public class OfferingBrazier extends Block implements EntityBlock, SimpleWaterloggedBlock {

    protected static final VoxelShape BASE_SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 2.0D, 14.0D);
    protected static final VoxelShape COLUMN_SHAPE = Block.box(5.0D, 2.0D, 5.0D, 11.0D, 4.0D, 11.0D);
    protected static final VoxelShape TOP_SHAPE = Block.box(0, 4, 0, 15, 6, 15);
    protected static final VoxelShape NORTH = Block.box(0, 6, 0, 16, 8, 2);
    protected static final VoxelShape SOUTH = Block.box(0, 6, 14, 16, 8, 16);
    protected static final VoxelShape EAST = Block.box(14, 6, 0, 16, 8, 16);
    protected static final VoxelShape WEST = Block.box(0, 6, 0, 2, 8, 16);
    protected static final VoxelShape SHAPE = Shapes.or(BASE_SHAPE, COLUMN_SHAPE, TOP_SHAPE, NORTH, SOUTH, EAST, WEST);

    public OfferingBrazier() {
        super(Block.Properties
                .of(Material.STONE)
                .strength(3.5f)
                .lightLevel(value -> value.getValue(LIT) ? 13 : 0)
                .noOcclusion()
        );
        registerDefaultState(this.getStateDefinition().any().setValue(LIT, false));
    }

    @Nonnull
    @Override
    @ParametersAreNonnullByDefault
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(LIT));
    }


    @Override
    public void setPlacedBy(@Nonnull Level world, @Nonnull BlockPos blockPos, @Nonnull BlockState blockState, @Nullable LivingEntity livingEntity, @Nonnull ItemStack itemStack) {
        super.setPlacedBy(world, blockPos, blockState, livingEntity, itemStack);
        if (livingEntity instanceof Player) {
            BlockEntity tileEntity = world.getBlockEntity(blockPos);
            if (tileEntity instanceof OfferingBrazierTileEntity) {
                ((OfferingBrazierTileEntity) tileEntity).setUUID(livingEntity.getUUID());
            }
        }
    }

    @Nonnull
    @Override
    public InteractionResult use(@Nonnull BlockState blockState, Level world, @Nonnull BlockPos blockPos, @Nonnull Player playerEntity, @Nonnull InteractionHand hand, @Nonnull BlockHitResult blockRayTraceResult) {
        BlockEntity entity = world.getBlockEntity(blockPos);
        if (entity instanceof OfferingBrazierTileEntity) {
            //If the block does not have a bound player, sets the bound player to the player
            if (!((OfferingBrazierTileEntity) entity).getBoundPlayer().isPresent()) {
                ((OfferingBrazierTileEntity) entity).setUUID(playerEntity.getUUID());
            }
            //Sneak click to bind a player, regular right click inserts/extracts
            if (!playerEntity.isCrouching()) {
                ((OfferingBrazierTileEntity) entity).extractInsertItem(playerEntity, hand);
                return InteractionResult.SUCCESS;
            }
            else if (playerEntity.isCrouching()) {
                ((OfferingBrazierTileEntity) entity).setUUID(playerEntity.getUUID());
                return InteractionResult.SUCCESS;
            }
        }
        return super.use(blockState, world, blockPos, playerEntity, hand, blockRayTraceResult);
    }

    @Override
    public void onRemove(BlockState state, @Nonnull Level worldIn, @Nonnull BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity tileentity = worldIn.getBlockEntity(pos);
            if (tileentity instanceof OfferingBrazierTileEntity) {
                tileentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(itemHandler -> IntStream.range(0, itemHandler.getSlots()).forEach(i -> Block.popResource(worldIn, pos, itemHandler.getStackInSlot(i))));

                worldIn.updateNeighbourForOutputSignal(pos, this);
            }

            super.onRemove(state, worldIn, pos, newState, isMoving);
        }
    }

    @Override
    public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
        if (!entityIn.fireImmune() && state.getValue(LIT) && entityIn instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity) entityIn)) {
            entityIn.hurt(DamageSource.IN_FIRE, 1.0F);
        }

        super.entityInside(state, worldIn, pos, entityIn);

    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, @Nonnull Level worldIn, @Nonnull BlockPos pos, @Nonnull Random rand) {
        if (stateIn.getValue(LIT)) {
            double d0 = (double) pos.getX() + 0.5D;
            double d1 = pos.getY();
            double d2 = (double) pos.getZ() + 0.5D;
            if (rand.nextDouble() < 0.1D) {
                worldIn.playLocalSound(d0, d1, d2, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
            }
            BlockEntity tileEntity = worldIn.getBlockEntity(pos);
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
    public boolean hasAnalogOutputSignal(@Nonnull BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(@Nonnull BlockState blockState, Level worldIn, @Nonnull BlockPos pos) {
        final BlockEntity tileEntity = worldIn.getBlockEntity(pos);
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
            return Mth.floor(f * 14.0F) + (i > 0 ? 1 : 0);
        }
        return 0;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new OfferingBrazierTileEntity(pPos, pState);
    }
}
