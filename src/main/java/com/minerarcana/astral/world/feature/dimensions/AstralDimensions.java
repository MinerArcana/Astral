package com.minerarcana.astral.world.feature.dimensions;

import com.minerarcana.astral.Astral;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

public class AstralDimensions {
    public static final ResourceKey<Level> INNER_REALM = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(Astral.MOD_ID, "inner_realm"));
    public static final ResourceKey<DimensionType> INNER_REALM_TYPE = ResourceKey.create(Registry.DIMENSION_TYPE_REGISTRY, INNER_REALM.location());
    public static boolean isEntityNotInInnerRealm(Entity entity) {
        return true;
    }
}