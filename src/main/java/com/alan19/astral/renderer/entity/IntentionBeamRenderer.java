package com.alan19.astral.renderer.entity;

import com.alan19.astral.entity.projectile.IntentionBeam;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class IntentionBeamRenderer extends EntityRenderer<IntentionBeam> {
    private static final RenderType CUTOUT_NO_CULL = RenderType.getEntityCutoutNoCull(new ResourceLocation("minecraft", "textures/item/ender_pearl.png"));

    public IntentionBeamRenderer(EntityRendererManager rendererManager) {
        super(rendererManager);
    }

    private static void func_229045_a_(IVertexBuilder p_229045_0_, Matrix4f p_229045_1_, Matrix3f p_229045_2_, int p_229045_3_, float p_229045_4_, int p_229045_5_, int p_229045_6_, int p_229045_7_) {
        p_229045_0_.pos(p_229045_1_, p_229045_4_ - 0.5F, (float)p_229045_5_ - .5F, 0.0F).color(255, 255, 255, 255).tex((float)p_229045_6_, (float)p_229045_7_).overlay(OverlayTexture.NO_OVERLAY).lightmap(p_229045_3_).normal(p_229045_2_, 0.0F, 1.0F, 0.0F).endVertex();
    }

    @Nonnull
    @Override
    public ResourceLocation getEntityTexture(@Nonnull IntentionBeam entity) {
        return new ResourceLocation("astral", "textures/entity/intention_beam.png");
    }

    @Override
    protected int getBlockLight(@Nonnull IntentionBeam entityIn, float partialTicks) {
        return 15;
    }

    @Override
    public void render(@Nonnull IntentionBeam entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        matrixStackIn.push();
        matrixStackIn.scale(1.0F, 1.0F, 1.0F);
        matrixStackIn.rotate(this.renderManager.getCameraOrientation());
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F));
        MatrixStack.Entry matrixstack$entry = matrixStackIn.getLast();
        Matrix4f matrix4f = matrixstack$entry.getMatrix();
        Matrix3f matrix3f = matrixstack$entry.getNormal();
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(CUTOUT_NO_CULL);
        func_229045_a_(ivertexbuilder, matrix4f, matrix3f, packedLightIn, 0.0F, 0, 0, 1);
        func_229045_a_(ivertexbuilder, matrix4f, matrix3f, packedLightIn, 1.0F, 0, 1, 1);
        func_229045_a_(ivertexbuilder, matrix4f, matrix3f, packedLightIn, 1.0F, 1, 1, 0);
        func_229045_a_(ivertexbuilder, matrix4f, matrix3f, packedLightIn, 0.0F, 1, 0, 0);
        matrixStackIn.pop();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

}
