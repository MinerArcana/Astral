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
import java.util.Random;
import java.util.function.Function;

public class AstralIslandStructure extends ScatteredStructure<NoFeatureConfig> {
    public AstralIslandStructure(Function<Dynamic<?>, ? extends NoFeatureConfig> function) {
        super(function);
    }

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
    public String getStructureName() {
        return "astral:astral_island_structure";
    }

    @Nonnull
    @Override
    protected ChunkPos getStartPositionForPosition(ChunkGenerator<?> chunkGenerator, Random random, int x, int z, int spacingOffsetsX, int spacingOffsetsZ) {
        random.setSeed(this.getSeedModifier());
        int distance = this.getDistance();
        int separation = this.getSeparation();
        int x1 = x + distance * spacingOffsetsX;
        int z1 = z + distance * spacingOffsetsZ;
        int x2 = x1 < 0 ? x1 - distance + 1 : x1;
        int z2 = z1 < 0 ? z1 - distance + 1 : z1;
        int x3 = x2 / distance;
        int z3 = z2 / distance;
        ((SharedSeedRandom) random).setLargeFeatureSeedWithSalt(chunkGenerator.getSeed(), x3, z3, this.getSeedModifier());
        x3 = x3 * distance;
        z3 = z3 * distance;
        x3 = x3 + random.nextInt(distance - separation);
        z3 = z3 + random.nextInt(distance - separation);

        return new ChunkPos(x3, z3);
    }

    private int getSeparation() {
        return 5;
    }

    private int getDistance() {
        return 6;
    }

    @Override
    public int getSize() {
        return 8;
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

            int i1 = generator.func_222531_c(x, z, Heightmap.Type.WORLD_SURFACE_WG);
            int j1 = generator.func_222531_c(x, z + j, Heightmap.Type.WORLD_SURFACE_WG);
            int k1 = generator.func_222531_c(x + i, z, Heightmap.Type.WORLD_SURFACE_WG);
            int l1 = generator.func_222531_c(x + i, z + j, Heightmap.Type.WORLD_SURFACE_WG);

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
