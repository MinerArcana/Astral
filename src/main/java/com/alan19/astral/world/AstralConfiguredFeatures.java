package com.alan19.astral.world;

import com.alan19.astral.Astral;
import com.alan19.astral.blocks.AstralBlocks;
import com.alan19.astral.configs.AstralConfig;
import com.alan19.astral.configs.WorldgenSettings;
import com.alan19.astral.world.features.SnowberryFeatureConfig;
import com.alan19.astral.world.trees.EtherealTree;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.Features;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.blockplacers.SimpleBlockPlacer;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraftforge.common.util.NonNullLazy;

public class AstralConfiguredFeatures {
    static final WorldgenSettings worldgenSettings = AstralConfig.getWorldgenSettings();

    // Configured features
    public static final ConfiguredFeature<?, ?> CONFIGURED_FEVERWEED = Feature.RANDOM_PATCH.configured(new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(AstralBlocks.FEVERWEED_BLOCK.get().defaultBlockState()), SimpleBlockPlacer.INSTANCE)
                    .tries(worldgenSettings.feverweedMaxTries.get())
                    .xspread(worldgenSettings.feverweedPatchDistribution.get())
                    .zspread(worldgenSettings.feverweedPatchDistribution.get()).build())
            .decorated(Features.Decorators.ADD_32.decorated(Features.Decorators.HEIGHTMAP_SQUARE))
            .chance(worldgenSettings.feverweedPatchSpawnRate.get());

    public static final ConfiguredFeature<?, ?> CONFIGURED_SNOWBERRY = AstralFeatures.SNOWBERRY_FEATURE
            .get()
            .configured(new SnowberryFeatureConfig(worldgenSettings.snowberryMinPatchSize.get(), worldgenSettings.snowberryMaxPatchSize.get(), worldgenSettings.snowberryPatchSpawnRate.get(), 60));
    public static final NonNullLazy<ConfiguredFeature<?, ?>> CONFIGURED_ETHEREAL_TREE = () -> AstralFeatures.ETHEREAL_TREE.get().configured(EtherealTree.ETHEREAL_TREE_CONFIG.get()).squared();

    // Configured structures
    public static final ConfiguredStructureFeature<?, ?> CONFIGURED_ETHERIC_ISLE = AstralStructures.ETHERIC_ISLE.get().configured(FeatureConfiguration.NONE);

    public static void registerConfiguredFeatures() {
        final Registry<ConfiguredFeature<?, ?>> featureRegistry = BuiltinRegistries.CONFIGURED_FEATURE;
        final Registry<ConfiguredStructureFeature<?, ?>> structureRegistry = BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE;

        Registry.register(featureRegistry, new ResourceLocation(Astral.MOD_ID, "feverweed"), CONFIGURED_FEVERWEED);
        Registry.register(featureRegistry, new ResourceLocation(Astral.MOD_ID, "snowberry"), CONFIGURED_SNOWBERRY);
        Registry.register(featureRegistry, new ResourceLocation(Astral.MOD_ID, "ethereal_tree"), CONFIGURED_ETHEREAL_TREE.get());

        Registry.register(structureRegistry, new ResourceLocation(Astral.MOD_ID, "etheric_isle"), CONFIGURED_ETHERIC_ISLE);
    }
}
