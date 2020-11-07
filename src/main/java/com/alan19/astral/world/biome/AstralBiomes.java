package com.alan19.astral.world.biome;

import com.alan19.astral.Astral;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeMaker;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AstralBiomes {
    public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES, Astral.MOD_ID);

    static {
        BIOMES.register("psyscape", BiomeMaker::makeVoidBiome);
    }

    public static void register(IEventBus modBus) {
        BIOMES.register(modBus);
    }
}
