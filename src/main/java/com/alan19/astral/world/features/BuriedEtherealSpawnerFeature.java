package com.alan19.astral.world.features;

import com.alan19.astral.blocks.AstralBlocks;
import com.alan19.astral.blocks.tileentities.EtherealMobSpawnerTileEntity;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.spawner.AbstractSpawner;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public class BuriedEtherealSpawnerFeature extends Feature<BuriedEtherealSpawnerConfig> {
    public BuriedEtherealSpawnerFeature(Codec<BuriedEtherealSpawnerConfig> codec) {
        super(codec);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, BuriedEtherealSpawnerConfig config) {
        BlockState blockstate = AstralBlocks.ETHEREAL_SPAWNER.get().getDefaultState();

        int i = 0;
        BlockPos.Mutable mutablePos = new BlockPos.Mutable();

        for (int j = 0; j < config.tryCount && i < config.size; ++j) {
            mutablePos.setAndOffset(pos, rand.nextInt(config.xSpread + 1) - rand.nextInt(config.xSpread + 1), rand.nextInt(config.ySpread + 1) - rand.nextInt(config.ySpread + 1), rand.nextInt(config.zSpread + 1) - rand.nextInt(config.zSpread + 1));
            mutablePos.down();
            if (config.target.test(reader.getBlockState(mutablePos), rand)) {
                reader.setBlockState(mutablePos, blockstate, 2);
                final TileEntity type = reader.getTileEntity(mutablePos);
                if (type instanceof EtherealMobSpawnerTileEntity) {
                    final AbstractSpawner spawnerBaseLogic = ((EtherealMobSpawnerTileEntity) type).getSpawnerBaseLogic();
                    spawnerBaseLogic.read(config.spawnerNBT);
                }
                i++;
            }
        }
        return i > 0;
    }
}
