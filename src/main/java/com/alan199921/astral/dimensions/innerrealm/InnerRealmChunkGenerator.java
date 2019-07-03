package com.alan199921.astral.dimensions.innerrealm;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.*;
import org.checkerframework.checker.nullness.qual.NonNull;

public class InnerRealmChunkGenerator extends ChunkGenerator<GenerationSettings> {
    public InnerRealmChunkGenerator(IWorld worldIn, BiomeProvider provider, GenerationSettings settingsIn) {
        super(worldIn, provider, settingsIn);
    }

    @Override
    public void generateSurface(@NonNull IChunk chunk) {
        BlockState bedrock = Blocks.BEDROCK.getDefaultState();
        BlockState stone = Blocks.STONE.getDefaultState();
        BlockState dirt = Blocks.DIRT.getDefaultState();
        BlockState grass = Blocks.GRASS_BLOCK.getDefaultState();
        int x1, y1, z1;
        int worldHeight = 256;

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        for (x1 = 0; x1 < 16; x1++) {
            for (z1 = 0; z1 < 16; z1++) {
                chunk.setBlockState(pos.setPos(x1, 0, z1), bedrock, false);
            }
        }
        for (x1 = 0; x1 < 16; x1++) {
            for (y1 = 1; y1 < worldHeight - 3; y1++) {
                for (z1 = 0; z1 < 16; z1++) {
                    chunk.setBlockState(pos.setPos(x1, y1, z1), stone, false);
                }
            }
        }
        for (x1 = 0; x1 < 16; x1++) {
            for (y1 = worldHeight - 3; y1 < worldHeight - 1; y1++) {
                for (z1 = 0; z1 < 16; z1++) {
                    chunk.setBlockState(pos.setPos(x1, y1, z1), dirt, false);
                }
            }
        }
        for (x1 = 0; x1 < 16; x1++) {
            for (y1 = worldHeight - 1; y1 < worldHeight; y1++) {
                for (z1 = 0; z1 < 16; z1++) {
                    chunk.setBlockState(pos.setPos(x1, y1, z1), grass, false);
                }
            }
        }
    }

    @Override
    public int getGroundHeight() {
        return this.world.getSeaLevel() + 1;
    }

    @Override
    public void makeBase(IWorld iWorld, IChunk iChunk) {

    }

    @Override
    public int func_222529_a(int p_222529_1_, int p_222529_2_, Heightmap.Type p_222529_3_) {
        return 0;
    }
}