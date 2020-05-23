package com.alan19.astral.world.biome;

import net.minecraft.world.biome.Biome;

public class PsyscapeBiome extends Biome {
    protected PsyscapeBiome() {
        super(new Biome.Builder().category(Category.NONE).precipitation(RainType.NONE));
    }
}
