package com.alan19.astral.world.islands;

import com.alan19.astral.Astral;
import com.mojang.serialization.Codec;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.feature.template.TemplateManager;
import org.apache.logging.log4j.Level;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Adapted from https://github.com/SlimeKnights/TinkersConstruct
 */
public class EthericIsleStructure extends Structure<NoFeatureConfig> {

    public EthericIsleStructure(Codec<NoFeatureConfig> codec) {
        super(codec);
    }

    @Nonnull
    @Override
    public GenerationStage.Decoration getDecorationStage() {
        return GenerationStage.Decoration.TOP_LAYER_MODIFICATION;
    }

    @Nonnull
    @Override
    public IStartFactory<NoFeatureConfig> getStartFactory() {
        return AstralIslandStart::new;
    }

    public static class AstralIslandStart extends StructureStart<NoFeatureConfig> {

        public AstralIslandStart(Structure<NoFeatureConfig> structure, int chunkPosX, int chunkPosZ, MutableBoundingBox bounds, int references, long seed) {
            super(structure, chunkPosX, chunkPosZ, bounds, references, seed);
        }

        @Override
        @ParametersAreNonnullByDefault
        public void func_230364_a_(DynamicRegistries registries, ChunkGenerator generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn, NoFeatureConfig config) {
            int x = (chunkX << 4) + 7;
            int z = (chunkZ << 4) + 7;
            int heightModifier = rand.nextInt(32) + 64;
            BlockPos blockpos = new BlockPos(x, generator.getHeight(x, z, Heightmap.Type.WORLD_SURFACE) + heightModifier, z);

            JigsawManager.func_242837_a(registries, new VillageConfig(() -> registries.getRegistry(Registry.JIGSAW_POOL_KEY).getOrDefault(new ResourceLocation(Astral.MOD_ID, "etheric_isle/start_pool")), 50), AbstractVillagePiece::new, generator, templateManagerIn, blockpos, this.components, this.rand, true, false);

            this.recalculateStructureSize();
            Astral.LOGGER.log(Level.DEBUG, "Etheric Isle spawned at " + blockpos.getX() + " " + blockpos.getY() + " " + blockpos.getZ());
        }

    }
}
