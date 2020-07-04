package com.alan19.astral.dimensions.innerrealm;

import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;

import javax.annotation.Nonnull;

/**
 * Generates a void world, no chunks are generated naturally
 */
public class InnerRealmChunkGenerator extends ChunkGenerator<GenerationSettings> {
    public InnerRealmChunkGenerator(IWorld worldIn, BiomeProvider provider, GenerationSettings settingsIn) {
        super(worldIn, provider, settingsIn);
    }

    @Override
    public void generateSurface(@Nonnull WorldGenRegion region, IChunk chunk) {
        //This is a void world!

    }

    @Override
    public int getGroundHeight() {
        return this.world.getSeaLevel() + 1;
    }

    @Override
    public void makeBase(@Nonnull IWorld iWorld, @Nonnull IChunk iChunk) {
        //Chunks are created as players spawn and unlock rooms
    }

    @Override
    public int getHeight(int x, int z, @Nonnull Heightmap.Type heightmapType) {
        return 0;
    }

    @Override
    public boolean hasStructure(@Nonnull Biome biomeIn, @Nonnull Structure<? extends IFeatureConfig> structureIn) {
        return false;
    }
}
