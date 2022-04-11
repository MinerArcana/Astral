package com.alan19.astral.renderer.entity;

import com.alan19.astral.entity.projectile.IntentionBeam;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

public class IntentionBeamRenderer extends EntityRenderer<IntentionBeam> {

    protected IntentionBeamRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    @Nonnull
    public ResourceLocation getTextureLocation(@Nonnull IntentionBeam entity) {
        return new ResourceLocation("astral", "textures/entity/intention_beam.png");
    }
}
