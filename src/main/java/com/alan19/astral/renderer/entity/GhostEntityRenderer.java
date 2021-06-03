package com.alan19.astral.renderer.entity;

import com.alan19.astral.entity.ghost.GhostEntity;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class GhostEntityRenderer extends BipedRenderer<GhostEntity, GhostModel> {

    public GhostEntityRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new GhostModel(), .3F);
    }

    @Nonnull
    @Override
    public ResourceLocation getEntityTexture(@Nonnull GhostEntity entity) {
        return new ResourceLocation("astral", "textures/entity/ghost.png");
    }

}
