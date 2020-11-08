package com.alan19.astral.world;

import com.alan19.astral.Astral;
import com.alan19.astral.configs.AstralConfig;
import com.alan19.astral.world.islands.AstralIslandPiece;
import com.alan19.astral.world.islands.AstralIslandStructure;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AstralFeatures {

    // Registries
    private static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Astral.MOD_ID);
    private static final DeferredRegister<Structure<?>> STRUCTURE_FEATURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, Astral.MOD_ID);

    // Vegetation
    public static final RegistryObject<Feature<BaseTreeFeatureConfig>> ETHEREAL_TREE = FEATURES.register("ethereal_tree", () -> new TreeFeature(BaseTreeFeatureConfig.CODEC));
    public static final RegistryObject<Feature<FeverweedFeatureConfig>> FEVERWEED_FEATURE = FEATURES.register("feverweed", FeverweedFeature::new);
    public static final RegistryObject<Feature<SnowberryFeatureConfig>> SNOWBERRY_FEATURE = FEATURES.register("snowberries", SnowberryFeature::new);

    // Structures
    public static final RegistryObject<Structure<NoFeatureConfig>> ETHERIC_ISLE_OCEAN = STRUCTURE_FEATURES.register("etheric_isle_ocean", () -> new AstralIslandStructure(NoFeatureConfig.field_236558_a_, "etheric_isle_ocean"));
    public static final RegistryObject<Structure<NoFeatureConfig>> ETHERIC_ISLE_LAND = STRUCTURE_FEATURES.register("etheric_isle_land", () -> new AstralIslandStructure(NoFeatureConfig.field_236558_a_, "etheric_isle_land"));

    public static final IStructurePieceType ASTRAL_ISLAND_PIECE = Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(Astral.MOD_ID, "astral_island_piece"), AstralIslandPiece::new);

    public static void register(IEventBus modBus) {
        FEATURES.register(modBus);
    }

    @SubscribeEvent
    void commonSetup(FMLCommonSetupEvent event) {
        WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, ETHERIC_ISLE_OCEAN.getId(), ETHERIC_ISLE_OCEAN.get().withConfiguration(NoFeatureConfig.field_236559_b_));
        Structure.NAME_STRUCTURE_BIMAP.put(ETHERIC_ISLE_OCEAN.getId().toString(), ETHERIC_ISLE_OCEAN.get());
        DimensionSettings.func_242746_i().getStructures().func_236195_a_().put(ETHERIC_ISLE_OCEAN.get(), new StructureSeparationSettings(6, 3, 27));

        WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, ETHERIC_ISLE_LAND.getId(), ETHERIC_ISLE_LAND.get().withConfiguration(NoFeatureConfig.field_236559_b_));
        Structure.NAME_STRUCTURE_BIMAP.put(ETHERIC_ISLE_LAND.getId().toString(), ETHERIC_ISLE_LAND.get());
        DimensionSettings.func_242746_i().getStructures().func_236195_a_().put(ETHERIC_ISLE_LAND.get(), new StructureSeparationSettings(12, 10, 27));

        final AstralConfig.WorldgenSettings worldgenSettings = AstralConfig.getWorldgenSettings();
        WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_FEATURE, FEVERWEED_FEATURE.getId(), FEVERWEED_FEATURE.get().withConfiguration(new FeverweedFeatureConfig(worldgenSettings.getFeverweedMinPatchSize(), worldgenSettings.getFeverweedMaxPatchSize(), worldgenSettings.getFeverweedPatchSpawnRate(), worldgenSettings.getFeverweedPatchSpawnRate(), worldgenSettings.getFeverweedPatchDistribution())));
        WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_FEATURE, SNOWBERRY_FEATURE.getId(), SNOWBERRY_FEATURE.get().withConfiguration(new SnowberryFeatureConfig(worldgenSettings.getSnowberryMinPatchSize(), worldgenSettings.getSnowberryMaxPatchSize(), worldgenSettings.getSnowberryPatchSpawnRate(), 60)));

    }
}
