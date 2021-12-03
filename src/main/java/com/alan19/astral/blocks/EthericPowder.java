package com.alan19.astral.blocks;

import com.alan19.astral.blocks.etherealblocks.Ethereal;
import com.alan19.astral.blocks.etherealblocks.EtherealBlock;
import com.alan19.astral.events.astraltravel.TravelEffects;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TripWireHookBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Random;

import static net.minecraft.block.TripWireBlock.POWERED;

import net.minecraft.block.AbstractBlock.Properties;

public class EthericPowder extends EtherealBlock {
    private static final VoxelShape BASE_SHAPE = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 1.0D, 13.0D);

    protected EthericPowder() {
        super(Properties.of(Material.DECORATION)
                .noCollission()
                .strength(0));
        registerDefaultState(getStateDefinition().any().setValue(POWERED, false));
    }

    @OnlyIn(Dist.CLIENT)
    @Nonnull
    @Override
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull IBlockReader worldIn, @Nonnull BlockPos pos, @Nonnull ISelectionContext context) {
        return Ethereal.getShape(BASE_SHAPE);
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }

    @ParametersAreNonnullByDefault
    @Override
    public void entityInside(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        if (worldIn instanceof ServerWorld && !state.getValue(POWERED)) {
            this.updateState(worldIn, pos);
        }
    }

    @ParametersAreNonnullByDefault
    @Override
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        if (state.getValue(POWERED)) {
            this.updateState(worldIn, pos);
        }
    }

    private void updateState(World worldIn, BlockPos pos) {
        BlockState blockState = worldIn.getBlockState(pos);
        boolean isPowered = blockState.getValue(POWERED);
        if (!blockState.getShape(worldIn, pos).isEmpty()) {
            List<? extends Entity> mobsInBlock = worldIn.getEntities(null, blockState.getShape(worldIn, pos).bounds().move(pos));

            // Checks if entity is an Astral Entity and triggers pressure plates
            final boolean canTrigger = mobsInBlock.stream().anyMatch(EthericPowder::isEntityAstralAndTriggersPressurePlate);

            if (isPowered != canTrigger) {
                blockState = blockState.setValue(POWERED, canTrigger);
                worldIn.setBlock(pos, blockState, 3);
            }
            if (canTrigger) {
                worldIn.getBlockTicks().scheduleTick(new BlockPos(pos), this, 30);
            }
        }
        else {
            blockState = blockState.setValue(POWERED, false);
            worldIn.setBlock(pos, blockState, 3);
        }

    }

    @Override
    public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side) {
        return true;
    }

    public static boolean isEntityAstralAndTriggersPressurePlate(Entity entity) {
        return !entity.isIgnoringBlockTriggers() && entity instanceof LivingEntity && TravelEffects.isEntityAstral((LivingEntity) entity);
    }

    @Override
    @ParametersAreNonnullByDefault
    public int getSignal(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
        return blockState.getValue(TripWireHookBlock.POWERED) ? 15 : 0;
    }

    @Override
    @ParametersAreNonnullByDefault
    public int getDirectSignal(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
        return blockState.getValue(TripWireHookBlock.POWERED) ? 15 : 0;
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        return state.getValue(TripWireHookBlock.POWERED) ? 7 : 0;
    }
}
