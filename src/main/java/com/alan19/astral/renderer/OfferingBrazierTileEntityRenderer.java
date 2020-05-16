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
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class OfferingBrazierTileEntityRenderer extends TileEntityRenderer<OfferingBrazierTileEntity> {
    public OfferingBrazierTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(@Nonnull OfferingBrazierTileEntity offeringBrazierTileEntity, float partialTicks, @Nonnull MatrixStack matrixStackIn, @Nonnull IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (!offeringBrazierTileEntity.hasWorld()) {
            return;
        }

        renderInventory(offeringBrazierTileEntity, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
    }

    private void renderInventory(OfferingBrazierTileEntity offeringBrazierTileEntity, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack item = offeringBrazierTileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElseGet(ItemStackHandler::new).getStackInSlot(1);
        if (!item.isEmpty()) {
            matrixStackIn.push();
            matrixStackIn.translate(0.5, 0.6, 0.5);
            itemRenderer.renderItem(item, ItemCameraTransforms.TransformType.GROUND, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn);
            matrixStackIn.pop();
        }
    }
}
