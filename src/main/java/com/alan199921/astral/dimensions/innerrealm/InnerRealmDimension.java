package com.alan199921.astral.dimensions.innerrealm;

import com.alan199921.astral.dimensions.AstralDimensions;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.provider.BiomeProviderType;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class InnerRealmDimension extends Dimension {

    public InnerRealmDimension(World world, DimensionType dimensionType) {
        super(world, dimensionType, 1.0f);
    }

    @Override
    @Nonnull
    public ChunkGenerator<?> createChunkGenerator() {
        GenerationSettings generationSettings = AstralDimensions.INNER_REALM_CHUNK_GENERATOR.createSettings();
        return AstralDimensions.INNER_REALM_CHUNK_GENERATOR.create(this.world, BiomeProviderType.FIXED.create(BiomeProviderType.FIXED.func_226840_a_(this.world.getWorldInfo()).setBiome(Biomes.PLAINS)), generationSettings);
    }

    @Nullable
    @Override
    public BlockPos findSpawn(@Nonnull ChunkPos chunkPos, boolean checkValid) {
        return null;
    }

    @Nullable
    @Override
    public BlockPos findSpawn(int x, int z, boolean checkValid) {
        return null;
    }

    @Override
    public float calculateCelestialAngle(long worldTime, float partialTicks) {

        int j = 6000;
        float f1 = (j + partialTicks) / 24000.0F - 0.25F;

        if (f1 < 0.0F) {
            f1 += 1.0F;
        }

        if (f1 > 1.0F) {
            f1 -= 1.0F;
        }

        float f2 = f1;
        f1 = 1.0F - (float) ((Math.cos(f1 * Math.PI) + 1.0D) / 2.0D);
        f1 = f2 + (f1 - f2) / 3.0F;
        return f1;
    }


    @Override
    public boolean isSurfaceWorld() {
        return true;
    }

    @Override
    @Nonnull
    @OnlyIn(Dist.CLIENT)
    public Vec3d getFogColor(float p_76562_1_, float p_76562_2_) {
        float f = MathHelper.cos(p_76562_1_ * ((float) Math.PI * 2F)) * 2.0F + 0.5F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        float f1 = 0.7529412F;
        float f2 = 0.84705883F;
        float f3 = 1.0F;
        f1 = f1 * (f * 0.94F + 0.06F);
        f2 = f2 * (f * 0.94F + 0.06F);
        f3 = f3 * (f * 0.91F + 0.09F);
        return new Vec3d(f1, f2, f3);
    }

    @Override
    public boolean canRespawnHere() {
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean doesXZShowFog(int x, int z) {
        return false;
    }


    @Override
    public boolean hasSkyLight() {
        return true;
    }
}
