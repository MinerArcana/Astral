package com.minerarcana.astral.data;

import com.google.gson.JsonElement;
import com.minerarcana.astral.Astral;
import com.minerarcana.astral.blocks.AstralBlocks;
import com.minerarcana.astral.tags.AstralTags;
import com.minerarcana.astral.world.feature.AstralFeatures;
import com.minerarcana.astral.world.feature.SnowberryPatchConfig;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.JsonCodecProvider;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Astral.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void createProviders(final GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        JsonCodecProvider<BiomeModifier> biomeModifierProvider = getBiomeModifierProvider(event, generator);
        generator.addProvider(true, biomeModifierProvider);
        generator.addProvider(true, new AstralBlockTagProvider(generator, existingFileHelper));
        generator.addProvider(true, new AstralBlockstates(generator, existingFileHelper));
        generator.addProvider(true, new EnglishLocalizaton(generator));
        generator.addProvider(true, new AstralLootTables(generator));
        generator.addProvider(true, new ItemModels(generator, existingFileHelper));
        generator.addProvider(true, new AstralBrewProvider(generator));
    }

    @NotNull
    private static JsonCodecProvider<BiomeModifier> getBiomeModifierProvider(GatherDataEvent event, DataGenerator generator) {
        final RegistryOps<JsonElement> ops = RegistryOps.create(JsonOps.INSTANCE, RegistryAccess.builtinCopy());
        Map<ResourceLocation, BiomeModifier> modifierMap = new HashMap<>();
        modifierMap.put(new ResourceLocation(Astral.MOD_ID, "patch_snowberry_bush"),
                new ForgeBiomeModifiers.AddFeaturesBiomeModifier(new HolderSet.Named<>(ops.registry(Registry.BIOME_REGISTRY).orElseThrow(), BiomeTags.IS_TAIGA),
                        HolderSet.direct(Holder.direct(new PlacedFeature(Holder.direct(new ConfiguredFeature<>(AstralFeatures.PATCH_SNOWBERRIES.get(),
                                new SnowberryPatchConfig(2,
                                        5,
                                        2,
                                        5,
                                        128
                                        ))), List.of(RarityFilter.onAverageOnceEvery(25),
                                InSquarePlacement.spread(),
                                HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                                BiomeFilter.biome())))),
                        GenerationStep.Decoration.VEGETAL_DECORATION));
        modifierMap.put(new ResourceLocation(Astral.MOD_ID, "patch_feverweed"), new ForgeBiomeModifiers.AddFeaturesBiomeModifier(new HolderSet.Named<>(ops.registry(Registry.BIOME_REGISTRY).orElseThrow(), BiomeTags.IS_JUNGLE),
                HolderSet.direct(Holder.direct(new PlacedFeature(Holder.direct(new ConfiguredFeature<>(Feature.RANDOM_PATCH,
                        FeatureUtils.simpleRandomPatchConfiguration(96, PlacementUtils.filtered(Feature.SIMPLE_BLOCK,
                                new SimpleBlockConfiguration(BlockStateProvider.simple(AstralBlocks.FEVERWEED.get())),
                                BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, BlockPredicate.matchesTag(Direction.DOWN.getNormal(), AstralTags.FEVERWEED_PLANTABLE_ON)))))), List.of(RarityFilter.onAverageOnceEvery(10),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                        BiomeFilter.biome())))),
                GenerationStep.Decoration.VEGETAL_DECORATION));
        return JsonCodecProvider.forDatapackRegistry(generator,
                event.getExistingFileHelper(),
                Astral.MOD_ID,
                ops,
                ForgeRegistries.Keys.BIOME_MODIFIERS,
                modifierMap);
    }
}
