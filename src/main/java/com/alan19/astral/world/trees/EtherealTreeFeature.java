package com.alan19.astral.world.trees;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public class EtherealTreeFeature extends TreeFeature {
    public EtherealTreeFeature(Codec<TreeConfiguration> codec) {
        super(codec);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean place(WorldGenLevel reader, ChunkGenerator generator, Random rand, BlockPos pos, TreeConfiguration config) {
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        for (int i = 0; i < 32; i++) {
            mutablePos.setWithOffset(pos, rand.nextInt(7 + 1) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));
            boolean generated = super.place(reader, generator, rand, mutablePos, config);
            if (generated) {
                return true;
            }
        }
        return false;
    }
}
