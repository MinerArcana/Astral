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

public class SnowberryBush extends SweetBerryBushBlock {

    public SnowberryBush() {
        super(Properties.create(Material.PLANTS)
                .sound(SoundType.PLANT)
                .tickRandomly()
                .hardnessAndResistance(0.2f)
                .notSolid());
    }

    @Override
    @ParametersAreNonnullByDefault
    protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
        Block block = state.getBlock();
        return AstralTags.SNOWBERRY_SUSTAIN.contains(block);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        BlockPos blockpos = pos.down();
        return this.isValidGround(worldIn.getBlockState(blockpos), worldIn, blockpos);
    }

    @Nonnull
    @Override
    @ParametersAreNonnullByDefault
    public ItemStack getItem(IBlockReader worldIn, BlockPos pos, BlockState state) {
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
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        int age = state.get(AGE);
        boolean readyToHarvest = age == 3;
        if (!readyToHarvest && player.getHeldItem(handIn).getItem() == Items.BONE_MEAL) {
            return ActionResultType.PASS;
        }
        else if (age > 1) {
            int j = 1 + worldIn.rand.nextInt(2);
            spawnAsEntity(worldIn, pos, new ItemStack(AstralItems.SNOWBERRY.get().asItem(), j + (readyToHarvest ? 1 : 0)));
            worldIn.playSound(null, pos, SoundEvents.ITEM_SWEET_BERRIES_PICK_FROM_BUSH, SoundCategory.BLOCKS, 1.0F, 0.8F + worldIn.rand.nextFloat() * 0.4F);
            worldIn.setBlockState(pos, state.with(AGE, 1), 2);
            return ActionResultType.SUCCESS;
        }
        else {
            return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        int i = state.get(AGE);
        if (i < 3 && worldIn.getLightSubtracted(pos.up(), 0) >= 9 && ForgeHooks.onCropsGrowPre(worldIn, pos, state, rand.nextInt(5) == 0)) {
            worldIn.setBlockState(pos, state.with(AGE, i + 1), 2);
            spreadSnow(worldIn, pos, rand);
            ForgeHooks.onCropsGrowPost(worldIn, pos, state);
        }
    }

    public void spreadSnow(ServerWorld worldIn, BlockPos pos, Random rand) {
        if (rand.nextInt(AstralConfig.getWorldgenSettings().snowberrySpreadSnowChance.get()) == 0) {
            BlockPos.getAllInBox(pos.add(-1, 0, -1), pos.add(1, 0, 1))
                    .filter(blockPos -> isBlockAirOrSnow(blockPos, worldIn))
                    .map(blockPos -> getPosSnowLevelPair(blockPos.toImmutable(), worldIn))
                    .min(Comparator.comparing(Pair::getRight))
                    .ifPresent(blockPosIntegerPair -> {
                        final BlockState blockState = worldIn.getBlockState(blockPosIntegerPair.getLeft());
                        if (blockState.getBlock() == Blocks.AIR) {
                            worldIn.setBlockState(blockPosIntegerPair.getLeft(), Blocks.SNOW.getDefaultState(), 2);
                        }
                        else if (blockState.getBlock() == Blocks.SNOW) {
                            worldIn.setBlockState(blockPosIntegerPair.getLeft(), blockState.with(SnowBlock.LAYERS, blockState.get(SnowBlock.LAYERS) + 1), 2);
                        }
                    });
        }
    }

    private boolean isBlockAirOrSnow(BlockPos blockPos, ServerWorld worldIn) {
        BlockState state = worldIn.getBlockState(blockPos);
        return Blocks.SNOW.getDefaultState().isValidPosition(worldIn, blockPos) && (state.getBlock() == Blocks.SNOW ? state.get(SnowBlock.LAYERS) < 8 : state.getBlock() == Blocks.AIR);
    }

    private Pair<BlockPos, Integer> getPosSnowLevelPair(BlockPos pos, ServerWorld worldIn) {
        if (worldIn.getBlockState(pos).getBlock() == Blocks.AIR) {
            return Pair.of(pos, 0);
        }
        else if (worldIn.getBlockState(pos).getBlock() == Blocks.SNOW) {
            return Pair.of(pos, worldIn.getBlockState(pos).get(SnowBlock.LAYERS));
        }
        return Pair.of(pos, -1);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void grow(ServerWorld worldIn, Random rand, BlockPos pos, BlockState state) {
        super.grow(worldIn, rand, pos, state);
        spreadSnow(worldIn, pos, rand);
    }
}
