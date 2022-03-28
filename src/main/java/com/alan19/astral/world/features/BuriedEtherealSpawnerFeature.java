package com.alan19.astral.world.features;

import com.alan19.astral.blocks.AstralBlocks;
import com.alan19.astral.blocks.tileentities.EtherealMobSpawnerTileEntity;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.Random;

public class BuriedEtherealSpawnerFeature extends Feature<BuriedEtherealSpawnerConfig> {
    public BuriedEtherealSpawnerFeature(Codec<BuriedEtherealSpawnerConfig> codec) {
        super(codec);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean place(WorldGenLevel reader, ChunkGenerator generator, Random rand, BlockPos pos, BuriedEtherealSpawnerConfig config) {
        BlockState blockstate = AstralBlocks.ETHEREAL_SPAWNER.get().defaultBlockState();

        int generatedCount = 0;
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        for (int j = 0; j < config.tryCount && generatedCount < config.size; ++j) {
            mutablePos.setWithOffset(pos, rand.nextInt(config.xSpread + 1) - rand.nextInt(config.xSpread + 1), rand.nextInt(config.ySpread + 1) - rand.nextInt(config.ySpread + 1), rand.nextInt(config.zSpread + 1) - rand.nextInt(config.zSpread + 1));
            if (config.target.test(reader.getBlockState(mutablePos), rand) && Arrays.stream(Direction.values()).noneMatch(direction -> reader.isEmptyBlock(mutablePos.relative(direction)))) {
                reader.setBlock(mutablePos, blockstate, 2);
                final BlockEntity type = reader.getBlockEntity(mutablePos);
                if (type instanceof EtherealMobSpawnerTileEntity) {
                    final BaseSpawner spawnerBaseLogic = ((EtherealMobSpawnerTileEntity) type).getSpawnerBaseLogic();
                    spawnerBaseLogic.load(config.spawnerNBT);
                }
                generatedCount++;
            }
        }
        return generatedCount > 0;
    }
}
