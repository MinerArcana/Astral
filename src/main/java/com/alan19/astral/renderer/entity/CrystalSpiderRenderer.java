package com.alan19.astral.renderer.entity;

import com.alan19.astral.Astral;
import com.alan19.astral.entity.CrystalSpiderEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SpiderRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@OnlyIn(Dist.CLIENT)
public class CrystalSpiderRenderer extends SpiderRenderer<CrystalSpiderEntity> {
    private static final ResourceLocation CAVE_SPIDER_TEXTURES = new ResourceLocation(Astral.MOD_ID, "textures/entity/crystal_spider.png");

    public CrystalSpiderRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
        this.shadowSize *= 0.7F;
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void preRenderCallback(CrystalSpiderEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(0.7F, 0.7F, 0.7F);
    }

    /**
     * Returns the location of an entity's texture.
     */
    @Override
    @Nonnull
    public ResourceLocation getEntityTexture(@Nonnull CrystalSpiderEntity entity) {
        return CAVE_SPIDER_TEXTURES;
    }
}
