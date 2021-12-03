package com.alan19.astral.world.features;

import com.alan19.astral.blocks.AstralBlocks;
import com.alan19.astral.blocks.tileentities.EtherealMobSpawnerTileEntity;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.spawner.AbstractSpawner;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.Random;

public class BuriedEtherealSpawnerFeature extends Feature<BuriedEtherealSpawnerConfig> {
    public BuriedEtherealSpawnerFeature(Codec<BuriedEtherealSpawnerConfig> codec) {
        super(codec);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean place(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, BuriedEtherealSpawnerConfig config) {
        BlockState blockstate = AstralBlocks.ETHEREAL_SPAWNER.get().defaultBlockState();

        int generatedCount = 0;
        BlockPos.Mutable mutablePos = new BlockPos.Mutable();

        for (int j = 0; j < config.tryCount && generatedCount < config.size; ++j) {
            mutablePos.setWithOffset(pos, rand.nextInt(config.xSpread + 1) - rand.nextInt(config.xSpread + 1), rand.nextInt(config.ySpread + 1) - rand.nextInt(config.ySpread + 1), rand.nextInt(config.zSpread + 1) - rand.nextInt(config.zSpread + 1));
            if (config.target.test(reader.getBlockState(mutablePos), rand) && Arrays.stream(Direction.values()).noneMatch(direction -> reader.isEmptyBlock(mutablePos.relative(direction)))) {
                reader.setBlock(mutablePos, blockstate, 2);
                final TileEntity type = reader.getBlockEntity(mutablePos);
                if (type instanceof EtherealMobSpawnerTileEntity) {
                    final AbstractSpawner spawnerBaseLogic = ((EtherealMobSpawnerTileEntity) type).getSpawnerBaseLogic();
                    spawnerBaseLogic.load(config.spawnerNBT);
                }
                generatedCount++;
            }
        }
        return generatedCount > 0;
    }
}
