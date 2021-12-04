package com.alan19.astral.biomes;

import com.alan19.astral.Astral;
import net.minecraft.data.worldgen.biome.VanillaBiomes;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AstralBiomes {
    public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES, Astral.MOD_ID);

    static {
        BIOMES.register("psyscape", VanillaBiomes::theVoidBiome);
    }

    public static void register(IEventBus modBus) {
        BIOMES.register(modBus);
    }
}
