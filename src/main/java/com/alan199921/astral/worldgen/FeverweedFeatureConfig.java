package com.alan199921.astral.worldgen;

import com.alan199921.astral.configs.AstralConfig;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import net.minecraft.world.gen.feature.IFeatureConfig;

import javax.annotation.Nonnull;

public class FeverweedFeatureConfig implements IFeatureConfig {

    private final AstralConfig.WorldgenSettings worldgenSettings = AstralConfig.getWorldgenSettings();

    @Nonnull
    @Override
    public <T> Dynamic<T> serialize(@Nonnull DynamicOps<T> ops) {
        return new Dynamic<>(ops);
    }

    public int getMinPatchSize() {
        return worldgenSettings.getFeverweedMinPatchSize();
    }

    public int getMaxPatchSize() {
        return worldgenSettings.getFeverweedMaxPatchSize();
    }

    public int getPatchChance() {
        return worldgenSettings.getFeverweedPatchSpawnRate();
    }

    public static FeverweedFeatureConfig deserialize(Dynamic<?> dynamic) {
        return new FeverweedFeatureConfig();
    }

    public int getMaxTries() {
        return worldgenSettings.getFeverweedMaxTries();
    }

    public int getDistribution() {
        return (int) Math.max(worldgenSettings.getFeverweedPatchDistribution(), Math.ceil(Math.sqrt(getMaxPatchSize())));
    }
}
