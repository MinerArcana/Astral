package com.alan19.astral.world.features;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.MultipleRandomFeatureConfig;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public class GenerateMultipleFeature extends Feature<MultipleRandomFeatureConfig> {
    public GenerateMultipleFeature(Codec<MultipleRandomFeatureConfig> codec) {
        super(codec);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, MultipleRandomFeatureConfig config) {
        final float aFloat = rand.nextFloat();
        final boolean atLeastOneGenerated = config.features.stream()
                .filter(configuredRandomFeatureList -> aFloat < configuredRandomFeatureList.chance)
                .anyMatch(configuredRandomFeatureList -> configuredRandomFeatureList.func_242787_a(reader, generator, rand, pos));
        return atLeastOneGenerated || config.defaultFeature.get().generate(reader, generator, rand, pos);
    }
}
