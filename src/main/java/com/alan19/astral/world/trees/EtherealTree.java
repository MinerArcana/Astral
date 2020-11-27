package com.alan19.astral.world.trees;

import com.alan19.astral.blocks.AstralBlocks;
import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.trunkplacer.StraightTrunkPlacer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Supplier;

public class EtherealTree extends Tree {

    public static final Supplier<BaseTreeFeatureConfig> ETHEREAL_TREE_CONFIG = () -> {
        final BaseTreeFeatureConfig config = Feature.TREE.withConfiguration(new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(AstralBlocks.ETHEREAL_LOG.get().getDefaultState()), new SimpleBlockStateProvider(AstralBlocks.ETHEREAL_LEAVES.get().getDefaultState()), new BlobFoliagePlacer(FeatureSpread.func_242252_a(2), FeatureSpread.func_242252_a(0), 3), new StraightTrunkPlacer(5, 2, 0), new TwoLayerFeature(2, 0, 1)).setIgnoreVines().build()).getConfig();
        config.forcePlacement();
        return config;
    };

    @Nullable
    @Override
    protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getTreeFeature(@Nonnull Random randomIn, boolean p_225546_2_) {
        return Feature.TREE.withConfiguration(ETHEREAL_TREE_CONFIG.get());
    }
}
