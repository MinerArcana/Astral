package com.alan19.astral.client.gui;

import com.alan19.astral.Astral;
import com.alan19.astral.effects.AstralEffects;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class GuiEventHandler {
    @SubscribeEvent
    public static void addWidgetToInventory(GuiScreenEvent.InitGuiEvent.Post event) {
        Screen screen = event.getGui();
        if (Minecraft.getInstance().player != null && !Minecraft.getInstance().player.hasEffect(AstralEffects.ASTRAL_TRAVEL.get()) && (screen instanceof InventoryScreen)) {
            AbstractContainerScreen<?> gui = (AbstractContainerScreen<?>) screen;
            final AstralInventoryButton button = new AstralInventoryButton(gui, gui.getGuiLeft() + 27, gui.height / 2 - 17, 9, 9, 0, 0, 9, new ResourceLocation(Astral.MOD_ID, "textures/gui/astral_inventory_button.png"));
            event.addWidget(button);

            final Advancement stepsOnTheWind = Minecraft.getInstance().player.connection.getAdvancements().getAdvancements().get(new ResourceLocation(Astral.MOD_ID, "steps_on_the_wind"));
            final AdvancementProgress stepsOnTheWindProgress = Minecraft.getInstance().player.connection.getAdvancements().progress.get(stepsOnTheWind);
            if (stepsOnTheWindProgress != null) {
                button.visible = stepsOnTheWindProgress.isDone();
            }
        }
    }
}
