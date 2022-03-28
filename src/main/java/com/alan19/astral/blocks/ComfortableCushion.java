package com.alan19.astral.blocks;

import com.alan19.astral.api.AstralAPI;
import com.alan19.astral.dimensions.AstralDimensions;
import com.alan19.astral.mentalconstructs.AstralMentalConstructs;
import com.alan19.astral.mentalconstructs.Garden;
import com.alan19.astral.tags.AstralTags;
import com.alan19.astral.util.Constants;
import com.alan19.astral.util.VoxelShapeUtils;
import net.minecraft.block.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public class ComfortableCushion extends Block implements MentalConstructController {
    protected static final VoxelShape CUSHION_SHAPE = Block.box(1.0D, 0.0D, 6.0D, 15.0D, 4.0D, 14.0D);


    public ComfortableCushion() {
        super(Properties.of(Material.WOOL, DyeColor.LIGHT_BLUE).randomTicks().strength(0.8f).sound(SoundType.WOOL).noOcclusion());
        this.registerDefaultState(this.getStateDefinition().any().setValue(Constants.TRACKED_CONSTRUCT, false).setValue(HorizontalDirectionalBlock.FACING, Direction.NORTH));
    }

    @Nonnull
    @Override
    @ParametersAreNonnullByDefault
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return VoxelShapeUtils.rotateHorizontal(CUSHION_SHAPE, state.getValue(HorizontalDirectionalBlock.FACING));
    }

    @Nonnull
    @Override
    public InteractionResult use(@Nonnull BlockState state, @Nonnull Level worldIn, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand handIn, @Nonnull BlockHitResult hit) {
        if (worldIn instanceof ServerLevel && worldIn.dimension() == AstralDimensions.INNER_REALM && handIn == InteractionHand.MAIN_HAND) {
            final int level = calculateLevel(worldIn, pos);
            AstralAPI.getConstructTracker((ServerLevel) worldIn).ifPresent(tracker -> tracker.getMentalConstructsForPlayer(player).modifyConstructInfo(pos, (ServerLevel) worldIn, AstralMentalConstructs.GARDEN.get(), level));
            worldIn.setBlockAndUpdate(pos, state.setValue(Constants.TRACKED_CONSTRUCT, true));
            final double particleNum = Math.max(1, 15 - Math.ceil(Garden.getConversionRatio(level)) + 1);
            worldIn.playSound(null, pos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1.0F, worldIn.getRandom().nextFloat() * 0.1F + 0.9F);
            for (int i = 0; i < particleNum; i++) {
                ((ServerLevel) worldIn).sendParticles(ParticleTypes.ENCHANT, pos.getX() + i / particleNum, pos.getY() + .6, pos.getZ() + .5, 1, 0, 0, 0, .01);
            }
            return InteractionResult.SUCCESS;
        }
        return super.use(state, worldIn, pos, player, handIn, hit);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(Constants.TRACKED_CONSTRUCT, AbstractFurnaceBlock.FACING));
    }

    /**
     * Calculates the level the Garden mental construct is based off the number of water, dirt, leaf, and wood blocks in a 7 block area multiplied by the number of plant blocks
     *
     * @param worldIn The world used to get the blockstates of the blocks being checked
     * @param pos     The BlockPos of the comfortable cushion
     * @return The level of the Garden
     */
    public int calculateLevel(Level worldIn, BlockPos pos) {
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
    public BlockState getStateForPlacement(BlockPlaceContext context) {
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

    public Pair<BlockState, FluidState> getStates(LevelAccessor world, BlockPos pos) {
        return Pair.of(world.getBlockState(pos), world.getFluidState(pos));
    }

    @Override
    public void onRemove(@Nonnull BlockState state, @Nonnull Level worldIn, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
        MentalConstructController.onReplaced(worldIn, pos, this, AstralMentalConstructs.GARDEN.get());
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public void tick(BlockState state, @Nonnull ServerLevel worldIn, @Nonnull BlockPos pos, @Nonnull Random rand) {
        MentalConstructController.tick(state, worldIn, pos, calculateLevel(worldIn, pos), AstralMentalConstructs.GARDEN.get());
        super.tick(state, worldIn, pos, rand);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return state.getValue(Constants.TRACKED_CONSTRUCT);
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, @Nonnull Level worldIn, @Nonnull BlockPos pos) {
        return MentalConstructController.getComparatorInputOverride(blockState, (int) Math.ceil(Garden.getConversionRatio(calculateLevel(worldIn, pos))), super.getAnalogOutputSignal(blockState, worldIn, pos));
    }
}
