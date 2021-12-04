package com.alan19.astral.dimensions;

import com.alan19.astral.Astral;
import com.alan19.astral.dimensions.innerrealm.InnerRealmChunkGenerator;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class AstralDimensions {
    public static final ResourceKey<Level> INNER_REALM = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(Astral.MOD_ID, "inner_realm"));


    public static boolean isEntityNotInInnerRealm(Entity entity) {
        return entity.getCommandSenderWorld().dimension() != INNER_REALM;
    }


    public static void setupDimension() {
        InnerRealmChunkGenerator.register();
    }
}