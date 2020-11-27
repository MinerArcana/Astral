package com.alan19.astral.world;

import com.alan19.astral.Astral;
import com.alan19.astral.world.features.FeverweedFeature;
import com.alan19.astral.world.features.FeverweedFeatureConfig;
import com.alan19.astral.world.features.SnowberryFeature;
import com.alan19.astral.world.features.SnowberryFeatureConfig;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
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
    public static final RegistryObject<Feature<BaseTreeFeatureConfig>> ETHEREAL_TREE = FEATURES.register("ethereal_tree", () -> new TreeFeature(BaseTreeFeatureConfig.CODEC));
    public static final RegistryObject<Feature<FeverweedFeatureConfig>> FEVERWEED_FEATURE = FEATURES.register("feverweed", FeverweedFeature::new);
    public static final RegistryObject<Feature<SnowberryFeatureConfig>> SNOWBERRY_FEATURE = FEATURES.register("snowberries", SnowberryFeature::new);

    public static void register(IEventBus modBus) {
        FEATURES.register(modBus);
    }

    // Adds features from biomes
    public static void addFeatures(BiomeLoadingEvent event) {
        if (event.getCategory() == Biome.Category.TAIGA) {
            event.getGeneration().withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, AstralConfiguredFeatures.CONFIGURED_SNOWBERRY.get());
        }
        if (event.getCategory() == Biome.Category.JUNGLE) {
            event.getGeneration().withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, AstralConfiguredFeatures.CONFIGURED_FEVERWEED.get());
        }
        if (event.getCategory() == Biome.Category.OCEAN) {
            event.getGeneration().getStructures().add(AstralConfiguredFeatures.CONFIGURED_ETHERIC_ISLE);
        }
    }

    // Restricts Etheric Isles to non-superflat Overworld
    public static void addDimensionSpacing(WorldEvent.Load event) {
        if (event.getWorld() instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) event.getWorld();
            if (!(serverWorld.getChunkProvider().getChunkGenerator() instanceof FlatChunkGenerator) && serverWorld.getDimensionKey().equals(World.OVERWORLD)) {
                Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(serverWorld.getChunkProvider().generator.func_235957_b_().func_236195_a_());
                tempMap.put(AstralStructures.ETHERIC_ISLE.get(), DimensionStructuresSettings.field_236191_b_.get(AstralStructures.ETHERIC_ISLE.get()));
                serverWorld.getChunkProvider().generator.func_235957_b_().field_236193_d_ = tempMap;
            }
        }
    }


}

