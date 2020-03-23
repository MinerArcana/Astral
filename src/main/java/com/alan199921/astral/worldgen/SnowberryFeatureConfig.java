package com.alan199921.astral.worldgen;

import com.alan199921.astral.configs.AstralConfig;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import net.minecraft.world.gen.feature.IFeatureConfig;

import javax.annotation.Nonnull;

public class SnowberryFeatureConfig implements IFeatureConfig {

    private final AstralConfig.WorldgenSettings worldgenSettings = AstralConfig.getWorldgenSettings();

    @Nonnull
    @Override
    public <T> Dynamic<T> serialize(@Nonnull DynamicOps<T> ops) {
        return new Dynamic<>(ops);
    }

    public int getMinPatchSize() {
        return worldgenSettings.getSnowberryMinPatchSize();
    }

    public int getMaxPatchSize() {
        return worldgenSettings.getSnowberryMaxPatchSize();
    }

    public int getPatchChance() {
        return worldgenSettings.getSnowberryPatchSpawnRate();
    }

    public static SnowberryFeatureConfig deserialize(Dynamic<?> dynamic) {
        return new SnowberryFeatureConfig();
    }
}
