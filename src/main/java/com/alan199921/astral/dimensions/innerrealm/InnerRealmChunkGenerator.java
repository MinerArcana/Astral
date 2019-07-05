package com.alan199921.astral.dimensions.innerrealm;

import com.alan199921.astral.blocks.AstralMeridian;
import com.alan199921.astral.blocks.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import org.checkerframework.checker.nullness.qual.NonNull;

public class InnerRealmChunkGenerator extends ChunkGenerator<GenerationSettings> {
    public InnerRealmChunkGenerator(IWorld worldIn, BiomeProvider provider, GenerationSettings settingsIn) {
        super(worldIn, provider, settingsIn);
    }

    @Override
    public void generateSurface(@NonNull IChunk chunk) {
        //This is a void world!
    }

    @Override
    public int getGroundHeight() {
        return this.world.getSeaLevel() + 1;
    }

    @Override
    public void makeBase(IWorld iWorld, IChunk iChunk) {
        int x;
        int y;
        int z;
        //Make 15 block cubes with an Astral Meridian block in the center, except on the XZ plane

        //XZ plane
        for (x = 0; x < 16; x++) {
            for (z = 0; z < 16; z++) {
                iChunk.setBlockState(new BlockPos(x, iWorld.getSeaLevel(), z), ModBlocks.egoMembrane.getDefaultState(), false);
                iChunk.setBlockState(new BlockPos(x, iWorld.getSeaLevel() + 16, z), ModBlocks.egoMembrane.getDefaultState(), false);

            }
        }

        //XY Plane
        for (x = 0; x < 16; x++) {
            for (y = 0; y < 16; y++) {
                if (x == 8 && y == 8) {
                    iChunk.setBlockState(new BlockPos(x, iWorld.getSeaLevel() + y, 0), ModBlocks.astralMeridian.getDefaultState().with(AstralMeridian.PLANE, 0), false);
                    iChunk.setBlockState(new BlockPos(x, iWorld.getSeaLevel() + y, 16), ModBlocks.astralMeridian.getDefaultState().with(AstralMeridian.PLANE, 0), false);
                } else {
                    iChunk.setBlockState(new BlockPos(x, iWorld.getSeaLevel() + y, 0), ModBlocks.egoMembrane.getDefaultState(), false);
                    iChunk.setBlockState(new BlockPos(x, iWorld.getSeaLevel() + y, 16), ModBlocks.egoMembrane.getDefaultState(), false);
                }
            }
        }

        //YZ Plane
        for (z = 0; z < 16; z++) {
            for (y = 0; y < 16; y++) {
                if (y == 8 && z == 8) {
                    iChunk.setBlockState(new BlockPos(0, iWorld.getSeaLevel() + y, z), ModBlocks.astralMeridian.getDefaultState().with(AstralMeridian.PLANE, 1), false);
                    iChunk.setBlockState(new BlockPos(16, iWorld.getSeaLevel() + y, z), ModBlocks.astralMeridian.getDefaultState().with(AstralMeridian.PLANE, 1), false);
                } else {
                    iChunk.setBlockState(new BlockPos(0, iWorld.getSeaLevel() + y, z), ModBlocks.egoMembrane.getDefaultState(), false);
                    iChunk.setBlockState(new BlockPos(16, iWorld.getSeaLevel() + y, z), ModBlocks.egoMembrane.getDefaultState(), false);
                }
            }
        }
    }

    @Override
    public int func_222529_a(int p_222529_1_, int p_222529_2_, Heightmap.Type p_222529_3_) {
        return 0;
    }

    @Override
    public boolean hasStructure(Biome biomeIn, @NonNull Structure<? extends IFeatureConfig> structureIn) {
        return false;
    }
}
