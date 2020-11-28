package com.alan19.astral.dimensions;

import com.alan19.astral.Astral;
import com.alan19.astral.dimensions.innerrealm.InnerRealmChunkGenerator;
import net.minecraft.entity.Entity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class AstralDimensions {
    public static final RegistryKey<World> INNER_REALM = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation(Astral.MOD_ID, "inner_realm"));


    public static boolean isEntityNotInInnerRealm(Entity entity) {
        return entity.getEntityWorld().getDimensionKey() != INNER_REALM;
    }


    public static void setupDimension() {
        InnerRealmChunkGenerator.register();
    }
}