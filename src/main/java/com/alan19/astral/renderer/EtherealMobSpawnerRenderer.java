package com.alan19.astral.renderer;

import com.alan19.astral.blocks.tileentities.EtherealMobSpawnerTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.spawner.AbstractSpawner;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.ParametersAreNonnullByDefault;

@OnlyIn(Dist.CLIENT)
public class EtherealMobSpawnerRenderer extends TileEntityRenderer<EtherealMobSpawnerTileEntity> {
    public EtherealMobSpawnerRenderer(TileEntityRendererDispatcher p_i226016_1_) {
        super(p_i226016_1_);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void render(EtherealMobSpawnerTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        matrixStackIn.pushPose();
        matrixStackIn.translate(0.5D, 0.0D, 0.5D);
        AbstractSpawner abstractspawner = tileEntityIn.getSpawnerBaseLogic();
        Entity entity = abstractspawner.getOrCreateDisplayEntity();
        if (entity != null) {
            float f = 0.53125F;
            float f1 = Math.max(entity.getBbWidth(), entity.getBbHeight());
            if ((double) f1 > 1.0D) {
                f /= f1;
            }

            matrixStackIn.translate(0.0D, 0.4F, 0.0D);
            matrixStackIn.mulPose(Vector3f.YP.rotationDegrees((float) MathHelper.lerp(partialTicks, abstractspawner.getoSpin(), abstractspawner.getSpin()) * 10.0F));
            matrixStackIn.translate(0.0D, -0.2F, 0.0D);
            matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(-30.0F));
            matrixStackIn.scale(f, f, f);
            Minecraft.getInstance().getEntityRenderDispatcher().render(entity, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        }

        matrixStackIn.popPose();
    }
}
