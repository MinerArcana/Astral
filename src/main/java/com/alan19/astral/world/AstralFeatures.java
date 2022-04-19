package com.alan19.astral.world;

import com.alan19.astral.Astral;
import com.alan19.astral.blocks.AstralBlocks;
import com.alan19.astral.world.features.SnowberryFeature;
import com.alan19.astral.world.features.SnowberryFeatureConfig;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.RandomSpreadFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.BendingTrunkPlacer;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class AstralFeatures {

    // Registries
    private static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Astral.MOD_ID);

    // Vegetation
    public static final RegistryObject<Feature<TreeConfiguration>> ETHEREAL_TREE = FEATURES.register("ethereal_tree", () -> new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(AstralBlocks.ETHEREAL_WOOD.get().defaultBlockState()),
            new BendingTrunkPlacer(5, 2, 0, 3, UniformInt.of(0, 2)),
            BlockStateProvider.simple(AstralBlocks.ETHEREAL_LEAVES.get().defaultBlockState()),
            new RandomSpreadFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), ConstantInt.of(3), 30),
            new TwoLayersFeatureSize(1, 0, 1))
            .dirt(BlockStateProvider.simple(AstralBlocks.ETHER_DIRT.get().defaultBlockState()))
            .forceDirt()
            .build()).feature());
    public static final RegistryObject<Feature<SnowberryFeatureConfig>> SNOWBERRY_FEATURE = FEATURES.register("snowberries", SnowberryFeature::new);

    public static void register(IEventBus modBus) {
        FEATURES.register(modBus);
    }

    // Adds features from biomes
    public static void addFeatures(BiomeLoadingEvent event) {
        if (event.getCategory() == Biome.BiomeCategory.TAIGA) {
            event.getGeneration().getFeatures(GenerationStep.Decoration.VEGETAL_DECORATION).add(AstralConfiguredFeatures.PLACED_SNOWBERRY);
        }
        if (event.getCategory() == Biome.BiomeCategory.JUNGLE) {
            event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AstralConfiguredFeatures.PLACED_FEVERWEED);
        }
    }

}
