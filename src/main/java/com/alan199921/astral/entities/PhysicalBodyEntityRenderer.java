package com.alan199921.astral.entities;

import com.alan199921.astral.Astral;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PhysicalBodyEntityRenderer extends LivingRenderer {

    public PhysicalBodyEntityRenderer(EntityRendererManager p_i50965_1_, float p_i50965_3_) {
        super(p_i50965_1_, new PlayerModel<>(0.0f, true), p_i50965_3_);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(@Nonnull Entity entity) {
        return new ResourceLocation("minecraft" + ":textures/entity/zombie/zombie.png");
    }

    @Nonnull
    @Override
    public EntityModel getEntityModel() {
        return super.getEntityModel();
    }

    @Override
    public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
        super.doRender((LivingEntity) p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }

    @Override
    protected void renderLivingAt(LivingEntity e, double x, double y, double z) {
        super.renderLivingAt(e,x,y,z); // translation
        GlStateManager.rotated(90,1F,0F,0F); // face-down
        GlStateManager.rotated(e.getRotationYawHead(),0F,0F,1F); // turn
        GlStateManager.translated(0F, -0.85F, -0.125F); // center
    }
}
