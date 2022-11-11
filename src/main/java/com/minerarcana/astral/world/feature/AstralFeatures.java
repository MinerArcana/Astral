package com.minerarcana.astral.world.feature;

import com.minerarcana.astral.Astral;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.RandomPatchFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AstralFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Astral.MOD_ID);

    public static final RegistryObject<Feature<SnowberryPatchConfig>> PATCH_SNOWBERRIES = FEATURES.register("snowberries", SnowberryBushFeature::new);

}
