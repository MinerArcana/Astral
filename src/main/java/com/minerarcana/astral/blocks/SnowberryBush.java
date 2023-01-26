package com.minerarcana.astral.blocks;

import com.minerarcana.astral.items.AstralItems;
import com.minerarcana.astral.tags.AstralTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.ForgeHooks;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Comparator;

public class SnowberryBush extends SweetBerryBushBlock {

    public SnowberryBush() {
        super(Properties.of(Material.PLANT)
                .sound(SoundType.GRASS)
                .randomTicks()
                .strength(0.2f)
                .noOcclusion());
    }

    @Override
    @ParametersAreNonnullByDefault
    protected boolean mayPlaceOn(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return state.is(AstralTags.SNOWBERRY_PLANTABLE_ON);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        BlockPos blockpos = pos.below();
        return this.mayPlaceOn(worldIn.getBlockState(blockpos), worldIn, blockpos);
    }

    @Nonnull
    @Override
    @ParametersAreNonnullByDefault
    public ItemStack getCloneItemStack(BlockGetter worldIn, BlockPos pos, BlockState state) {
        return new ItemStack(AstralItems.SNOWBERRIES.get().asItem());
    }

    /**
     * When the block is right clicked, check if the bush is ready to harvest (age 3), and if it is, drop a snowberry
     * item and lower its age to age 2.
     * Copied from SweetBerryBushBlock.java
     *
     * @param state   The blockstate of the block
     * @param worldIn The world object
     * @param pos     The BlockPos of the block
     * @param player  The player
     * @param handIn  The item being held
     * @param hit     Where the block was hit
     * @return Whether the interaction was successful or not
     */
    @Nonnull
    @Override
    @ParametersAreNonnullByDefault
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        int age = state.getValue(AGE);
        boolean readyToHarvest = age == 3;
        if (!readyToHarvest && player.getItemInHand(handIn).getItem() == Items.BONE_MEAL) {
            return InteractionResult.PASS;
        } else if (age > 1) {
            int j = 1 + worldIn.random.nextInt(2);
            popResource(worldIn, pos, new ItemStack(AstralItems.SNOWBERRIES.get().asItem(), j + (readyToHarvest ? 1 : 0)));
            worldIn.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + worldIn.random.nextFloat() * 0.4F);
            worldIn.setBlock(pos, state.setValue(AGE, 1), 2);
            return InteractionResult.SUCCESS;
        } else {
            return super.use(state, worldIn, pos, player, handIn, hit);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    @ParametersAreNonnullByDefault
    public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource rand) {
        int i = state.getValue(AGE);
        if (i < 3 && worldIn.getRawBrightness(pos.above(), 0) >= 9 && ForgeHooks.onCropsGrowPre(worldIn, pos, state, rand.nextInt(5) == 0)) {
            worldIn.setBlock(pos, state.setValue(AGE, i + 1), 2);
            spreadSnow(worldIn, pos, rand);
            ForgeHooks.onCropsGrowPost(worldIn, pos, state);
        }
    }

    public void spreadSnow(ServerLevel worldIn, BlockPos pos, RandomSource rand) {
        BlockPos.betweenClosedStream(pos.offset(-1, 0, -1), pos.offset(1, 0, 1))
                .filter(blockPos -> isBlockAirOrSnow(blockPos, worldIn))
                .map(blockPos -> getPosSnowLevelPair(blockPos.immutable(), worldIn))
                .min(Comparator.comparing(Pair::getRight))
                .ifPresent(blockPosIntegerPair -> {
                    final BlockState blockState = worldIn.getBlockState(blockPosIntegerPair.getLeft());
                    if (blockState.getBlock() == Blocks.AIR) {
                        worldIn.setBlock(blockPosIntegerPair.getLeft(), Blocks.SNOW.defaultBlockState(), 2);
                    } else if (blockState.getBlock() == Blocks.SNOW) {
                        worldIn.setBlock(blockPosIntegerPair.getLeft(), blockState.setValue(SnowLayerBlock.LAYERS, blockState.getValue(SnowLayerBlock.LAYERS) + 1), 2);
                    }
                });
    }

    private boolean isBlockAirOrSnow(BlockPos blockPos, ServerLevel worldIn) {
        BlockState state = worldIn.getBlockState(blockPos);
        return Blocks.SNOW.defaultBlockState().canSurvive(worldIn, blockPos) && (state.getBlock() == Blocks.SNOW ? state.getValue(SnowLayerBlock.LAYERS) < 8 : state.getBlock() == Blocks.AIR);
    }

    private Pair<BlockPos, Integer> getPosSnowLevelPair(BlockPos pos, ServerLevel worldIn) {
        if (worldIn.getBlockState(pos).getBlock() == Blocks.AIR) {
            return Pair.of(pos, 0);
        } else if (worldIn.getBlockState(pos).getBlock() == Blocks.SNOW) {
            return Pair.of(pos, worldIn.getBlockState(pos).getValue(SnowLayerBlock.LAYERS));
        }
        return Pair.of(pos, -1);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void performBonemeal(ServerLevel worldIn, RandomSource rand, BlockPos pos, BlockState state) {
        super.performBonemeal(worldIn, rand, pos, state);
        spreadSnow(worldIn, pos, rand);
    }
}
