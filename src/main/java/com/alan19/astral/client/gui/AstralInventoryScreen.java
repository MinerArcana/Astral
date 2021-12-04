package com.alan19.astral.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class AstralInventoryScreen extends EffectRenderingInventoryScreen<AstralInventoryContainer> {

    public AstralInventoryScreen(AstralInventoryContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    protected void renderBg(@Nonnull PoseStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        AstralDrawable.ASTRAL_INVENTORY_DRAWABLE.draw(leftPos, topPos, 175, 165);
        int i = this.leftPos;
        int j = this.topPos;

        if (Minecraft.getInstance().player != null) {
            InventoryScreen.renderEntityInInventory(i + 51, j + 75, 30, (float) (i + 51) - mouseX, (float) (j + 75 - 50) - mouseY, Minecraft.getInstance().player);
        }
    }

    @Override
    public void render(@Nonnull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrixStack);
        renderBg(matrixStack, partialTicks, mouseX, mouseY);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        renderLabels(matrixStack, mouseX, mouseY);
        renderTooltip(matrixStack, mouseX, mouseY);
    }
}
