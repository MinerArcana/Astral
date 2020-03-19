package com.alan199921.astral.worldgen;

import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import net.minecraft.world.gen.feature.IFeatureConfig;

import javax.annotation.Nonnull;

public class SnowberryFeatureConfig implements IFeatureConfig {
    @Nonnull
    @Override
    public <T> Dynamic<T> serialize(@Nonnull DynamicOps<T> ops) {
        return new Dynamic<>(ops);
    }

    public int getMinPatchSize() {
        return 2;
    }

    public int getMaxPatchSize() {
        return 5;
    }

    public int getPatchChance() {
        return 25;
    }

    public static SnowberryFeatureConfig deserialize(Dynamic<?> dynamic) {
        return new SnowberryFeatureConfig();
    }
}
