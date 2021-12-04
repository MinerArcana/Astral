package com.alan19.astral.blocks;

import com.alan19.astral.blocks.etherealblocks.Ethereal;
import com.alan19.astral.blocks.etherealblocks.EtherealBlock;
import com.alan19.astral.events.astraltravel.TravelEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TripWireHookBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Random;

import static net.minecraft.block.TripWireBlock.POWERED;

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
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return Ethereal.getShape(BASE_SHAPE);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }

    @ParametersAreNonnullByDefault
    @Override
    public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
        if (worldIn instanceof ServerLevel && !state.getValue(POWERED)) {
            this.updateState(worldIn, pos);
        }
    }

    @ParametersAreNonnullByDefault
    @Override
    public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, Random rand) {
        if (state.getValue(POWERED)) {
            this.updateState(worldIn, pos);
        }
    }

    private void updateState(Level worldIn, BlockPos pos) {
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
    public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, @Nullable Direction side) {
        return true;
    }

    public static boolean isEntityAstralAndTriggersPressurePlate(Entity entity) {
        return !entity.isIgnoringBlockTriggers() && entity instanceof LivingEntity && TravelEffects.isEntityAstral((LivingEntity) entity);
    }

    @Override
    @ParametersAreNonnullByDefault
    public int getSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
        return blockState.getValue(TripWireHookBlock.POWERED) ? 15 : 0;
    }

    @Override
    @ParametersAreNonnullByDefault
    public int getDirectSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
        return blockState.getValue(TripWireHookBlock.POWERED) ? 15 : 0;
    }

    @Override
    public int getLightValue(BlockState state, BlockGetter world, BlockPos pos) {
        return state.getValue(TripWireHookBlock.POWERED) ? 7 : 0;
    }
}
