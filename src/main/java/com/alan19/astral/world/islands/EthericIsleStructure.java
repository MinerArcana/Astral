package com.alan19.astral.world.islands;

import com.alan19.astral.Astral;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature.StructureStartFactory;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.structures.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import org.apache.logging.log4j.Level;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.text.MessageFormat;

/**
 * Adapted from https://github.com/SlimeKnights/TinkersConstruct
 */
public class EthericIsleStructure extends StructureFeature<NoneFeatureConfiguration> {

    public EthericIsleStructure(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Nonnull
    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.TOP_LAYER_MODIFICATION;
    }

    @Nonnull
    @Override
    public StructureStartFactory<NoneFeatureConfiguration> getStartFactory() {
        return AstralIslandStart::new;
    }

    public static class AstralIslandStart extends StructureStart<NoneFeatureConfiguration> {

        public AstralIslandStart(StructureFeature<NoneFeatureConfiguration> structure, int chunkPosX, int chunkPosZ, BoundingBox bounds, int references, long seed) {
            super(structure, chunkPosX, chunkPosZ, bounds, references, seed);
        }

        @Override
        @ParametersAreNonnullByDefault
        public void generatePieces(RegistryAccess registries, ChunkGenerator generator, StructureManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn, NoneFeatureConfiguration config) {
            int x = (chunkX << 4) + 7;
            int z = (chunkZ << 4) + 7;
            int heightModifier = random.nextInt(32) + 64;
            BlockPos blockpos = new BlockPos(x, generator.getBaseHeight(x, z, Heightmap.Types.WORLD_SURFACE) + heightModifier, z);

            JigsawPlacement.addPieces(registries, new JigsawConfiguration(() -> registries.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY).get(new ResourceLocation(Astral.MOD_ID, "etheric_isle/start_pool")), 50), PoolElementStructurePiece::new, generator, templateManagerIn, blockpos, this.pieces, this.random, true, false);

            this.calculateBoundingBox();
            Astral.LOGGER.log(Level.DEBUG, MessageFormat.format("Etheric Isle spawned at {0} {1} {2}", blockpos.getX(), blockpos.getY(), blockpos.getZ()));
        }

    }
}
