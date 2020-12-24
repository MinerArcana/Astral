package com.alan19.astral.world;

import com.alan19.astral.Astral;
import com.alan19.astral.blocks.AstralBlocks;
import com.alan19.astral.configs.AstralConfig;
import com.alan19.astral.configs.WorldgenSettings;
import com.alan19.astral.world.features.SnowberryFeatureConfig;
import com.alan19.astral.world.trees.EtherealTree;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraftforge.common.util.NonNullLazy;

import java.util.function.Supplier;

public class AstralConfiguredFeatures {
    static final WorldgenSettings worldgenSettings = AstralConfig.getWorldgenSettings();

    // Configured features
    public static final NonNullLazy<ConfiguredFeature<?, ?>> CONFIGURED_FEVERWEED = () -> Feature.RANDOM_PATCH.withConfiguration(new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(AstralBlocks.FEVERWEED_BLOCK.get().getDefaultState()), SimpleBlockPlacer.PLACER).tries(worldgenSettings.feverweedMaxTries.get()).xSpread(worldgenSettings.feverweedPatchDistribution.get()).zSpread(worldgenSettings.feverweedPatchDistribution.get()).build()).withPlacement(Features.Placements.VEGETATION_PLACEMENT.withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT)).chance(worldgenSettings.feverweedPatchSpawnRate.get());
    public static final NonNullLazy<ConfiguredFeature<?, ?>> CONFIGURED_SNOWBERRY = () -> AstralFeatures.SNOWBERRY_FEATURE.get().withConfiguration(new SnowberryFeatureConfig(worldgenSettings.snowberryMinPatchSize.get(), worldgenSettings.snowberryMaxPatchSize.get(), worldgenSettings.snowberryPatchSpawnRate.get(), 60));
    public static final NonNullLazy<ConfiguredFeature<?, ?>> CONFIGURED_ETHEREAL_TREE = () -> AstralFeatures.ETHEREAL_TREE.get().withConfiguration(EtherealTree.ETHEREAL_TREE_CONFIG.get()).square();

    // Configured structures
    public static final Supplier<StructureFeature<?, ?>> CONFIGURED_ETHERIC_ISLE = () -> AstralStructures.ETHERIC_ISLE.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG);

    public static void registerConfiguredFeatures() {
        final Registry<ConfiguredFeature<?, ?>> featureRegistry = WorldGenRegistries.CONFIGURED_FEATURE;
        final Registry<StructureFeature<?, ?>> structureRegistry = WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE;

        Registry.register(featureRegistry, new ResourceLocation(Astral.MOD_ID, "feverweed"), CONFIGURED_FEVERWEED.get());
        Registry.register(featureRegistry, new ResourceLocation(Astral.MOD_ID, "snowberry"), CONFIGURED_SNOWBERRY.get());
        Registry.register(featureRegistry, new ResourceLocation(Astral.MOD_ID, "ethereal_tree"), CONFIGURED_ETHEREAL_TREE.get());

        Registry.register(structureRegistry, new ResourceLocation(Astral.MOD_ID, "etheric_isle"), CONFIGURED_ETHERIC_ISLE.get());
    }
}
