package com.alan19.astral.renderer.entity;

import com.alan19.astral.entity.projectile.IntentionBeam;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

public class IntentionBeamRenderer extends EntityRenderer<IntentionBeam> {
    public IntentionBeamRenderer(EntityRenderDispatcher rendererManager) {
        super(rendererManager);
    }

    @Override
    @Nonnull
    public ResourceLocation getTextureLocation(@Nonnull IntentionBeam entity) {
        return new ResourceLocation("astral", "textures/entity/intention_beam.png");
    }
}
