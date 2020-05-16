package com.alan19.astral.blocks;

import com.alan19.astral.items.AstralItems;
import com.alan19.astral.tags.AstralTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.SweetBerryBushBlock;
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

import javax.annotation.Nonnull;

public class SnowberryBush extends SweetBerryBushBlock {

    public SnowberryBush() {
        super(Properties.create(Material.PLANTS)
                .sound(SoundType.PLANT)
                .tickRandomly()
                .hardnessAndResistance(0.2f)
                .notSolid());
    }

    @Override
    protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
        Block block = state.getBlock();
        return AstralTags.SNOWBERRY_SUSTAIN.contains(block);
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        BlockPos blockpos = pos.down();
        return this.isValidGround(worldIn.getBlockState(blockpos), worldIn, blockpos);
    }

    @Nonnull
    @Override
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
    public ActionResultType onBlockActivated(BlockState state, @Nonnull World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
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
}
