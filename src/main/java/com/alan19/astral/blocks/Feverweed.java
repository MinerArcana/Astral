package com.alan19.astral.blocks;

import com.alan19.astral.tags.AstralTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BushBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import java.util.Random;

public class Feverweed extends BushBlock {
    public Feverweed() {
        super(Properties.create(Material.PLANTS)
                .sound(SoundType.PLANT)
                .tickRandomly()
                .doesNotBlockMovement()
                .hardnessAndResistance(0f)
        );
    }

    @Nonnull
    @Override
    public OffsetType getOffsetType() {
        return OffsetType.XYZ;
    }

    //Mushroom spread code but ignores light levels
    @Override
    public void tick(@Nonnull BlockState state, @Nonnull ServerWorld worldIn, @Nonnull BlockPos pos, Random random) {
        if (random.nextInt(25) == 0) {
            //If there are more than 5 blocks of Feverweed in a 9 * 2 * 9 cube centered on the block, do nothing
            int i = 5;

            for (BlockPos blockpos : BlockPos.getAllInBoxMutable(pos.add(-4, -1, -4), pos.add(4, 1, 4))) {
                if (worldIn.getBlockState(blockpos).getBlock() == this) {
                    i--;
                    if (i <= 0) {
                        return;
                    }
                }
            }

            BlockPos blockpos1 = pos.add(random.nextInt(3) - 1, random.nextInt(2) - random.nextInt(2), random.nextInt(3) - 1);

            for (int k = 0; k < 4; ++k) {
                if (worldIn.isAirBlock(blockpos1) && state.isValidPosition(worldIn, blockpos1)) {
                    pos = blockpos1;
                }

                blockpos1 = pos.add(random.nextInt(3) - 1, random.nextInt(2) - random.nextInt(2), random.nextInt(3) - 1);
            }

            if (worldIn.isAirBlock(blockpos1) && state.isValidPosition(worldIn, blockpos1)) {
                worldIn.setBlockState(blockpos1, state, 2);
            }
        }
    }

    @Override
    protected boolean isValidGround(BlockState state, @Nonnull IBlockReader worldIn, @Nonnull BlockPos pos) {
        return state.isOpaqueCube(worldIn, pos);
    }

    //Feverweed is sustained by mycelium and podzol
    @Override
    public boolean isValidPosition(@Nonnull BlockState state, IWorldReader worldIn, BlockPos pos) {
        BlockPos blockpos = pos.down();
        BlockState blockstate = worldIn.getBlockState(blockpos);
        Block block = blockstate.getBlock();
        return AstralTags.FEVERWEED_SUSTAIN.contains(block) || blockstate.canSustainPlant(worldIn, blockpos, net.minecraft.util.Direction.UP, this);
    }
}
