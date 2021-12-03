package com.alan19.astral.blocks;

import com.alan19.astral.configs.AstralConfig;
import com.alan19.astral.items.AstralItems;
import com.alan19.astral.tags.AstralTags;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeHooks;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Comparator;
import java.util.Random;

import net.minecraft.block.AbstractBlock.Properties;

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
    protected boolean mayPlaceOn(BlockState state, IBlockReader worldIn, BlockPos pos) {
        Block block = state.getBlock();
        return AstralTags.SNOWBERRY_SUSTAIN.contains(block);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean canSurvive(BlockState state, IWorldReader worldIn, BlockPos pos) {
        BlockPos blockpos = pos.below();
        return this.mayPlaceOn(worldIn.getBlockState(blockpos), worldIn, blockpos);
    }

    @Nonnull
    @Override
    @ParametersAreNonnullByDefault
    public ItemStack getCloneItemStack(IBlockReader worldIn, BlockPos pos, BlockState state) {
        return new ItemStack(AstralItems.SNOWBERRY.get().asItem());
    }

    /**
     * When the block is right clicked, check if the bush is ready to harvest (age 3), and if it is, drop a snowberry
     * item and lower it's age to age 2.
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
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        int age = state.getValue(AGE);
        boolean readyToHarvest = age == 3;
        if (!readyToHarvest && player.getItemInHand(handIn).getItem() == Items.BONE_MEAL) {
            return ActionResultType.PASS;
        }
        else if (age > 1) {
            int j = 1 + worldIn.random.nextInt(2);
            popResource(worldIn, pos, new ItemStack(AstralItems.SNOWBERRY.get().asItem(), j + (readyToHarvest ? 1 : 0)));
            worldIn.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundCategory.BLOCKS, 1.0F, 0.8F + worldIn.random.nextFloat() * 0.4F);
            worldIn.setBlock(pos, state.setValue(AGE, 1), 2);
            return ActionResultType.SUCCESS;
        }
        else {
            return super.use(state, worldIn, pos, player, handIn, hit);
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        int i = state.getValue(AGE);
        if (i < 3 && worldIn.getRawBrightness(pos.above(), 0) >= 9 && ForgeHooks.onCropsGrowPre(worldIn, pos, state, rand.nextInt(5) == 0)) {
            worldIn.setBlock(pos, state.setValue(AGE, i + 1), 2);
            spreadSnow(worldIn, pos, rand);
            ForgeHooks.onCropsGrowPost(worldIn, pos, state);
        }
    }

    public void spreadSnow(ServerWorld worldIn, BlockPos pos, Random rand) {
        if (rand.nextInt(AstralConfig.getWorldgenSettings().snowberrySpreadSnowChance.get()) == 0) {
            BlockPos.betweenClosedStream(pos.offset(-1, 0, -1), pos.offset(1, 0, 1))
                    .filter(blockPos -> isBlockAirOrSnow(blockPos, worldIn))
                    .map(blockPos -> getPosSnowLevelPair(blockPos.immutable(), worldIn))
                    .min(Comparator.comparing(Pair::getRight))
                    .ifPresent(blockPosIntegerPair -> {
                        final BlockState blockState = worldIn.getBlockState(blockPosIntegerPair.getLeft());
                        if (blockState.getBlock() == Blocks.AIR) {
                            worldIn.setBlock(blockPosIntegerPair.getLeft(), Blocks.SNOW.defaultBlockState(), 2);
                        }
                        else if (blockState.getBlock() == Blocks.SNOW) {
                            worldIn.setBlock(blockPosIntegerPair.getLeft(), blockState.setValue(SnowBlock.LAYERS, blockState.getValue(SnowBlock.LAYERS) + 1), 2);
                        }
                    });
        }
    }

    private boolean isBlockAirOrSnow(BlockPos blockPos, ServerWorld worldIn) {
        BlockState state = worldIn.getBlockState(blockPos);
        return Blocks.SNOW.defaultBlockState().canSurvive(worldIn, blockPos) && (state.getBlock() == Blocks.SNOW ? state.getValue(SnowBlock.LAYERS) < 8 : state.getBlock() == Blocks.AIR);
    }

    private Pair<BlockPos, Integer> getPosSnowLevelPair(BlockPos pos, ServerWorld worldIn) {
        if (worldIn.getBlockState(pos).getBlock() == Blocks.AIR) {
            return Pair.of(pos, 0);
        }
        else if (worldIn.getBlockState(pos).getBlock() == Blocks.SNOW) {
            return Pair.of(pos, worldIn.getBlockState(pos).getValue(SnowBlock.LAYERS));
        }
        return Pair.of(pos, -1);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void performBonemeal(ServerWorld worldIn, Random rand, BlockPos pos, BlockState state) {
        super.performBonemeal(worldIn, rand, pos, state);
        spreadSnow(worldIn, pos, rand);
    }
}
