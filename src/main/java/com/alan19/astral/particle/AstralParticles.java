package com.alan19.astral.particle;

import com.alan19.astral.Astral;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AstralParticles {
    private static final DeferredRegister<ParticleType<?>> PARTICLES = new DeferredRegister<>(ForgeRegistries.PARTICLE_TYPES, Astral.MOD_ID);

    public static final RegistryObject<ParticleType<BlockParticleData>> ETHEREAL_REPLACE_PARTICLE = PARTICLES.register("ethereal_replace_particle", () -> new ParticleType<>(false, BlockParticleData.DESERIALIZER));

    public static final RegistryObject<BasicParticleType> ETHEREAL_FLAME = PARTICLES.register("ethereal_flame", () -> new BasicParticleType(false));
    public static final RegistryObject<BasicParticleType> INTENTION_BEAM_PARTICLE = PARTICLES.register("intention_beam", () -> new BasicParticleType(false));

    public static void register(IEventBus modBus) {
        PARTICLES.register(modBus);
    }

}
