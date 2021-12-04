package com.alan19.astral.world.features;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.WeightedConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public class GenerateMultipleFeature extends Feature<RandomFeatureConfiguration> {
    public GenerateMultipleFeature(Codec<RandomFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean place(WorldGenLevel reader, ChunkGenerator generator, Random rand, BlockPos pos, RandomFeatureConfiguration config) {
        final float aFloat = rand.nextFloat();
        boolean atLeastOneGenerated = false;
        for (WeightedConfiguredFeature configuredRandomFeatureList : config.features) {
            if (aFloat < configuredRandomFeatureList.chance && configuredRandomFeatureList.place(reader, generator, rand, pos)) {
                atLeastOneGenerated = true;
            }
        }
        return atLeastOneGenerated || config.defaultFeature.get().place(reader, generator, rand, pos);
    }
}
