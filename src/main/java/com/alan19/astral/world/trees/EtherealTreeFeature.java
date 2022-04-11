package com.alan19.astral.world.trees;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;

public class EtherealTreeFeature extends TreeFeature {
    public EtherealTreeFeature(Codec<TreeConfiguration> codec) {
        super(codec);
    }
}
