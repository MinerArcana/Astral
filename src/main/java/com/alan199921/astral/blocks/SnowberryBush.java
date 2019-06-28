package com.alan199921.blocks;

import com.alan199921.astral.items.ModItems;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class SnowberryBush extends SweetBerryBushBlock {
    public SnowberryBush() {
        super(Properties.create(Material.PLANTS)
                .sound(SoundType.PLANT)
                .tickRandomly()
                .hardnessAndResistance(0.2f));
        setRegistryName("snowberry_bush");
    }

//    @Override
//    protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
//        Block block = state.getBlock();
//        return block == Blocks.SNOW_BLOCK;
//    }
//
//    @Override
//    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
//        BlockPos blockpos = pos.down();
//        return this.isValidGround(worldIn.getBlockState(blockpos), worldIn, blockpos);
//    }

    @Override
    public ItemStack getItem(IBlockReader worldIn, BlockPos pos, BlockState state) {
        return new ItemStack(ModItems.snowberry.asItem());
    }

    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        int age = state.get(AGE);
        boolean readyToHarvest = age == 3;
        if (!readyToHarvest && player.getHeldItem(handIn).getItem() == Items.BONE_MEAL) {
            return false;
        } else if (age > 1) {
            int j = 1 + worldIn.rand.nextInt(2);
            spawnAsEntity(worldIn, pos, new ItemStack(ModItems.snowberry.asItem(), j + (readyToHarvest ? 1 : 0)));
            worldIn.playSound(null, pos, SoundEvents.ITEM_SWEET_BERRIES_PICK_FROM_BUSH, SoundCategory.BLOCKS, 1.0F, 0.8F + worldIn.rand.nextFloat() * 0.4F);
            worldIn.setBlockState(pos, state.with(AGE, 1), 2);
            return true;
        } else {
            return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
        }
    }
}
