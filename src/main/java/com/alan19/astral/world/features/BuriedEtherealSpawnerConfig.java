package com.alan19.astral.world.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.template.RuleTest;

public class BuriedEtherealSpawnerConfig implements IFeatureConfig {
    public static final Codec<BuriedEtherealSpawnerConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            RuleTest.CODEC.fieldOf("target").forGetter(config -> config.target),
            Codec.intRange(0, 64).fieldOf("size").forGetter(config -> config.size),
            CompoundNBT.CODEC.fieldOf("spawnerNBT").forGetter(config -> config.spawnerNBT),
            Codec.INT.fieldOf("xspread").orElse(7).forGetter(config -> config.xSpread),
            Codec.INT.fieldOf("yspread").orElse(3).forGetter(config -> config.ySpread),
            Codec.INT.fieldOf("zspread").orElse(7).forGetter(config -> config.zSpread),
            Codec.INT.fieldOf("tries").orElse(128).forGetter(config -> config.tryCount))
            .apply(instance, BuriedEtherealSpawnerConfig::new));

    public final RuleTest target;
    public final CompoundNBT spawnerNBT;
    public final int xSpread;
    public final int ySpread;
    public final int zSpread;
    public final int size;
    public final int tryCount;

    public BuriedEtherealSpawnerConfig(RuleTest ruleTest, int size, CompoundNBT spawnerNBT, int xSpread, int ySpread, int zSpread, int tryCount) {
        this.size = size;
        this.target = ruleTest;
        this.spawnerNBT = spawnerNBT;
        this.xSpread = xSpread;
        this.ySpread = ySpread;
        this.zSpread = zSpread;
        this.tryCount = tryCount;
    }

}
