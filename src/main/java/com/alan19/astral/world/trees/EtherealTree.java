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
import java.util.function.Supplier;

public class EtherealTree extends Tree {

    public static final Supplier<TreeFeatureConfig> ETHEREAL_TREE_CONFIG = () -> new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(AstralBlocks.ETHEREAL_LOG.get().getDefaultState()), new SimpleBlockStateProvider(AstralBlocks.ETHEREAL_LEAVES.get().getDefaultState()), new BlobFoliagePlacer(2, 0)).baseHeight(5).heightRandA(2).foliageHeight(3).ignoreVines().setSapling(AstralBlocks.ETHEREAL_SAPLING.get()).build();

    @Nullable
    @Override
    protected ConfiguredFeature<TreeFeatureConfig, ?> getTreeFeature(@Nonnull Random randomIn, boolean p_225546_2_) {
        return Feature.NORMAL_TREE.withConfiguration(ETHEREAL_TREE_CONFIG.get());
    }

    @Override
    public boolean place(@Nonnull IWorld worldIn, @Nonnull ChunkGenerator<?> chunkGeneratorIn, @Nonnull BlockPos blockPosIn, @Nonnull BlockState blockStateIn, @Nonnull Random randomIn) {
        ConfiguredFeature<TreeFeatureConfig, ?> configuredEtherealTree = this.getTreeFeature(randomIn, true);
        if (configuredEtherealTree == null) {
            return false;
        }
        else {
            worldIn.setBlockState(blockPosIn, Blocks.AIR.getDefaultState(), 4);
            if (configuredEtherealTree.place(worldIn, chunkGeneratorIn, randomIn, blockPosIn)) {
                return true;
            }
            else {
                worldIn.setBlockState(blockPosIn, blockStateIn, 4);
                return false;
            }
        }
    }

}
