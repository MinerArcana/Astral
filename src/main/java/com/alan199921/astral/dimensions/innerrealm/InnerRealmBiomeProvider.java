package com.alan199921.astral.dimensions.innerrealm;

import com.google.common.collect.Sets;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.biome.provider.SingleBiomeProviderSettings;
import net.minecraft.world.gen.feature.structure.Structure;

import javax.annotation.Nullable;
import java.util.*;

public class InnerRealmBiomeProvider extends BiomeProvider {
    private static final List<Biome> SPAWN = Collections.singletonList(Biomes.PLAINS);
    private final Biome biome;

    public InnerRealmBiomeProvider(SingleBiomeProviderSettings settings) {
        this.biome = settings.getBiome();
    }

    @Override
    public List<Biome> getBiomesToSpawnIn() {
        return SPAWN;
    }

    @Override
    public Biome getBiome(int x, int y) {
        return this.biome;
    }

    @Override
    public Biome[] getBiomes(int x, int z, int width, int length, boolean cacheFlag) {
        Biome[] abiome = new Biome[width * length];
        Arrays.fill(abiome, 0, width * length, this.biome);
        return abiome;
    }

    @Override
    @Nullable
    public BlockPos findBiomePosition(int x, int z, int range, List<Biome> biomes, Random random) {
        return biomes.contains(this.biome) ? new BlockPos(x - range + random.nextInt(range * 2 + 1), 0, z - range + random.nextInt(range * 2 + 1)) : null;
    }

    @Override
    public boolean hasStructure(Structure<?> structureIn) {
        return false;
    }

    @Override
    public Set<BlockState> getSurfaceBlocks() {
        if (this.topBlocksCache.isEmpty()) {
            this.topBlocksCache.add(this.biome.getSurfaceBuilderConfig().getTop());
        }

        return this.topBlocksCache;
    }

    @Override
    public Set<Biome> getBiomesInSquare(int centerX, int centerZ, int sideLength) {
        return Sets.newHashSet(this.biome);
    }
}
