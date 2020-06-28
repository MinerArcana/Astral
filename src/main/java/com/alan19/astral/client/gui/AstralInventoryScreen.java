package com.alan19.astral.client.gui;

import net.minecraft.client.gui.DisplayEffectsScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AstralInventoryScreen extends DisplayEffectsScreen<AstralInventoryContainer> {

    public AstralInventoryScreen(AstralInventoryContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        AstralDrawable.ASTRAL_INVENTORY_DRAWABLE.draw(guiLeft, guiTop, 175, 165);
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
