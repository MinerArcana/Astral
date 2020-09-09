package com.alan19.astral.world;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

public class AstralWorld {
    private static final Feature<SnowberryFeatureConfig> SNOWBERRY = new SnowberryFeature(SnowberryFeatureConfig::deserialize);
    private static final Feature<FeverweedFeatureConfig> FEVERWEED = new FeverweedFeature(FeverweedFeatureConfig::deserialize);

    public static void addOverworldVegetation() {
        for (Biome biome : Biome.BIOMES) {
            generateSnowberries(biome);
            generateFeverweed(biome);
        }
        ForgeRegistries.BIOMES.getEntries().stream().map(Map.Entry::getValue).forEach(AstralWorld::addEthericIsles);

    }

    public static void addEthericIsles(Biome biome) {
        if (BiomeDictionary.getBiomes(BiomeDictionary.Type.OCEAN).contains(biome)) {
            biome.addStructure(AstralFeatures.ETHERIC_ISLE_OCEAN.get().withConfiguration(new NoFeatureConfig()));
            biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, AstralFeatures.ETHERIC_ISLE_OCEAN.get().withConfiguration(new NoFeatureConfig()));
        }
        else {
            biome.addStructure(AstralFeatures.ETHERIC_ISLE_LAND.get().withConfiguration(new NoFeatureConfig()));
            biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, AstralFeatures.ETHERIC_ISLE_LAND.get().withConfiguration(new NoFeatureConfig()));

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
