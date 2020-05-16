package com.alan19.astral.world.trees;

import com.alan19.astral.blocks.AstralBlocks;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.Dynamic;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;

public class EtherealTreeConfig extends BaseTreeFeatureConfig {
    public EtherealTreeConfig() {
        super(new SimpleBlockStateProvider(AstralBlocks.ETHEREAL_WOOD.get().getDefaultState()), new SimpleBlockStateProvider(AstralBlocks.ETHEREAL_LEAVES.get().getDefaultState()), ImmutableList.of(), 5);

    }

    public static <T> EtherealTreeConfig deserialize(Dynamic<T> dynamic) {
        return new EtherealTreeConfig();
    }

}
