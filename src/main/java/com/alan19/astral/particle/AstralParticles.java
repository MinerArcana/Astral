package com.alan19.astral.particle;

import com.alan19.astral.Astral;
import com.mojang.serialization.Codec;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AstralParticles {
    private static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Astral.MOD_ID);

    public static final RegistryObject<ParticleType<BlockParticleOption>> ETHEREAL_REPLACE_PARTICLE = PARTICLES.register("ethereal_replace_particle", () -> new ParticleType<BlockParticleOption>(false, BlockParticleOption.DESERIALIZER) {
        @Override
        public Codec codec() {
            return null;
        }
    });

    public static final RegistryObject<SimpleParticleType> ETHEREAL_FLAME = PARTICLES.register("ethereal_flame", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> INTENTION_BEAM_PARTICLE = PARTICLES.register("intention_beam", () -> new SimpleParticleType(false));

    public static void register(IEventBus modBus) {
        PARTICLES.register(modBus);
    }

}
