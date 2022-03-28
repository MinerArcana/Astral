package com.alan19.astral.world.features;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredRandomFeatureList;
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
    public boolean place(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, MultipleRandomFeatureConfig config) {
        final float aFloat = rand.nextFloat();
        boolean atLeastOneGenerated = false;
        for (ConfiguredRandomFeatureList configuredRandomFeatureList : config.features) {
            if (aFloat < configuredRandomFeatureList.chance && configuredRandomFeatureList.place(reader, generator, rand, pos)) {
                atLeastOneGenerated = true;
            }
        }
        return atLeastOneGenerated || config.defaultFeature.get().place(reader, generator, rand, pos);
    }
}
