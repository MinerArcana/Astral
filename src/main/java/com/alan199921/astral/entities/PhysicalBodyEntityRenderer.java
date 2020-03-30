package com.alan199921.astral.entities;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Map;

public class PhysicalBodyEntityRenderer extends LivingRenderer<PhysicalBodyEntity, PhysicalBodyModel> {

    public PhysicalBodyEntityRenderer(EntityRendererManager rendererManager) {
        super(rendererManager, new PhysicalBodyModel(0.0f, true), .7F);
        this.addLayer(new BipedArmorLayer<>(this, new NonRotatingBipedModel(0.5F), new NonRotatingBipedModel(1.0F)));
        this.addLayer(new HeldItemLayer<>(this));
        this.addLayer(new ArrowLayer<>(this));
        this.addLayer(new HeadLayer<>(this));
        this.addLayer(new ElytraLayer<>(this));
    }

    @Nonnull
    @Override
    public ResourceLocation getEntityTexture(PhysicalBodyEntity entity) {
        return entity.getGameProfile()
                .map(this::getSkin)
                .orElseGet(() -> new ResourceLocation("minecraft:textures/entity/steve.png"));
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

    @Override
    public void render(@Nonnull PhysicalBodyEntity entityIn, float entityYaw, float partialTicks, @Nonnull MatrixStack matrixStackIn, @Nonnull IRenderTypeBuffer bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Nonnull
    @Override
    public PhysicalBodyModel getEntityModel() {
        return super.getEntityModel();
    }

    @Override
    protected void applyRotations(PhysicalBodyEntity entityLiving, @Nonnull MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F - rotationYaw));
        float rotationDegrees = 90F;
        matrixStackIn.translate(0, .125, -1);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(rotationDegrees));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(this.getDeathMaxRotation(entityLiving)));
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90F));

        super.applyRotations(entityLiving, matrixStackIn, 0, 0, 0);
    }

}
