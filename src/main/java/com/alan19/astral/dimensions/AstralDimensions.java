package com.alan19.astral.dimensions;

import com.alan19.astral.Astral;
import net.minecraft.entity.Entity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class AstralDimensions {
    public static final DeferredRegister<ChunkGenerator> CHUNK_GENERATOR_DEFERRED_REGISTER = DeferredRegister.create(Registry.CHUNK_GENERATOR_KEY, Astral.MOD_ID);
    public static final ResourceLocation INNER_REALM_RL = new ResourceLocation(Astral.MOD_ID, "inner_realm");
    public static final RegistryKey<World> INNER_REALM = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation(Astral.MOD_ID, "inner_realm"));


    public static boolean isEntityNotInInnerRealm(Entity entity) {
        return entity.dimension != DimensionType.byName(INNER_REALM_RL);
    }


}