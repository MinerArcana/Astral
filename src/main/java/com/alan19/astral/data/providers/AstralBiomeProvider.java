package com.alan19.astral.data.providers;

import net.minecraft.data.BiomeProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;

public class AstralBiomeProvider extends BiomeProvider {
    public AstralBiomeProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    public void run(DirectoryCache cache) {
        super.run(cache);
    }
}
