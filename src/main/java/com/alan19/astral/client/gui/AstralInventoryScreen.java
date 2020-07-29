package com.alan19.astral.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DisplayEffectsScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static net.minecraft.client.gui.screen.inventory.InventoryScreen.drawEntityOnScreen;

@OnlyIn(Dist.CLIENT)
public class AstralInventoryScreen extends DisplayEffectsScreen<AstralInventoryContainer> {

    public AstralInventoryScreen(AstralInventoryContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        AstralDrawable.ASTRAL_INVENTORY_DRAWABLE.draw(guiLeft, guiTop, 175, 165);
        int i = this.guiLeft;
        int j = this.guiTop;

        if (Minecraft.getInstance().player != null) {
            drawEntityOnScreen(i + 51, j + 75, 30, (float) (i + 51) - mouseX, (float) (j + 75 - 50) - mouseY, Minecraft.getInstance().player);
        }
    }

    @Override
    public void renderBackground() {
        renderBackground(0);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground();
        drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        super.render(mouseX, mouseY, partialTicks);
        drawGuiContainerForegroundLayer(mouseX, mouseY);
        renderHoveredToolTip(mouseX, mouseY);
    }
}
