package com.alan19.astral.world;

import com.alan19.astral.Astral;
import com.alan19.astral.world.islands.AstralIslandPiece;
import com.alan19.astral.world.islands.AstralIslandStructure;
import com.alan19.astral.world.trees.EtherealTreeFeature;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AstralFeatures {

    private static final DeferredRegister<Feature<?>> FEATURES = new DeferredRegister<>(ForgeRegistries.FEATURES, Astral.MOD_ID);

    public static final RegistryObject<Feature<TreeFeatureConfig>> ETHEREAL_TREE = FEATURES.register("ethereal_tree", () -> new EtherealTreeFeature(TreeFeatureConfig::deserializeFoliage));
    public static final RegistryObject<Structure<NoFeatureConfig>> ASTRAL_ISLAND = FEATURES.register("astral_island_structure", () -> new AstralIslandStructure(NoFeatureConfig::deserialize));
    public static final IStructurePieceType ASTRAL_ISLAND_PIECE = Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(Astral.MOD_ID, "astral_island_piece"), AstralIslandPiece::new);

    public static void register(IEventBus modBus) {
        FEATURES.register(modBus);
    }
}
