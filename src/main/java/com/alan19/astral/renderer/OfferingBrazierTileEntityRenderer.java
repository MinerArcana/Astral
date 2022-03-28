package com.alan19.astral.renderer;

import com.alan19.astral.blocks.tileentities.OfferingBrazierTileEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class OfferingBrazierTileEntityRenderer extends BlockEntityRenderer<OfferingBrazierTileEntity> {
    public OfferingBrazierTileEntityRenderer(BlockEntityRenderDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(@Nonnull OfferingBrazierTileEntity offeringBrazierTileEntity, float partialTicks, @Nonnull PoseStack matrixStackIn, @Nonnull MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (!offeringBrazierTileEntity.hasLevel()) {
            return;
        }

        renderInventory(offeringBrazierTileEntity, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
    }

    private void renderInventory(OfferingBrazierTileEntity offeringBrazierTileEntity, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        final IItemHandler itemHandler = offeringBrazierTileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElseGet(ItemStackHandler::new);
        ItemStack fuel = itemHandler.getStackInSlot(0);
        ItemStack burning = itemHandler.getStackInSlot(1);
        if (!burning.isEmpty()) {
            matrixStackIn.pushPose();
            matrixStackIn.translate(0.5, 0.6, 0.5);
            itemRenderer.renderStatic(burning, ItemTransforms.TransformType.GROUND, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn);
            matrixStackIn.popPose();
        }
        if (!fuel.isEmpty()) {
            matrixStackIn.pushPose();
            matrixStackIn.translate(0.5, 0.4, 0.5);
            matrixStackIn.scale(.5f, .5f, .5f);
            matrixStackIn.mulPose(new Quaternion(-90, 0, 0, true));
            itemRenderer.renderStatic(fuel, ItemTransforms.TransformType.FIXED, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn);
            matrixStackIn.popPose();
        }
    }
}
