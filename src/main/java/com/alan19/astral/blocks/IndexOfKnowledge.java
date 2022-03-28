package com.alan19.astral.blocks;

import com.alan19.astral.api.AstralAPI;
import com.alan19.astral.dimensions.AstralDimensions;
import com.alan19.astral.mentalconstructs.AstralMentalConstructs;
import com.alan19.astral.util.Constants;
import com.alan19.astral.util.ExperienceHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;
import java.util.stream.IntStream;

public class IndexOfKnowledge extends Block implements MentalConstructController {
    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D);

    public IndexOfKnowledge() {
        super(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_RED).strength(1.5F));
        this.registerDefaultState(getStateDefinition().any().setValue(Constants.TRACKED_CONSTRUCT, false).setValue(Constants.LIBRARY_LEVEL, 0).setValue(Constants.CAPPED_LEVEL, false));
    }

    @Nonnull
    @Override
    @ParametersAreNonnullByDefault
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Nonnull
    @Override
    public InteractionResult use(@Nonnull BlockState state, @Nonnull Level worldIn, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand handIn, @Nonnull BlockHitResult hit) {
        final int libraryLevel = state.getValue(Constants.LIBRARY_LEVEL);
        if (worldIn instanceof ServerLevel && worldIn.dimension() == AstralDimensions.INNER_REALM && handIn == InteractionHand.MAIN_HAND) {
            int levelRequirement = (libraryLevel + 1) * 10;
            if (player.experienceLevel >= levelRequirement && calculateLevel(worldIn, pos) > libraryLevel) {
                ExperienceHelper.drainPlayerXP(player, ExperienceHelper.getExperienceForLevel(levelRequirement));
                worldIn.setBlockAndUpdate(pos, state.setValue(Constants.TRACKED_CONSTRUCT, true));
                worldIn.setBlockAndUpdate(pos, state.setValue(Constants.LIBRARY_LEVEL, libraryLevel + 1));
                AstralAPI.getConstructTracker((ServerLevel) worldIn).ifPresent(tracker -> tracker.getMentalConstructsForPlayer(player).modifyConstructInfo(pos, (ServerLevel) worldIn, AstralMentalConstructs.LIBRARY.get(), Math.min(calculateLevel(worldIn, pos), state.getValue(Constants.LIBRARY_LEVEL))));
                worldIn.playSound(player, pos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1.0F, worldIn.getRandom().nextFloat() * 0.1F + 0.9F);
                state.setValue(Constants.CAPPED_LEVEL, libraryLevel >= calculateLevel(worldIn, pos));
                final int newLevel = state.getValue(Constants.LIBRARY_LEVEL);
                IntStream.range(0, newLevel).forEach(i -> ((ServerLevel) worldIn).sendParticles(ParticleTypes.ENCHANT, pos.getX() + (double) i / newLevel, pos.getY() + .6, pos.getZ() + .5, 1, 0, 0, 0, .01));
                return InteractionResult.SUCCESS;
            }
        }
        state.setValue(Constants.CAPPED_LEVEL, libraryLevel >= calculateLevel(worldIn, pos));
        return super.use(state, worldIn, pos, player, handIn, hit);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(Constants.TRACKED_CONSTRUCT).add(Constants.LIBRARY_LEVEL).add(Constants.CAPPED_LEVEL));
    }

    /**
     * Calculates the maximum level for the Index of Knowledge, which is equal to the number of bookshelves in a 3 block radius * .25, plus the number of lecterns with books in a 3 block radius * .5
     *
     * @param world The world the Index of Knowledge is in
     * @param pos   The BlockPos of the Index of Knowledge
     * @return The maximum level of the Index of Knowledge
     */
    @Override
    public int calculateLevel(Level world, BlockPos pos) {
        return BlockPos.betweenClosedStream(pos.offset(-3, -3, -3), pos.offset(3, 3, 3))
                .map(blockPos -> sumStates(world, blockPos))
                .reduce((integerIntegerPair, integerIntegerPair2) -> Pair.of(integerIntegerPair.getLeft() + integerIntegerPair2.getLeft(), integerIntegerPair.getRight() + integerIntegerPair2.getRight()))
                .map(integerIntegerPair -> (int) (integerIntegerPair.getLeft() * .25 + integerIntegerPair.getRight() * .5))
                .orElse(0);
    }

    private Pair<Integer, Integer> sumStates(Level world, BlockPos blockPos) {
        return Pair.of(world.getBlockState(blockPos).getBlock() == Blocks.BOOKSHELF ? 1 : 0, world.getBlockEntity(blockPos) instanceof LecternBlockEntity && ((LecternBlockEntity) world.getBlockEntity(blockPos)).hasBook() ? 1 : 0);
    }

    @Override
    public void onRemove(@Nonnull BlockState state, @Nonnull Level worldIn, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
        MentalConstructController.onReplaced(worldIn, pos, this, AstralMentalConstructs.LIBRARY.get());
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public void tick(BlockState state, @Nonnull ServerLevel worldIn, @Nonnull BlockPos pos, @Nonnull Random rand) {
        final int libraryLevel = state.getValue(Constants.LIBRARY_LEVEL);
        state.setValue(Constants.CAPPED_LEVEL, libraryLevel >= calculateLevel(worldIn, pos));
        MentalConstructController.tick(state, worldIn, pos, Math.min(calculateLevel(worldIn, pos), libraryLevel), AstralMentalConstructs.LIBRARY.get());
        super.tick(state, worldIn, pos, rand);
    }

    @Override
    public boolean hasAnalogOutputSignal(@Nonnull BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, @Nonnull Level worldIn, @Nonnull BlockPos pos) {
        return Math.min(calculateLevel(worldIn, pos), blockState.getValue(Constants.LIBRARY_LEVEL));
    }
}
