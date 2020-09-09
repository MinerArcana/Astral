package com.alan19.astral.world.islands;

import com.mojang.datafixers.Dynamic;
import net.minecraft.util.Rotation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.ScatteredStructure;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;
import java.util.function.Function;

/**
 * Adapted from https://github.com/SlimeKnights/TinkersConstruct
 */
public abstract class AstralIslandStructure extends ScatteredStructure<NoFeatureConfig> {

    public AstralIslandStructure(Function<Dynamic<?>, ? extends NoFeatureConfig> function) {
        super(function);
    }

    @Override
    protected abstract int getBiomeFeatureDistance(@Nonnull ChunkGenerator<?> chunkGenerator);

    @Override
    protected abstract int getBiomeFeatureSeparation(@Nonnull ChunkGenerator<?> chunkGenerator);

    @Override
    protected int getSeedModifier() {
        return 27;
    }

    @Nonnull
    @Override
    public IStartFactory getStartFactory() {
        return AstralIslandStart::new;
    }

    @Nonnull
    @Override
    public abstract String getStructureName();

    @Override
    public int getSize() {
        return 8;
    }

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    protected ChunkPos getStartPositionForPosition(ChunkGenerator<?> chunkGenerator, Random random, int x, int z, int spacingOffsetsX, int spacingOffsetsZ) {
        int distance = getBiomeFeatureDistance(chunkGenerator);
        int separation = getBiomeFeatureSeparation(chunkGenerator);
        int k = x + distance * spacingOffsetsX;
        int l = z + distance * spacingOffsetsZ;
        int i1 = k < 0 ? k - distance + 1 : k;
        int j1 = l < 0 ? l - distance + 1 : l;
        int k1 = i1 / distance;
        int l1 = j1 / distance;
        ((SharedSeedRandom) random).setLargeFeatureSeedWithSalt(chunkGenerator.getSeed(), k1, l1, this.getSeedModifier());
        k1 = k1 * distance;
        l1 = l1 * distance;
        k1 = k1 + random.nextInt(distance - separation);
        l1 = l1 + random.nextInt(distance - separation);
        return new ChunkPos(k1, l1);
    }

    public static class AstralIslandStart extends StructureStart {
        public AstralIslandStart(Structure<?> structure, int chunkPosX, int chunkPosZ, MutableBoundingBox bounds, int references, long seed) {
            super(structure, chunkPosX, chunkPosZ, bounds, references, seed);
        }

        @Override
        public void init(@Nonnull ChunkGenerator<?> generator, @Nonnull TemplateManager templateManagerIn, int chunkX, int chunkZ, @Nonnull Biome biomeIn) {
            int x = chunkX * 16 + 4 + this.rand.nextInt(8);
            int z = chunkZ * 16 + 4 + this.rand.nextInt(8);

            Rotation rotation = Rotation.values()[this.rand.nextInt(Rotation.values().length)];
            int i = 5;
            int j = 5;
            if (rotation == Rotation.CLOCKWISE_90) {
                i = -5;
            }
            else if (rotation == Rotation.CLOCKWISE_180) {
                i = -5;
                j = -5;
            }
            else if (rotation == Rotation.COUNTERCLOCKWISE_90) {
                j = -5;
            }

            int i1 = generator.getNoiseHeightMinusOne(x, z, Heightmap.Type.WORLD_SURFACE_WG);
            int j1 = generator.getNoiseHeightMinusOne(x, z + j, Heightmap.Type.WORLD_SURFACE_WG);
            int k1 = generator.getNoiseHeightMinusOne(x + i, z, Heightmap.Type.WORLD_SURFACE_WG);
            int l1 = generator.getNoiseHeightMinusOne(x + i, z + j, Heightmap.Type.WORLD_SURFACE_WG);

            int y = Math.min(Math.min(i1, j1), Math.min(k1, l1)) + 50 + this.rand.nextInt(50) + 11;

            int rnr = this.rand.nextInt(4);
            AstralIslandVariant variant = AstralIslandVariant.getVariantFromIndex(rnr);
//            String[] sizes = new String[]{"0x1x0", "2x2x4", "4x1x6", "8x1x11", "11x1x11"};
            String[] sizes = new String[]{"comma_island"};

            AstralIslandPiece astralIslandPiece = new AstralIslandPiece(templateManagerIn, variant, sizes[this.rand.nextInt(sizes.length)], new BlockPos(x, y, z), rotation);
            this.components.add(astralIslandPiece);
            this.recalculateStructureSize();
        }
    }
}
