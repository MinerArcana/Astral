package com.alan19.astral.world.biome;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;

public class PsyscapeBiome extends Biome {
    protected PsyscapeBiome() {
        super(new Biome.Builder()
                .category(Category.NONE)
                .surfaceBuilder(SurfaceBuilder.NOPE, SurfaceBuilder.AIR_CONFIG)
                .precipitation(RainType.NONE)
                .depth(0.1F)
                .scale(0.2F)
                .temperature(0.7F)
                .downfall(0.8F)
                .waterColor(4159204)
                .waterFogColor(329011)
                .parent(null));
    }
}
