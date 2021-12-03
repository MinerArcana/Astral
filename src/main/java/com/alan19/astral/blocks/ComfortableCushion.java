package com.alan19.astral.blocks;

import com.alan19.astral.api.AstralAPI;
import com.alan19.astral.dimensions.AstralDimensions;
import com.alan19.astral.mentalconstructs.AstralMentalConstructs;
import com.alan19.astral.mentalconstructs.Garden;
import com.alan19.astral.tags.AstralTags;
import com.alan19.astral.util.Constants;
import com.alan19.astral.util.VoxelShapeUtils;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.DyeColor;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

import net.minecraft.block.AbstractBlock.Properties;

public class ComfortableCushion extends Block implements MentalConstructController {
    protected static final VoxelShape CUSHION_SHAPE = Block.box(1.0D, 0.0D, 6.0D, 15.0D, 4.0D, 14.0D);


    public ComfortableCushion() {
        super(Properties.of(Material.WOOL, DyeColor.LIGHT_BLUE).randomTicks().strength(0.8f).sound(SoundType.WOOL).noOcclusion());
        this.registerDefaultState(this.getStateDefinition().any().setValue(Constants.TRACKED_CONSTRUCT, false).setValue(HorizontalBlock.FACING, Direction.NORTH));
    }

    @Nonnull
    @Override
    @ParametersAreNonnullByDefault
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return VoxelShapeUtils.rotateHorizontal(CUSHION_SHAPE, state.getValue(HorizontalBlock.FACING));
    }

    @Nonnull
    @Override
    public ActionResultType use(@Nonnull BlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull PlayerEntity player, @Nonnull Hand handIn, @Nonnull BlockRayTraceResult hit) {
        if (worldIn instanceof ServerWorld && worldIn.dimension() == AstralDimensions.INNER_REALM && handIn == Hand.MAIN_HAND) {
            final int level = calculateLevel(worldIn, pos);
            AstralAPI.getConstructTracker((ServerWorld) worldIn).ifPresent(tracker -> tracker.getMentalConstructsForPlayer(player).modifyConstructInfo(pos, (ServerWorld) worldIn, AstralMentalConstructs.GARDEN.get(), level));
            worldIn.setBlockAndUpdate(pos, state.setValue(Constants.TRACKED_CONSTRUCT, true));
            final double particleNum = Math.max(1, 15 - Math.ceil(Garden.getConversionRatio(level)) + 1);
            worldIn.playSound(null, pos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0F, worldIn.getRandom().nextFloat() * 0.1F + 0.9F);
            for (int i = 0; i < particleNum; i++) {
                ((ServerWorld) worldIn).sendParticles(ParticleTypes.ENCHANT, pos.getX() + i / particleNum, pos.getY() + .6, pos.getZ() + .5, 1, 0, 0, 0, .01);
            }
            return ActionResultType.SUCCESS;
        }
        return super.use(state, worldIn, pos, player, handIn, hit);
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(Constants.TRACKED_CONSTRUCT, AbstractFurnaceBlock.FACING));
    }

    /**
     * Calculates the level the Garden mental construct is based off the number of water, dirt, leaf, and wood blocks in a 7 block area multiplied by the number of plant blocks
     *
     * @param worldIn The world used to get the blockstates of the blocks being checked
     * @param pos     The BlockPos of the comfortable cushion
     * @return The level of the Garden
     */
    public int calculateLevel(World worldIn, BlockPos pos) {
        //Get number of water blocks, dirt blocks, leaf blocks, and wood blocks and multiply them by the number of plants
        //TODO Add config option to limit number of "valid" blocks to prevent people from making cubes of organic matter
        return BlockPos.betweenClosedStream(pos.offset(-3, -3, -3), pos.offset(3, 3, 3))
                .map(blockPos -> getStates(worldIn, blockPos))
                .map(this::sumStates)
                .reduce((objectPlantPair1, objectPlantPair2) -> Pair.of(objectPlantPair1.getLeft() + objectPlantPair2.getLeft(), objectPlantPair1.getRight() + objectPlantPair2.getRight()))
                .map(totalCountPair -> totalCountPair.getLeft() * totalCountPair.getRight())
                .orElse(0);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState().setValue(AbstractFurnaceBlock.FACING, context.getHorizontalDirection().getOpposite());
    }

    /**
     * Creates a pair of two integers based off the block or fluid at one BlockPos
     *
     * @param pair A pair that represents the block or fluid at one BlockPos
     * @return A pair with 1 as the key if the BlockPos contains a water, dirt, leaf, or wood block and 0 otherwise, and 1 as the value if the BlockPos contains a plant and 0 otherwise
     */
    private Pair<Integer, Integer> sumStates(Pair<BlockState, FluidState> pair) {
        int containsObject = 0;
        int containsPlant = 0;
        if (AstralTags.GARDEN_OBJECTS.contains(pair.getLeft().getBlock()) || FluidTags.WATER.contains(pair.getRight().getType())) {
            containsObject = 1;
        }
        if (AstralTags.GARDEN_PLANTS.contains(pair.getLeft().getBlock())) {
            containsPlant = 1;
        }
        return Pair.of(containsObject, containsPlant);
    }

    public Pair<BlockState, FluidState> getStates(IWorld world, BlockPos pos) {
        return Pair.of(world.getBlockState(pos), world.getFluidState(pos));
    }

    @Override
    public void onRemove(@Nonnull BlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
        MentalConstructController.onReplaced(worldIn, pos, this, AstralMentalConstructs.GARDEN.get());
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public void tick(BlockState state, @Nonnull ServerWorld worldIn, @Nonnull BlockPos pos, @Nonnull Random rand) {
        MentalConstructController.tick(state, worldIn, pos, calculateLevel(worldIn, pos), AstralMentalConstructs.GARDEN.get());
        super.tick(state, worldIn, pos, rand);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return state.getValue(Constants.TRACKED_CONSTRUCT);
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, @Nonnull World worldIn, @Nonnull BlockPos pos) {
        return MentalConstructController.getComparatorInputOverride(blockState, (int) Math.ceil(Garden.getConversionRatio(calculateLevel(worldIn, pos))), super.getAnalogOutputSignal(blockState, worldIn, pos));
    }
}
