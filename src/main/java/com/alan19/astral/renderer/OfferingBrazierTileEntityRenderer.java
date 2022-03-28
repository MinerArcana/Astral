package com.alan19.astral.renderer;

import com.alan19.astral.blocks.tileentities.OfferingBrazierTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class OfferingBrazierTileEntityRenderer extends TileEntityRenderer<OfferingBrazierTileEntity> {
    public OfferingBrazierTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(@Nonnull OfferingBrazierTileEntity offeringBrazierTileEntity, float partialTicks, @Nonnull MatrixStack matrixStackIn, @Nonnull IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (!offeringBrazierTileEntity.hasLevel()) {
            return;
        }

        renderInventory(offeringBrazierTileEntity, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
    }

    private void renderInventory(OfferingBrazierTileEntity offeringBrazierTileEntity, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        final IItemHandler itemHandler = offeringBrazierTileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElseGet(ItemStackHandler::new);
        ItemStack fuel = itemHandler.getStackInSlot(0);
        ItemStack burning = itemHandler.getStackInSlot(1);
        if (!burning.isEmpty()) {
            matrixStackIn.pushPose();
            matrixStackIn.translate(0.5, 0.6, 0.5);
            itemRenderer.renderStatic(burning, ItemCameraTransforms.TransformType.GROUND, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn);
            matrixStackIn.popPose();
        }
        if (!fuel.isEmpty()) {
            matrixStackIn.pushPose();
            matrixStackIn.translate(0.5, 0.4, 0.5);
            matrixStackIn.scale(.5f, .5f, .5f);
            matrixStackIn.mulPose(new Quaternion(-90, 0, 0, true));
            itemRenderer.renderStatic(fuel, ItemCameraTransforms.TransformType.FIXED, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn);
            matrixStackIn.popPose();
        }
    }
}
