package com.alan19.astral.blocks;

import com.alan19.astral.api.AstralAPI;
import com.alan19.astral.dimensions.AstralDimensions;
import com.alan19.astral.mentalconstructs.AstralMentalConstructs;
import com.alan19.astral.util.Constants;
import com.alan19.astral.util.ExperienceHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SPlaySoundEventPacket;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.LecternTileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.Random;

public class IndexOfKnowledge extends Block implements MentalConstructController {
    public IndexOfKnowledge() {
        super(Block.Properties.create(Material.ROCK, MaterialColor.RED).hardnessAndResistance(1.5F));
        this.setDefaultState(getStateContainer().getBaseState().with(Constants.TRACKED_CONSTRUCT, false).with(Constants.LIBRARY_LEVEL, 0).with(Constants.CAPPED_LEVEL, false));
    }

    @Override
    public ActionResultType onBlockActivated(@Nonnull BlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull PlayerEntity player, @Nonnull Hand handIn, @Nonnull BlockRayTraceResult hit) {
        final int libraryLevel = state.get(Constants.LIBRARY_LEVEL);
        if (worldIn instanceof ServerWorld && worldIn.getDimension().getType() == DimensionType.byName(AstralDimensions.INNER_REALM) && handIn == Hand.MAIN_HAND) {
            int levelRequirement = (libraryLevel + 1) * 10;
            if (player.experienceLevel >= levelRequirement && calculateLevel(worldIn, pos) > libraryLevel) {
                ExperienceHelper.drainPlayerXP(player, ExperienceHelper.getExperienceForLevel(levelRequirement));
                worldIn.setBlockState(pos, state.with(Constants.TRACKED_CONSTRUCT, true));
                worldIn.setBlockState(pos, state.with(Constants.LIBRARY_LEVEL, libraryLevel + 1));
                AstralAPI.getConstructTracker((ServerWorld) worldIn).ifPresent(tracker -> tracker.getMentalConstructsForPlayer(player).modifyConstructInfo(pos, (ServerWorld) worldIn, AstralMentalConstructs.LIBRARY.get(), Math.min(calculateLevel(worldIn, pos), state.get(Constants.LIBRARY_LEVEL))));
                if (player instanceof ServerPlayerEntity) {
                    ((ServerPlayerEntity) player).connection.sendPacket(new SPlaySoundEventPacket());
                }
                worldIn.playSound(null, pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0F, worldIn.getRandom().nextFloat() * 0.1F + 0.9F);
                state.with(Constants.CAPPED_LEVEL, libraryLevel >= calculateLevel(worldIn, pos));
                return ActionResultType.SUCCESS;
            }
        }
        state.with(Constants.CAPPED_LEVEL, libraryLevel >= calculateLevel(worldIn, pos));
        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder.add(Constants.TRACKED_CONSTRUCT).add(Constants.LIBRARY_LEVEL).add(Constants.CAPPED_LEVEL));
    }

    /**
     * Calculates the maximum level for the Index of Knowledge, which is equal to the number of bookshelves in a 3 block radius * .25, plus the number of lecterns with books in a 3 block radius * .5
     *
     * @param world The world the Index of Knowledge is in
     * @param pos   The BlockPos of the Index of Knowledge
     * @return The maximum level of the Index of Knowledge
     */
    @Override
    public int calculateLevel(World world, BlockPos pos) {
        return BlockPos.getAllInBox(pos.add(-3, -3, -3), pos.add(3, 3, 3))
                .map(blockPos -> sumStates(world, blockPos))
                .reduce((integerIntegerPair, integerIntegerPair2) -> Pair.of(integerIntegerPair.getLeft() + integerIntegerPair2.getLeft(), integerIntegerPair.getRight() * +integerIntegerPair2.getRight()))
                .map(integerIntegerPair -> (int) (integerIntegerPair.getLeft() * .25 + integerIntegerPair.getRight() * .5))
                .orElse(0);
    }

    private Pair<Integer, Integer> sumStates(World world, BlockPos blockPos) {
        return Pair.of(world.getBlockState(blockPos).getBlock() == Blocks.BOOKSHELF ? 1 : 0, world.getTileEntity(blockPos) instanceof LecternTileEntity && ((LecternTileEntity) world.getTileEntity(blockPos)).hasBook() ? 1 : 0);
    }


    @Override
    public void onReplaced(@Nonnull BlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
        MentalConstructController.onReplaced(worldIn, pos, this, AstralMentalConstructs.LIBRARY.get());
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public void tick(BlockState state, @Nonnull ServerWorld worldIn, @Nonnull BlockPos pos, @Nonnull Random rand) {
        final int libraryLevel = state.get(Constants.LIBRARY_LEVEL);
        state.with(Constants.CAPPED_LEVEL, libraryLevel >= calculateLevel(worldIn, pos));
        MentalConstructController.tick(state, worldIn, pos, Math.min(calculateLevel(worldIn, pos), libraryLevel), AstralMentalConstructs.LIBRARY.get());
        super.tick(state, worldIn, pos, rand);
    }

    @Override
    public boolean hasComparatorInputOverride(@Nonnull BlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(BlockState blockState, @Nonnull World worldIn, @Nonnull BlockPos pos) {
        return Math.min(calculateLevel(worldIn, pos), blockState.get(Constants.LIBRARY_LEVEL));
    }
}
