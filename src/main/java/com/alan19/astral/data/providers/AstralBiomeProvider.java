package com.alan19.astral.data.providers;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.HashCache;
import net.minecraft.data.worldgen.biome.BiomeReport;

public class AstralBiomeProvider extends BiomeReport {
    public AstralBiomeProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    public void run(HashCache cache) {
        super.run(cache);
    }
}
