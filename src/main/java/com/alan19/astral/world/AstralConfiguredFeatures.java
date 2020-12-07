package com.alan19.astral.world;

import com.alan19.astral.Astral;
import com.alan19.astral.configs.AstralConfig;
import com.alan19.astral.world.features.FeverweedFeatureConfig;
import com.alan19.astral.world.features.SnowberryFeatureConfig;
import com.alan19.astral.world.trees.EtherealTree;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

import java.util.function.Supplier;

public class AstralConfiguredFeatures {
    static final AstralConfig.WorldgenSettings worldgenSettings = AstralConfig.getWorldgenSettings();

    // Configured features
    public static final Supplier<ConfiguredFeature<?, ?>> CONFIGURED_FEVERWEED = () -> AstralFeatures.FEVERWEED_FEATURE.get().withConfiguration(new FeverweedFeatureConfig(worldgenSettings.getFeverweedMinPatchSize(), worldgenSettings.getFeverweedMaxPatchSize(), worldgenSettings.getFeverweedPatchSpawnRate(), worldgenSettings.getFeverweedPatchSpawnRate(), worldgenSettings.getFeverweedPatchDistribution()));
    public static final Supplier<ConfiguredFeature<?, ?>> CONFIGURED_SNOWBERRY = () -> AstralFeatures.SNOWBERRY_FEATURE.get().withConfiguration(new SnowberryFeatureConfig(worldgenSettings.getSnowberryMinPatchSize(), worldgenSettings.getSnowberryMaxPatchSize(), worldgenSettings.getSnowberryPatchSpawnRate(), 60));
    public static final Supplier<ConfiguredFeature<?, ?>> CONFIGURED_ETHEREAL_TREE = () -> AstralFeatures.ETHEREAL_TREE.get().withConfiguration(EtherealTree.ETHEREAL_TREE_CONFIG.get()).square();

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
