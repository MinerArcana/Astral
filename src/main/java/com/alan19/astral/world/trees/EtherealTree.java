package com.alan19.astral.world.trees;

import com.alan19.astral.blocks.AstralBlocks;
import net.minecraft.util.UniformInt;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Supplier;

public class EtherealTree extends AbstractTreeGrower {

    public static final Supplier<TreeConfiguration> ETHEREAL_TREE_CONFIG = () -> {
        final TreeConfiguration config = Feature.TREE.configured(new TreeConfiguration.TreeConfigurationBuilder(new SimpleStateProvider(AstralBlocks.ETHEREAL_LOG.get().defaultBlockState()), new SimpleStateProvider(AstralBlocks.ETHEREAL_LEAVES.get().defaultBlockState()), new BlobFoliagePlacer(UniformInt.fixed(2), UniformInt.fixed(0), 3), new StraightTrunkPlacer(5, 2, 0), new TwoLayersFeatureSize(2, 0, 1)).ignoreVines().build()).config();
        config.setFromSapling();
        return config;
    };

    @Nullable
    @Override
    protected ConfiguredFeature<TreeConfiguration, ?> getConfiguredFeature(@Nonnull Random randomIn, boolean p_225546_2_) {
        return Feature.TREE.configured(ETHEREAL_TREE_CONFIG.get());
    }
}
