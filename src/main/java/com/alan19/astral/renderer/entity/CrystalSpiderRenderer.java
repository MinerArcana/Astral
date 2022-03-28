package com.alan19.astral.renderer.entity;

import com.alan19.astral.Astral;
import com.alan19.astral.entity.crystalspider.CrystalSpiderEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.SpiderRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@OnlyIn(Dist.CLIENT)
public class CrystalSpiderRenderer extends SpiderRenderer<CrystalSpiderEntity> {
    private static final ResourceLocation CAVE_SPIDER_TEXTURES = new ResourceLocation(Astral.MOD_ID, "textures/entity/crystal_spider.png");

    public CrystalSpiderRenderer(EntityRenderDispatcher renderManagerIn) {
        super(renderManagerIn);
        this.shadowRadius *= 0.7F;
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void scale(CrystalSpiderEntity entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(0.7F, 0.7F, 0.7F);
    }

    /**
     * Returns the location of an entity's texture.
     */
    @Override
    @Nonnull
    public ResourceLocation getTextureLocation(@Nonnull CrystalSpiderEntity entity) {
        return CAVE_SPIDER_TEXTURES;
    }
}
