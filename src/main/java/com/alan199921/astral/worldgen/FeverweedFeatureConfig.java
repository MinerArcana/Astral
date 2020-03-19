package com.alan199921.astral.worldgen;

import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import net.minecraft.world.gen.feature.IFeatureConfig;

import javax.annotation.Nonnull;

public class FeverweedFeatureConfig implements IFeatureConfig {
    @Nonnull
    @Override
    public <T> Dynamic<T> serialize(@Nonnull DynamicOps<T> ops) {
        return new Dynamic<>(ops);
    }

    public int getMinPatchSize() {
        return 4;
    }

    public int getMaxPatchSize() {
        return 10;
    }

    public int getPatchChance() {
        return 10;
    }

    public static FeverweedFeatureConfig deserialize(Dynamic<?> dynamic) {
        return new FeverweedFeatureConfig();
    }

    public int getMaxTries() {
        return 40;
    }

    public int getDistribution() {
        return (int) Math.max(5, Math.ceil(Math.sqrt(getMaxPatchSize())));
    }
}
