package com.alan19.astral.particle;

import com.alan19.astral.Astral;
import com.mojang.serialization.Codec;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AstralParticles {
    private static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Astral.MOD_ID);

    public static final RegistryObject<ParticleType<BlockParticleData>> ETHEREAL_REPLACE_PARTICLE = PARTICLES.register("ethereal_replace_particle", () -> new ParticleType<BlockParticleData>(false, BlockParticleData.DESERIALIZER) {
        @Override
        public Codec codec() {
            return null;
        }
    });

    public static final RegistryObject<BasicParticleType> ETHEREAL_FLAME = PARTICLES.register("ethereal_flame", () -> new BasicParticleType(false));
    public static final RegistryObject<BasicParticleType> INTENTION_BEAM_PARTICLE = PARTICLES.register("intention_beam", () -> new BasicParticleType(false));

    public static void register(IEventBus modBus) {
        PARTICLES.register(modBus);
    }

}
