package com.alan199921.astral.world.trees;

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
    protected boolean func_225557_a_(IWorldGenerationReader generationReader, Random rand, BlockPos blockPos, Set<BlockPos> blockPosSet, Set<BlockPos> blockPosSet1, MutableBoundingBox mutableBoundingBox, EtherealTreeConfig treeConfig) {
        if (!(generationReader instanceof IWorld)) {
            return false;
        }
        int height = treeConfig.baseHeight + rand.nextInt(3);


        placeTrunk(generationReader, rand, blockPos, blockPosSet, mutableBoundingBox, treeConfig, height);
        placeCanopy(generationReader, rand, height, blockPos, blockPosSet, mutableBoundingBox, treeConfig);
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
        for (int i = 0; i < 4; i++) {
            this.placeDiamondLayer(worldIn, randomIn, i + 1, blockPos.down(i), blockPosSet, mutableBoundingBoxIn, treeFeatureConfigIn);
        }

        blockPos = blockPos.down(3);

        this.placeAir(worldIn, blockPos.add(+4, 0, 0), blockPosSet, mutableBoundingBoxIn);
        this.placeAir(worldIn, blockPos.add(-4, 0, 0), blockPosSet, mutableBoundingBoxIn);
        this.placeAir(worldIn, blockPos.add(0, 0, +4), blockPosSet, mutableBoundingBoxIn);
        this.placeAir(worldIn, blockPos.add(0, 0, -4), blockPosSet, mutableBoundingBoxIn);

        //Drippers
        // stuck with only one block down because of leaf decay distance
        blockPos = blockPos.down();
        this.func_227219_b_(worldIn, randomIn, blockPos.add(+3, 0, 0), blockPosSet, mutableBoundingBoxIn, treeFeatureConfigIn);
        this.func_227219_b_(worldIn, randomIn, blockPos.add(-3, 0, 0), blockPosSet, mutableBoundingBoxIn, treeFeatureConfigIn);
        this.func_227219_b_(worldIn, randomIn, blockPos.add(0, 0, -3), blockPosSet, mutableBoundingBoxIn, treeFeatureConfigIn);
        this.func_227219_b_(worldIn, randomIn, blockPos.add(0, 0, +3), blockPosSet, mutableBoundingBoxIn, treeFeatureConfigIn);

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
