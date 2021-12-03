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
        final BaseTreeFeatureConfig config = Feature.TREE.configured(new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(AstralBlocks.ETHEREAL_LOG.get().defaultBlockState()), new SimpleBlockStateProvider(AstralBlocks.ETHEREAL_LEAVES.get().defaultBlockState()), new BlobFoliagePlacer(FeatureSpread.fixed(2), FeatureSpread.fixed(0), 3), new StraightTrunkPlacer(5, 2, 0), new TwoLayerFeature(2, 0, 1)).ignoreVines().build()).config();
        config.setFromSapling();
        return config;
    };

    @Nullable
    @Override
    protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getConfiguredFeature(@Nonnull Random randomIn, boolean p_225546_2_) {
        return Feature.TREE.configured(ETHEREAL_TREE_CONFIG.get());
    }
}
