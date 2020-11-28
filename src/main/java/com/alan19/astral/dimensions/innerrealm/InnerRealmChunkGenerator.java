package com.alan19.astral.dimensions.innerrealm;

import com.alan19.astral.Astral;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Blockreader;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.Optional;

/**
 * Generates a void world, no chunks are generated naturally
 */
public class InnerRealmChunkGenerator extends ChunkGenerator {
    public static final Codec<InnerRealmChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> instance.group(BiomeProvider.CODEC.fieldOf("biome_source")
            .forGetter(ChunkGenerator::getBiomeProvider)
    ).apply(instance, instance.stable(InnerRealmChunkGenerator::new)));

    public InnerRealmChunkGenerator(BiomeProvider biomeProvider) {
        super(biomeProvider, new DimensionStructuresSettings(Optional.empty(), Collections.emptyMap()));
    }

    public static void register() {
        Registry.register(Registry.CHUNK_GENERATOR_CODEC, new ResourceLocation(Astral.MOD_ID, "inner_realm_chunk_generator"), CODEC);
    }


    @Override
    protected Codec<? extends ChunkGenerator> func_230347_a_() {
        return CODEC;
    }

    @Nonnull
    @Override
    public ChunkGenerator func_230349_a_(long p_230349_1_) {
        return this;
    }

    @Override
    public void generateSurface(WorldGenRegion p_225551_1_, IChunk p_225551_2_) {
        // We don't generate anything because it's a void world
    }

    @Override
    @ParametersAreNonnullByDefault
    public void func_230352_b_(IWorld p_230352_1_, StructureManager p_230352_2_, IChunk p_230352_3_) {
        // We don't generate any structures
    }

    @Override
    public int getHeight(int x, int z, @Nonnull Heightmap.Type heightmapType) {
        return 0;
    }

    @Override
    @Nonnull
    public IBlockReader func_230348_a_(int p_230348_1_, int p_230348_2_) {
        return new Blockreader(new BlockState[0]);
    }
}
