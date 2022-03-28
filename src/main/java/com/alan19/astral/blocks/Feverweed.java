package com.alan19.astral.blocks;

import com.alan19.astral.tags.AstralTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BushBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import java.util.Random;

public class Feverweed extends BushBlock {
    public Feverweed() {
        super(Properties.of(Material.PLANT)
                .sound(SoundType.GRASS)
                .randomTicks()
                .noCollission()
                .strength(0f)
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
            int maxFeverweedInRange = 5;
            for (BlockPos blockpos : BlockPos.betweenClosed(pos.offset(-4, -1, -4), pos.offset(4, 1, 4))) {
                if (worldIn.getBlockState(blockpos).getBlock() == this) {
                    maxFeverweedInRange--;
                    if (maxFeverweedInRange <= 0) {
                        return;
                    }
                }
            }

            BlockPos spreadPos = pos.offset(random.nextInt(3) - 1, random.nextInt(2) - random.nextInt(2), random.nextInt(3) - 1);

            for (int spawnTry = 0; spawnTry < 4; ++spawnTry) {
                if (worldIn.isEmptyBlock(spreadPos) && state.canSurvive(worldIn, spreadPos)) {
                    pos = spreadPos;
                }

                spreadPos = pos.offset(random.nextInt(3) - 1, random.nextInt(2) - random.nextInt(2), random.nextInt(3) - 1);
            }

            if (worldIn.isEmptyBlock(spreadPos) && state.canSurvive(worldIn, spreadPos)) {
                worldIn.setBlock(spreadPos, state, 2);
            }
        }
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, @Nonnull IBlockReader worldIn, @Nonnull BlockPos pos) {
        return state.isSolidRender(worldIn, pos);
    }

    //Feverweed is sustained by mycelium and podzol
    @Override
    public boolean canSurvive(@Nonnull BlockState state, IWorldReader worldIn, BlockPos pos) {
        BlockPos blockpos = pos.below();
        BlockState blockstate = worldIn.getBlockState(blockpos);
        Block block = blockstate.getBlock();
        return AstralTags.FEVERWEED_SUSTAIN.contains(block) || blockstate.canSustainPlant(worldIn, blockpos, Direction.UP, this);
    }
}
