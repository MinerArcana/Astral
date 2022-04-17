package com.alan19.astral.world;

import com.alan19.astral.Astral;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AstralStructures {
    static final DeferredRegister<StructureFeature<?>> STRUCTURE_FEATURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, Astral.MOD_ID);
    // Structures
    public static final RegistryObject<StructureFeature<?>> ETHERIC_ISLE = STRUCTURE_FEATURES.register("etheric_isle", EthericIsleStructure::new);

    public static void register(IEventBus modBus) {
        STRUCTURE_FEATURES.register(modBus);
    }
}
