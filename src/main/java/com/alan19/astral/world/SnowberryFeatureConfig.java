package com.alan19.astral.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.gen.feature.IFeatureConfig;

public class SnowberryFeatureConfig implements IFeatureConfig {

    public static final Codec<SnowberryFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("min_patch_size").forGetter(SnowberryFeatureConfig::getMinPatchSize),
            Codec.INT.fieldOf("max_patch_size").forGetter(SnowberryFeatureConfig::getMaxPatchSize),
            Codec.INT.fieldOf("patch_chance").forGetter(SnowberryFeatureConfig::getPatchChance),
            Codec.INT.fieldOf("max_tries").forGetter(SnowberryFeatureConfig::getMaxTries)
    ).apply(instance, SnowberryFeatureConfig::new));

    private final int minSize;
    private final int maxSize;
    private final int patchChance;
    private final int maxTries;

    public SnowberryFeatureConfig(int minSize, int maxSize, int patchChance, int maxTries) {
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.patchChance = patchChance;
        this.maxTries = maxTries;
    }

    public int getMinPatchSize() {
        return minSize;
    }

    public int getMaxPatchSize() {
        return maxSize;
    }

    public int getPatchChance() {
        return patchChance;
    }

    public int getMaxTries() {
        return maxTries;
    }
}
