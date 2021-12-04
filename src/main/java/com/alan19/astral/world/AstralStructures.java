package com.alan19.astral.world;

import com.alan19.astral.Astral;
import com.alan19.astral.world.islands.AstralIslandPiece;
import com.alan19.astral.world.islands.EthericIsleStructure;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AstralStructures {
    public static final StructurePieceType ASTRAL_ISLAND_PIECE = Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(Astral.MOD_ID, "astral_island_piece"), AstralIslandPiece::new);
    static final DeferredRegister<StructureFeature<?>> STRUCTURE_FEATURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, Astral.MOD_ID);

    // Structures
    public static final RegistryObject<StructureFeature<NoneFeatureConfiguration>> ETHERIC_ISLE = STRUCTURE_FEATURES.register("etheric_isle", () -> new EthericIsleStructure(NoneFeatureConfiguration.CODEC));

    public static void setupStructures() {
        setupStructure(ETHERIC_ISLE.get(), new StructureFeatureConfiguration(3, 1, 11262020), false);
    }

    public static <F extends StructureFeature<?>> void setupStructure(F structure, StructureFeatureConfiguration structureSeparationSettings, boolean transformSurroundingLand) {
        StructureFeature.STRUCTURES_REGISTRY.put(structure.getRegistryName().toString(), structure);
        if (transformSurroundingLand) {
            StructureFeature.NOISE_AFFECTING_FEATURES = ImmutableList.<StructureFeature<?>>builder().addAll(StructureFeature.NOISE_AFFECTING_FEATURES).add(structure).build();
        }
        StructureSettings.DEFAULTS = ImmutableMap.<StructureFeature<?>, StructureFeatureConfiguration>builder()
                .putAll(StructureSettings.DEFAULTS)
                .put(structure, structureSeparationSettings)
                .build();
    }

    public static void register(IEventBus modBus) {
        STRUCTURE_FEATURES.register(modBus);
    }
}
