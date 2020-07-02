package com.alan19.astral.client.gui;

import com.alan19.astral.Astral;
import com.alan19.astral.effects.AstralEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class GuiEventHandler {
    @SubscribeEvent
    public static void addWidgetToInventory(GuiScreenEvent.InitGuiEvent.Post event) {
        Screen screen = event.getGui();
        if (Minecraft.getInstance().player != null && (screen instanceof InventoryScreen || screen instanceof CreativeScreen && !Minecraft.getInstance().player.isPotionActive(AstralEffects.ASTRAL_TRAVEL.get()))) {
            ContainerScreen<?> gui = (ContainerScreen<?>) screen;
            event.addWidget(new AstralInventoryButton(gui, gui.getGuiLeft() + 150, gui.height / 2 - 22, 20, 18, 0, 0, 19, new ResourceLocation(Astral.MOD_ID, "textures/gui/astral_inventory_button.png")));
        }
    }
}
