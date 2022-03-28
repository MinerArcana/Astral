package com.alan19.astral.world;

import com.alan19.astral.Astral;
import com.alan19.astral.world.features.*;
import com.alan19.astral.world.trees.EtherealTreeFeature;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class AstralFeatures {

    // Registries
    private static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Astral.MOD_ID);

    // Vegetation
    public static final RegistryObject<EtherealTreeFeature> ETHEREAL_TREE = FEATURES.register("ethereal_tree", () -> new EtherealTreeFeature(TreeConfiguration.CODEC));
    public static final RegistryObject<Feature<SnowberryFeatureConfig>> SNOWBERRY_FEATURE = FEATURES.register("snowberries", SnowberryFeature::new);
    public static final RegistryObject<BuriedEtherealSpawnerFeature> BURIED_SPAWNER_FEATURE = FEATURES.register("buried_ethereal_spawner", () -> new BuriedEtherealSpawnerFeature(BuriedEtherealSpawnerConfig.CODEC));
    public static final RegistryObject<GenerateMultipleFeature> GENERATE_MULTIPLE_FEATURE = FEATURES.register("generate_multiple_features", () -> new GenerateMultipleFeature(RandomFeatureConfiguration.CODEC));
    public static final RegistryObject<StickyRandomPlacerFeature> STICKY_RANDOM_PLACER_FEATURE = FEATURES.register("sticky_random_placer", () -> new StickyRandomPlacerFeature(RandomPatchConfiguration.CODEC));

    public static void register(IEventBus modBus) {
        FEATURES.register(modBus);
    }

    // Adds features from biomes
    public static void addFeatures(BiomeLoadingEvent event) {
        if (event.getCategory() == Biome.BiomeCategory.TAIGA) {
            event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AstralConfiguredFeatures.CONFIGURED_SNOWBERRY);
        }
        if (event.getCategory() == Biome.BiomeCategory.JUNGLE) {
            event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AstralConfiguredFeatures.CONFIGURED_FEVERWEED);
        }
        if (event.getCategory() == Biome.BiomeCategory.OCEAN) {
            event.getGeneration().getStructures().add(() -> AstralConfiguredFeatures.CONFIGURED_ETHERIC_ISLE);
        }
    }

    // Restricts Etheric Isles to non-superflat Overworld
    public static void addDimensionSpacing(WorldEvent.Load event) {
        if (event.getWorld() instanceof ServerLevel) {
            ServerLevel serverWorld = (ServerLevel) event.getWorld();
            if (!(serverWorld.getChunkSource().getGenerator() instanceof FlatLevelSource) && serverWorld.dimension().equals(Level.OVERWORLD)) {
                Map<StructureFeature<?>, StructureFeatureConfiguration> tempMap = new HashMap<>(serverWorld.getChunkSource().generator.getSettings().structureConfig());
                tempMap.put(AstralStructures.ETHERIC_ISLE.get(), StructureSettings.DEFAULTS.get(AstralStructures.ETHERIC_ISLE.get()));
                serverWorld.getChunkSource().generator.getSettings().structureConfig = tempMap;
            }
        }
    }
}
