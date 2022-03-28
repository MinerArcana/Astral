package com.alan19.astral.dimensions.innerrealm;

import com.alan19.astral.Astral;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.StructureSettings;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.Optional;

/**
 * Generates a void world, no chunks are generated naturally
 */
public class InnerRealmChunkGenerator extends ChunkGenerator {
    public static final Codec<InnerRealmChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> instance.group(BiomeSource.CODEC.fieldOf("biome_source")
            .forGetter(ChunkGenerator::getBiomeSource)
    ).apply(instance, instance.stable(InnerRealmChunkGenerator::new)));

    public InnerRealmChunkGenerator(BiomeSource biomeProvider) {
        super(biomeProvider, new StructureSettings(Optional.empty(), Collections.emptyMap()));
    }

    public static void register() {
        Registry.register(Registry.CHUNK_GENERATOR, new ResourceLocation(Astral.MOD_ID, "inner_realm_chunk_generator"), CODEC);
    }


    @Override
    protected Codec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Nonnull
    @Override
    public ChunkGenerator withSeed(long p_230349_1_) {
        return this;
    }

    @Override
    public void buildSurfaceAndBedrock(WorldGenRegion p_225551_1_, ChunkAccess p_225551_2_) {
        // We don't generate anything because it's a void world
    }

    @Override
    @ParametersAreNonnullByDefault
    public void fillFromNoise(LevelAccessor p_230352_1_, StructureFeatureManager p_230352_2_, ChunkAccess p_230352_3_) {
        // We don't generate any structures
    }

    @Override
    public int getBaseHeight(int x, int z, @Nonnull Heightmap.Types heightmapType) {
        return 0;
    }

    @Override
    @Nonnull
    public BlockGetter getBaseColumn(int p_230348_1_, int p_230348_2_) {
        return new NoiseColumn(new BlockState[0]);
    }
}
