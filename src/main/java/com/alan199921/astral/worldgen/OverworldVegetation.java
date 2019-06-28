package com.alan199921.astral.worldgen;

import com.alan199921.blocks.ModBlocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.BushConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.GrassFeatureConfig;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.Placement;

public class OverworldVegetation {
    public static void addOverworldVegetation() {
        for (Biome biome : Biome.BIOMES) {
            generateSnowberries(biome);
            generateFeverweed(biome);
        }
    }

    private static void generateFeverweed(Biome biome) {
        if (biome.getCategory() == Biome.Category.JUNGLE){
            biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(Feature.GRASS, new GrassFeatureConfig(ModBlocks.feverweedBlock.getDefaultState()), Placement.CHANCE_HEIGHTMAP_DOUBLE, new ChanceConfig(20)));
        }
    }

    private static void generateSnowberries(Biome biome) {
        if (biome.getCategory() == Biome.Category.TAIGA) {
            biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(Feature.BUSH, new BushConfig(ModBlocks.snowberryBush.getDefaultState()), Placement.CHANCE_HEIGHTMAP_DOUBLE, new ChanceConfig(20)));
        }
    }
}
