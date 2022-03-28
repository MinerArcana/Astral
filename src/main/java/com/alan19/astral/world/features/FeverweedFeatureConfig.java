package com.alan19.astral.world.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

/**
 * Codec adapted from https://github.com/Vazkii/Botania/blob/62ff10ddca0c6fcf79355daf3e52c7a0dbaefb9e/src/main/java/vazkii/botania/common/world/MysticalFlowerConfig.java#L16
 */
public class FeverweedFeatureConfig implements FeatureConfiguration {
    public static final Codec<FeverweedFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("min_patch_size").forGetter(FeverweedFeatureConfig::getMinPatchSize),
            Codec.INT.fieldOf("max_patch_size").forGetter(FeverweedFeatureConfig::getMaxPatchSize),
            Codec.INT.fieldOf("patch_chance").forGetter(FeverweedFeatureConfig::getPatchChance),
            Codec.INT.fieldOf("max_tries").forGetter(FeverweedFeatureConfig::getMaxTries),
            Codec.INT.fieldOf("distribution_range").forGetter(FeverweedFeatureConfig::getDistribution)
    ).apply(instance, FeverweedFeatureConfig::new));

    private final int minSize;
    private final int maxSize;
    private final int patchChance;
    private final int maxTries;
    private final int distRange;

    public FeverweedFeatureConfig(int minSize, int maxSize, int patchChance, int maxTries, int distRange) {
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.patchChance = patchChance;
        this.maxTries = maxTries;
        this.distRange = distRange;
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

    public int getDistribution() {
        return (int) Math.max(distRange, Math.ceil(Math.sqrt(maxSize)));
    }
}
