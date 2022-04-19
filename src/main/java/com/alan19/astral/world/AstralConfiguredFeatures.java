package com.alan19.astral.world;

import com.alan19.astral.Astral;
import com.alan19.astral.blocks.AstralBlocks;
import com.alan19.astral.configs.AstralConfig;
import com.alan19.astral.configs.WorldgenSettings;
import com.alan19.astral.world.features.SnowberryFeatureConfig;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.RandomSpreadFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.BendingTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;

public class AstralConfiguredFeatures {
    static final WorldgenSettings worldgenSettings = AstralConfig.getWorldgenSettings();

    // Configured features
    public static final Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> CONFIGURED_FEVERWEED = FeatureUtils.register("feverweed", Feature.FLOWER, new RandomPatchConfiguration(worldgenSettings.feverweedMaxTries.get(), worldgenSettings.feverweedPatchDistribution.get(), 2, PlacementUtils.inlinePlaced(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(AstralBlocks.FEVERWEED_BLOCK.get())))));

    public static final Holder<ConfiguredFeature<SnowberryFeatureConfig, ?>> CONFIGURED_SNOWBERRY = FeatureUtils.register("snowberries", AstralFeatures.SNOWBERRY_FEATURE.get(), new SnowberryFeatureConfig(worldgenSettings.snowberryMinPatchSize.get(), worldgenSettings.snowberryMaxPatchSize.get(), worldgenSettings.snowberryPatchSpawnRate.get(), 60));

    public static final Holder<PlacedFeature> PLACED_SNOWBERRY = PlacementUtils.register("snowberry", CONFIGURED_SNOWBERRY, RarityFilter.onAverageOnceEvery(worldgenSettings.snowberryPatchSpawnRate.get()), BiomeFilter.biome());

    public static final Holder<PlacedFeature> PLACED_FEVERWEED = PlacementUtils.register("feverweed", CONFIGURED_FEVERWEED, RarityFilter.onAverageOnceEvery(worldgenSettings.feverweedPatchSpawnRate.get()), BiomeFilter.biome());

    public static final Holder<ConfiguredFeature<TreeConfiguration, ?>> CONFIGURED_ETHEREAL_TREE = FeatureUtils.register("ethereal_tree", AstralFeatures.ETHEREAL_TREE.get(), new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(AstralBlocks.ETHEREAL_WOOD.get().defaultBlockState()),
            new BendingTrunkPlacer(5, 2, 0, 3, UniformInt.of(0, 2)),
            BlockStateProvider.simple(AstralBlocks.ETHEREAL_LEAVES.get().defaultBlockState()),
            new RandomSpreadFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), ConstantInt.of(3), 30),
            new TwoLayersFeatureSize(1, 0, 1))
            .dirt(BlockStateProvider.simple(AstralBlocks.ETHER_DIRT.get().defaultBlockState()))
            .forceDirt()
            .build());

    // Configured structures
//    public static final Holder<ConfiguredStructureFeature<?, ?>> CONFIGURED_ETHERIC_ISLE = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, new ResourceLocation(Astral.MOD_ID, "etheric_isle"), AstralStructures.ETHERIC_ISLE.get().configured(new JigsawConfiguration(), BiomeTags.IS_OCEAN));

    public static void registerConfiguredFeatures() {
        final Registry<ConfiguredFeature<?, ?>> featureRegistry = BuiltinRegistries.CONFIGURED_FEATURE;
        final Registry<ConfiguredStructureFeature<?, ?>> structureRegistry = BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE;

        Registry.register(featureRegistry, new ResourceLocation(Astral.MOD_ID, "feverweed"), CONFIGURED_FEVERWEED.value());
        Registry.register(featureRegistry, new ResourceLocation(Astral.MOD_ID, "snowberry"), CONFIGURED_SNOWBERRY.value());
        Registry.register(featureRegistry, new ResourceLocation(Astral.MOD_ID, "ethereal_tree"), CONFIGURED_ETHEREAL_TREE.value());

//        Registry.register(structureRegistry, new ResourceLocation(Astral.MOD_ID, "etheric_isle"), CONFIGURED_ETHERIC_ISLE);
    }
}
