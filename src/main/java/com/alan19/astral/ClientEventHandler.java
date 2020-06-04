package com.alan19.astral;

import com.alan19.astral.entities.AstralEntities;
import com.alan19.astral.renderer.entity.CrystalSpiderRenderer;
import com.alan19.astral.renderer.entity.PhysicalBodyEntityRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientEventHandler {
    public static void clientSetup(FMLClientSetupEvent event) {
        EntityRendererManager rendererManager = Minecraft.getInstance().getRenderManager();
        rendererManager.register(AstralEntities.PHYSICAL_BODY_ENTITY.get(), new PhysicalBodyEntityRenderer(rendererManager));
        rendererManager.register(AstralEntities.CRYSTAL_SPIDER.get(), new CrystalSpiderRenderer(rendererManager));
    }
}
