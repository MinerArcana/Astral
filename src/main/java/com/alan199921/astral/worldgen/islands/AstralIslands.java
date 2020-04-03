package com.alan199921.astral.worldgen.islands;

import com.mojang.datafixers.Dynamic;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.ScatteredStructure;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

import javax.annotation.Nonnull;
import java.util.function.Function;

public class AstralIslands extends ScatteredStructure<NoFeatureConfig> {
    public AstralIslands(Function<Dynamic<?>, ? extends NoFeatureConfig> function) {
        super(function);
    }

    @Override
    protected int getSeedModifier() {
        return 27;
    }

    @Override
    public IStartFactory getStartFactory() {
        return null;
    }

    @Override
    public String getStructureName() {
        return "Astral_Island";
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

        }
    }
}
