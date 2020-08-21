package com.alan19.astral.renderer.entity;

import com.alan19.astral.entity.projectile.IntentionBeam;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class IntentionBeamRenderer extends EntityRenderer<IntentionBeam> {
    public IntentionBeamRenderer(EntityRendererManager rendererManager) {
        super(rendererManager);
    }

    @Override
    @Nonnull
    public ResourceLocation getEntityTexture(@Nonnull IntentionBeam entity) {
        return new ResourceLocation("astral", "intention_beam");
    }
}
