package com.alan199921.astral.worldgen;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraftforge.common.BiomeManager;

import static com.alan199921.astral.worldgen.AstralFeatures.astralIsland;

public class OverworldVegetation {
    private static final Feature<SnowberryFeatureConfig> SNOWBERRY = new SnowberryFeature(SnowberryFeatureConfig::deserialize);
    private static final Feature<FeverweedFeatureConfig> FEVERWEED = new FeverweedFeature(FeverweedFeatureConfig::deserialize);

    public static void addOverworldVegetation() {
        for (Biome biome : Biome.BIOMES) {
            generateSnowberries(biome);
            generateFeverweed(biome);
        }
        for (Biome biome : BiomeManager.oceanBiomes) {
            biome.addStructure(astralIsland.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
            biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, AstralFeatures.astralIsland.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        }

    }

    private static void generateFeverweed(Biome biome) {
        if (biome.getCategory() == Biome.Category.JUNGLE || (biome.getTempCategory() == Biome.TempCategory.WARM && biome.getPrecipitation() == Biome.RainType.RAIN)) {
            biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, FEVERWEED.withConfiguration(new FeverweedFeatureConfig()));

        }
    }

    private static void generateSnowberries(Biome biome) {
        if (biome.getCategory() == Biome.Category.TAIGA || biome.getCategory() == Biome.Category.ICY) {
            biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, SNOWBERRY.withConfiguration(new SnowberryFeatureConfig()));
        }
    }
}
