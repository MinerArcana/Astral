package com.alan19.astral.world;

import com.alan19.astral.Astral;
import com.alan19.astral.world.islands.AstralIslandPiece;
import com.alan19.astral.world.islands.AstralIslandStructure;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;

public class AstralFeatures {

    private static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Astral.MOD_ID);

    public static final RegistryObject<Feature<TreeFeatureConfig>> ETHEREAL_TREE = FEATURES.register("ethereal_tree", () -> new TreeFeature(TreeFeatureConfig::deserializeFoliage));
    public static final RegistryObject<AstralIslandStructure> ETHERIC_ISLE_OCEAN = FEATURES.register("astral_island_structure", EthericIsleOcean::new);
    public static final RegistryObject<AstralIslandStructure> ETHERIC_ISLE_LAND = FEATURES.register("etheric_isle_land", EthericIsleLand::new);

    public static final IStructurePieceType ASTRAL_ISLAND_PIECE = Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(Astral.MOD_ID, "astral_island_piece"), AstralIslandPiece::new);

    public static void register(IEventBus modBus) {
        FEATURES.register(modBus);
    }

    private static class EthericIsleOcean extends AstralIslandStructure {
        public EthericIsleOcean() {
            super(NoFeatureConfig::deserialize);
        }

        @Override
        protected int getBiomeFeatureDistance(@Nonnull ChunkGenerator<?> chunkGenerator) {
            return 6;
        }

        @Override
        protected int getBiomeFeatureSeparation(@Nonnull ChunkGenerator<?> chunkGenerator) {
            return 5;
        }

        @Override
        @Nonnull
        public String getStructureName() {
            return "astral:etheric_isles_ocean";
        }
    }

    private static class EthericIsleLand extends AstralIslandStructure {
        public EthericIsleLand() {
            super(NoFeatureConfig::deserialize);
        }

        @Override
        protected int getBiomeFeatureDistance(@Nonnull ChunkGenerator<?> chunkGenerator) {
            return 12;
        }

        @Override
        protected int getBiomeFeatureSeparation(@Nonnull ChunkGenerator<?> chunkGenerator) {
            return 10;
        }

        @Override
        @Nonnull
        public String getStructureName() {
            return "astral:etheric_isles_land";
        }
    }
}
