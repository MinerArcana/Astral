package com.alan19.astral.world.trees;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeature;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public class EtherealTreeFeature extends TreeFeature {
    public EtherealTreeFeature(Codec<BaseTreeFeatureConfig> codec) {
        super(codec);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, BaseTreeFeatureConfig config) {
        BlockPos.Mutable mutablePos = new BlockPos.Mutable();
        for (int i = 0; i < 32; i++) {
            mutablePos.setAndOffset(pos, rand.nextInt(7 + 1) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));
            boolean generated = super.generate(reader, generator, rand, mutablePos, config);
            if (generated) {
                return true;
            }
        }
        return false;
    }
}
