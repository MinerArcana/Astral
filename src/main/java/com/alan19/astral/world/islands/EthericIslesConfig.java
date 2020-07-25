package com.alan19.astral.world.islands;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import net.minecraft.world.gen.feature.IFeatureConfig;

import javax.annotation.Nonnull;

public class EthericIslesConfig implements IFeatureConfig {
    private final int separation;
    private final int distance;
    private final boolean ocean;

    public EthericIslesConfig(boolean ocean) {
        this.ocean = ocean;

        if (ocean) {
            separation = 5;
            distance = 6;
        }
        else {
            separation = 10;
            distance = 12;
        }
    }

    public static EthericIslesConfig deserialize(Dynamic<?> dynamic) {
        return new EthericIslesConfig(dynamic.get("ocean").asBoolean(true));
    }

    public int getSeparation() {
        return separation;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    @Nonnull
    public <T> Dynamic<T> serialize(@Nonnull DynamicOps<T> ops) {
        return new Dynamic<>(ops, ops.createMap(ImmutableMap.of(ops.createString("ocean"), ops.createBoolean(ocean))));
    }


}
