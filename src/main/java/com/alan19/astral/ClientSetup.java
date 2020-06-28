package com.alan19.astral;

import com.alan19.astral.client.gui.AstralContainers;
import com.alan19.astral.client.gui.AstralInventoryScreen;
import com.alan19.astral.client.gui.GuiEventHandler;
import com.alan19.astral.entity.AstralEntities;
import com.alan19.astral.renderer.entity.CrystalSpiderRenderer;
import com.alan19.astral.renderer.entity.PhysicalBodyEntityRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientSetup {
    public static void clientSetup(FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(GuiEventHandler.class);
        EntityRendererManager rendererManager = Minecraft.getInstance().getRenderManager();
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        rendererManager.register(AstralEntities.PHYSICAL_BODY_ENTITY.get(), new PhysicalBodyEntityRenderer(rendererManager));
        rendererManager.register(AstralEntities.CRYSTAL_SPIDER.get(), new CrystalSpiderRenderer(rendererManager));
        rendererManager.register(AstralEntities.CRYSTAL_WEB_PROJECTILE_ENTITY.get(), new SpriteRenderer<>(rendererManager, itemRenderer, 1, true));
        ScreenManager.registerFactory(AstralContainers.ASTRAL_INVENTORY_CONTAINER.get(), AstralInventoryScreen::new);
    }
}
