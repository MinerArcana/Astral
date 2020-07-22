package com.alan19.astral.world.trees;

import com.mojang.datafixers.Dynamic;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;

import java.util.function.Function;

public class EtherealTreeFeature extends TreeFeature {

    public EtherealTreeFeature(Function<Dynamic<?>, ? extends TreeFeatureConfig> config) {
        super(config);
    }

}
