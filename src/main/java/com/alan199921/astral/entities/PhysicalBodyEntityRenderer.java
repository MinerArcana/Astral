package com.alan199921.astral.entities;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public class PhysicalBodyEntityRenderer extends LivingRenderer<PhysicalBodyEntity, PhysicalBodyModel> {

    public PhysicalBodyEntityRenderer(EntityRendererManager rendererManager, float shadowSizeIn) {
        super(rendererManager, new PhysicalBodyModel(0.0f, true), shadowSizeIn);
        this.addLayer(new BipedArmorLayer<>(this, new NonRotatingBipedModel(0.5F), new NonRotatingBipedModel(1.0F)));
        this.addLayer(new HeldItemLayer<>(this));
        this.addLayer(new ArrowLayer<>(this));
        this.addLayer(new HeadLayer<>(this));
        this.addLayer(new ElytraLayer<>(this));
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(PhysicalBodyEntity entity) {
        return getSkin(entity.getGameProfile());
    }

    private ResourceLocation getSkin(GameProfile gameProfile) {
        if (!gameProfile.isComplete()) {
            return new ResourceLocation("minecraft:textures/entity/steve.png");
        }
        else {
            final Minecraft minecraft = Minecraft.getInstance();
            SkinManager skinManager = minecraft.getSkinManager();
            final Map<Type, MinecraftProfileTexture> loadSkinFromCache = skinManager.loadSkinFromCache(gameProfile); // returned map may or may not be typed
            if (loadSkinFromCache.containsKey(MinecraftProfileTexture.Type.SKIN)) {
                return skinManager.loadSkin(loadSkinFromCache.get(Type.SKIN), Type.SKIN);
            }
            else {
                return DefaultPlayerSkin.getDefaultSkin(gameProfile.getId());
            }
        }
    }

    @Nonnull
    @Override
    public PhysicalBodyModel getEntityModel() {
        return super.getEntityModel();
    }

    @Override
    protected void applyRotations(PhysicalBodyEntity p_77043_1_, float p_77043_2_, float p_77043_3_, float p_77043_4_) {
        super.applyRotations(p_77043_1_, 0, 0, 0);
    }

    @Override
    protected void renderLivingAt(PhysicalBodyEntity e, double x, double y, double z) {
        super.renderLivingAt(e, x, y, z); // translation
        GlStateManager.rotated(e.isFaceDown() ? 90 : 270, 1F, 0F, 0F); // face-down
        GlStateManager.rotated(e.rotationPitch, 0F, 0F, 1F); // turn
        GlStateManager.translated(0F, -0.85F, e.isFaceDown() ? -0.125F : 0.125F); // center
    }
}
