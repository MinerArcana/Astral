package com.alan199921.astral;

import com.alan199921.astral.entities.AstralEntityRegistry;
import com.alan199921.astral.entities.PhysicalBodyEntityRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientEventHandler {
    public static void clientSetup(FMLClientSetupEvent event) {
        EntityRendererManager rendererManager = Minecraft.getInstance().getRenderManager();
        rendererManager.register(AstralEntityRegistry.PHYSICAL_BODY_ENTITY.get(), new PhysicalBodyEntityRenderer(rendererManager));
    }
}
