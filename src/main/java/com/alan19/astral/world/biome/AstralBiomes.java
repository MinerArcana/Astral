package com.alan19.astral.world.biome;

import com.alan19.astral.Astral;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AstralBiomes {
    public static final DeferredRegister<Biome> BIOMES = new DeferredRegister<>(ForgeRegistries.BIOMES, Astral.MOD_ID);

    public static final RegistryObject<Biome> PSYSCAPE_BIOME = BIOMES.register("psyscape_biome", PsyscapeBiome::new);

    public static void register(IEventBus modBus) {
        BIOMES.register(modBus);
    }
}
