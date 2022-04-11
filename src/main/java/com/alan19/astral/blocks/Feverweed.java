package com.alan19.astral.blocks;

import com.alan19.astral.tags.AstralTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

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
    public void tick(@Nonnull BlockState state, @Nonnull ServerLevel worldIn, @Nonnull BlockPos pos, Random random) {
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
    protected boolean mayPlaceOn(BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos) {
        return state.isSolidRender(worldIn, pos);
    }

    //Feverweed is sustained by mycelium and podzol
    @Override
    public boolean canSurvive(@Nonnull BlockState state, LevelReader worldIn, BlockPos pos) {
        BlockPos blockpos = pos.below();
        BlockState blockstate = worldIn.getBlockState(blockpos);
        return state.is(AstralTags.FEVERWEED_SUSTAIN) || blockstate.canSustainPlant(worldIn, blockpos, Direction.UP, this);
    }
}
