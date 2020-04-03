package com.alan199921.astral.worldgen;

import com.alan199921.astral.Astral;
import com.alan199921.astral.worldgen.islands.AstralIslandPiece;
import com.alan199921.astral.worldgen.islands.AstralIslandStructure;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class AstralFeatures {

    @ObjectHolder("astral:astral_island_structure")
    public static Structure<NoFeatureConfig> astralIsland;

    public static final IStructurePieceType ASTRAL_ISLAND_PIECE = Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(Astral.MOD_ID, "astral_island_piece"), AstralIslandPiece::new);


    @SubscribeEvent
    public static void registerFeature(RegistryEvent.Register<Feature<?>> event) {
        event.getRegistry().register(new AstralIslandStructure(NoFeatureConfig::deserialize).setRegistryName("astral:astral_island_structure"));
    }


}
