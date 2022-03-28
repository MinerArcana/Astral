package com.alan19.astral.renderer.entity;

import com.alan19.astral.entity.physicalbody.NonRotatingBipedModel;
import com.alan19.astral.entity.physicalbody.PhysicalBodyEntity;
import com.alan19.astral.entity.physicalbody.PhysicalBodyModel;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Map;

public class PhysicalBodyEntityRenderer extends LivingEntityRenderer<PhysicalBodyEntity, PhysicalBodyModel> {

    public PhysicalBodyEntityRenderer(EntityRenderDispatcher rendererManager) {
        super(rendererManager, new PhysicalBodyModel(0.0f, true), .7F);
        this.addLayer(new HumanoidArmorLayer<>(this, new NonRotatingBipedModel(0.5F), new NonRotatingBipedModel(1.0F)));
        this.addLayer(new ItemInHandLayer<>(this));
        this.addLayer(new ArrowLayer<>(this));
        this.addLayer(new CustomHeadLayer<>(this));
        this.addLayer(new ElytraLayer<>(this));
    }

    @Nonnull
    @Override
    public ResourceLocation getTextureLocation(PhysicalBodyEntity entity) {
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
            final Map<Type, MinecraftProfileTexture> loadSkinFromCache = skinManager.getInsecureSkinInformation(gameProfile); // returned map may or may not be typed
            if (loadSkinFromCache.containsKey(MinecraftProfileTexture.Type.SKIN)) {
                return skinManager.registerTexture(loadSkinFromCache.get(Type.SKIN), Type.SKIN);
            }
            else {
                return DefaultPlayerSkin.getDefaultSkin(gameProfile.getId());
            }
        }
    }

    @Nonnull
    @Override
    public PhysicalBodyModel getModel() {
        return super.getModel();
    }

    @Override
    protected void setupRotations(PhysicalBodyEntity entityLiving, @Nonnull PoseStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(180.0F - rotationYaw));
        float rotationDegrees = 90F;
        matrixStackIn.translate(0, .125, -1);
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(rotationDegrees));
        matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(this.getFlipDegrees(entityLiving)));
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(entityLiving.isFaceDown() ? 270F : 90F));

        super.setupRotations(entityLiving, matrixStackIn, 0, 0, 0);
    }

}
