package com.alan19.astral.world.trees;

import com.alan19.astral.blocks.AstralBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.trees.Tree;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class EtherealTree extends Tree {

    @Nullable
    @Override
    protected ConfiguredFeature<TreeFeatureConfig, ?> getTreeFeature(@Nonnull Random randomIn, boolean p_225546_2_) {
        return Feature.NORMAL_TREE.withConfiguration(new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(AstralBlocks.ETHEREAL_WOOD.get().getDefaultState()), new SimpleBlockStateProvider(AstralBlocks.ETHEREAL_LEAVES.get().getDefaultState()), new BlobFoliagePlacer(2, 0))
                .baseHeight(5)
                .heightRandA(1)
                .foliageHeight(3)
                .foliageHeightRandom(1)
                .ignoreVines()
                .setSapling(AstralBlocks.ETHEREAL_SAPLING.get()).build());
    }

    @Override
    public boolean func_225545_a_(@Nonnull IWorld world, @Nonnull ChunkGenerator<?> chunkGenerator, @Nonnull BlockPos blockPos, @Nonnull BlockState blockState, @Nonnull Random random) {
        ConfiguredFeature<TreeFeatureConfig, ?> configuredEtherealTree = this.getTreeFeature(random, true);
        if (configuredEtherealTree == null) {
            return false;
        }
        else {
            world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), 4);
            if (configuredEtherealTree.place(world, chunkGenerator, random, blockPos)) {
                return true;
            }
            else {
                world.setBlockState(blockPos, blockState, 4);
                return false;
            }
        }
    }
}
