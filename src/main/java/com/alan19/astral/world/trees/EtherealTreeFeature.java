package com.alan19.astral.world.trees;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.AbstractTreeFeature;

import java.util.Random;
import java.util.Set;
import java.util.function.Function;

public class EtherealTreeFeature extends AbstractTreeFeature<EtherealTreeConfig> {

    public EtherealTreeFeature(Function<Dynamic<?>, ? extends EtherealTreeConfig> config) {
        super(config);
    }

    @Override
    protected boolean place(IWorldGenerationReader generationReader, Random rand, BlockPos positionIn, Set<BlockPos> blockPosSet, Set<BlockPos> blockPosSet1, MutableBoundingBox boundingBoxIn, EtherealTreeConfig configIn) {
        if (!(generationReader instanceof IWorld)) {
            return false;
        }
        int height = configIn.baseHeight + (rand.nextInt(2) * (rand.nextInt(2) - rand.nextInt(3)));
        placeTrunk(generationReader, rand, positionIn, blockPosSet, boundingBoxIn, configIn, height);
        placeCanopy(generationReader, rand, height, positionIn, blockPosSet, boundingBoxIn, configIn);
        return true;
    }

    private void placeTrunk(IWorldGenerationReader generationReader, Random rand, BlockPos blockPos, Set<BlockPos> blockPosSet, MutableBoundingBox mutableBoundingBox, EtherealTreeConfig treeConfig, int height) {
        while (height > 0) {
            this.func_227216_a_(generationReader, rand, blockPos, blockPosSet, mutableBoundingBox, treeConfig);

            blockPos = blockPos.up();
            height--;
        }
    }

    protected void placeCanopy(IWorldGenerationReader worldIn, Random randomIn, int treeHeight, BlockPos blockPos, Set<BlockPos> blockPosSet, MutableBoundingBox mutableBoundingBoxIn, EtherealTreeConfig treeFeatureConfigIn) {
        blockPos = blockPos.up(treeHeight);
        int range = 1;
        final int canopyHeight = 3;
        for (int i = 0; i < canopyHeight; i++) {
            //Increase range 1/3 of the time when it's not the first layer and the range hasn't increased, or on the last layer if the range hasn't increased
            if ((range <= 3 && i == canopyHeight - 1 || range == 1 && i != 0 && randomIn.nextInt(3) == 1)) {
                range++;
            }
            this.placeDiamondLayer(worldIn, randomIn, range, blockPos.down(i), blockPosSet, mutableBoundingBoxIn, treeFeatureConfigIn);
        }

        blockPos = blockPos.down(3);

        this.placeAir(worldIn, blockPos.add(+4, 0, 0), blockPosSet, mutableBoundingBoxIn);
        this.placeAir(worldIn, blockPos.add(-4, 0, 0), blockPosSet, mutableBoundingBoxIn);
        this.placeAir(worldIn, blockPos.add(0, 0, +4), blockPosSet, mutableBoundingBoxIn);
        this.placeAir(worldIn, blockPos.add(0, 0, -4), blockPosSet, mutableBoundingBoxIn);

    }

    private void placeDiamondLayer(IWorldGenerationReader worldIn, Random randomIn, int range, BlockPos blockPos, Set<BlockPos> blockPosSet, MutableBoundingBox mutableBoundingBoxIn, EtherealTreeConfig treeFeatureConfigIn) {
        for (int x = -range; x <= range; x++) {
            for (int z = -range; z <= range; z++) {
                if (Math.abs(x) + Math.abs(z) <= range) {
                    BlockPos blockpos = blockPos.add(x, 0, z);
                    this.func_227219_b_(worldIn, randomIn, blockpos, blockPosSet, mutableBoundingBoxIn, treeFeatureConfigIn);
                }
            }
        }
    }


    protected void placeAir(IWorldGenerationReader worldIn, BlockPos blockPos, Set<BlockPos> blockPosSet, MutableBoundingBox mutableBoundingBoxIn) {
        if (isAirOrLeaves(worldIn, blockPos)) {
            this.func_227217_a_(worldIn, blockPos, Blocks.AIR.getDefaultState(), mutableBoundingBoxIn);
            blockPosSet.add(blockPos.toImmutable());
        }
    }
}
