package com.alan199921.astral.entities;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PhysicalBodyEntityRenderer extends LivingRenderer {

    public PhysicalBodyEntityRenderer(EntityRendererManager rendererManager, float shadowSizeIn) {
        super(rendererManager, new PhysicalBodyModel(0.0f, true), shadowSizeIn);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(@Nonnull Entity entity) {
        return new ResourceLocation("minecraft" + ":textures/entity/steve.png");
    }

    @Nonnull
    @Override
    public EntityModel getEntityModel() {
        return super.getEntityModel();
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender((LivingEntity) entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected void applyRotations(LivingEntity p_77043_1_, float p_77043_2_, float p_77043_3_, float p_77043_4_) {
        super.applyRotations(p_77043_1_, 0, 0, 0);
    }

    @Override
    protected void renderLivingAt(LivingEntity e, double x, double y, double z) {
        super.renderLivingAt(e, x, y, z); // translation
        GlStateManager.rotated(90, 1F, 0F, 0F); // face-down
        GlStateManager.rotated(e.rotationPitch, 0F, 0F, 1F); // turn
        GlStateManager.translated(0F, -0.85F, -0.125F); // center
    }
}
