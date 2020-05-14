package com.alan199921.astral.blocks;

import com.alan199921.astral.api.AstralAPI;
import com.alan199921.astral.dimensions.AstralDimensions;
import com.alan199921.astral.mentalconstructs.AstralMentalConstructs;
import com.alan199921.astral.mentalconstructs.Garden;
import com.alan199921.astral.mentalconstructs.MentalConstruct;
import com.alan199921.astral.tags.AstralTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.Random;
import java.util.stream.Stream;

public class ComfortableCushion extends SlabBlock implements MentalConstructController {
    public ComfortableCushion() {
        super(Properties.create(Material.WOOL).tickRandomly());
        this.setDefaultState(this.getStateContainer().getBaseState().with(MentalConstruct.TRACKED_CONSTRUCT, false));
    }

    @Override
    public ActionResultType onBlockActivated(@Nonnull BlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull PlayerEntity player, @Nonnull Hand handIn, @Nonnull BlockRayTraceResult hit) {
        if (worldIn instanceof ServerWorld && worldIn.getDimension().getType() == DimensionType.byName(AstralDimensions.INNER_REALM)) {
            AstralAPI.getConstructTracker((ServerWorld) worldIn).ifPresent(tracker -> tracker.getMentalConstructsForPlayer(player).modifyConstructInfo(pos, (ServerWorld) worldIn, AstralMentalConstructs.GARDEN.get(), calculateLevel(worldIn, pos)));
            worldIn.setBlockState(pos, state.with(MentalConstruct.TRACKED_CONSTRUCT, true));
            return ActionResultType.SUCCESS;
        }
        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder.add(MentalConstruct.TRACKED_CONSTRUCT));
    }

    /**
     * Calculates the level the Garden mental construct is based off the number of water, dirt, leaf, and wood blocks in a 7 block area multiplied by the number of plant blocks
     *
     * @param worldIn The world used to get the blockstates of the blocks being checked
     * @param pos     The BlockPos of the comfortable cushion
     * @return The level of the Garden
     */
    public int calculateLevel(World worldIn, BlockPos pos) {
        final Stream<BlockPos> gardenRegion = BlockPos.getAllInBox(pos.add(-3, -3, -3), pos.add(3, 3, 3));

        //Get number of water blocks, dirt blocks, leaf blocks, and wood blocks and multiply them by the number of plants
        return gardenRegion.map(blockPos -> this.getStates(worldIn, blockPos))
                .map(this::sumStates)
                .reduce((objectPlantPair1, objectPlantPair2) -> Pair.of(objectPlantPair1.getLeft() + objectPlantPair2.getLeft(), objectPlantPair1.getRight() + objectPlantPair2.getRight()))
                .map(totalCountPair -> totalCountPair.getLeft() * totalCountPair.getRight())
                .orElse(0);
    }

    /**
     * Creates a pair of two integers based off the block or fluid at one BlockPos
     *
     * @param pair A pair that represents the block or fluid at one BlockPos
     * @return A pair with 1 as the key if the BlockPos contains a water, dirt, leaf, or wood block and 0 otherwise, and 1 as the value if the BlockPos contains a plant and 0 otherwise
     */
    private Pair<Integer, Integer> sumStates(Pair<BlockState, IFluidState> pair) {
        int containsObject = 0;
        int containsPlant = 0;
        if (AstralTags.GARDEN_OBJECTS.contains(pair.getLeft().getBlock()) || FluidTags.WATER.contains(pair.getRight().getFluid())) {
            containsObject = 1;
        }
        if (AstralTags.GARDEN_PLANTS.contains(pair.getLeft().getBlock())) {
            containsPlant = 1;
        }
        return Pair.of(containsObject, containsPlant);
    }

    public Pair<BlockState, IFluidState> getStates(IWorld world, BlockPos pos) {
        return Pair.of(world.getBlockState(pos), world.getFluidState(pos));
    }

    @Override
    public void onReplaced(@Nonnull BlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
        MentalConstructController.onReplaced(worldIn, pos, this);
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public void tick(BlockState state, @Nonnull ServerWorld worldIn, @Nonnull BlockPos pos, @Nonnull Random rand) {
        MentalConstructController.tick(state, worldIn, pos, calculateLevel(worldIn, pos));
        super.tick(state, worldIn, pos, rand);
    }

    @Override
    public boolean hasComparatorInputOverride(BlockState state) {
        return state.get(MentalConstruct.TRACKED_CONSTRUCT);
    }

    @Override
    public int getComparatorInputOverride(BlockState blockState, @Nonnull World worldIn, @Nonnull BlockPos pos) {
        return MentalConstructController.getComparatorInputOverride(blockState, (int) Math.ceil(Garden.getConversionRatio(calculateLevel(worldIn, pos))), super.getComparatorInputOverride(blockState, worldIn, pos));
    }
}
