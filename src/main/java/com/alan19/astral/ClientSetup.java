package com.alan19.astral;

import com.alan19.astral.client.gui.AstralContainers;
import com.alan19.astral.client.gui.AstralInventoryScreen;
import com.alan19.astral.client.gui.GuiEventHandler;
import com.alan19.astral.entity.AstralEntities;
import com.alan19.astral.renderer.entity.CrystalSpiderRenderer;
import com.alan19.astral.renderer.entity.IntentionBeamRenderer;
import com.alan19.astral.renderer.entity.PhysicalBodyEntityRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;

import java.util.stream.Stream;

public class ClientSetup {
    private static final String ASTRAL_KEY_CATEGORY = "key.categories.astral";

    public static final KeyBinding INTENTION_TRACKER_BUTTON = new KeyBinding("keys.astral.intention_tracker", KeyConflictContext.IN_GAME, InputMappings.Type.MOUSE, GLFW.GLFW_MOUSE_BUTTON_4, ASTRAL_KEY_CATEGORY);

    public static void registerKeybinds(){
        Stream.of(INTENTION_TRACKER_BUTTON).forEach(ClientRegistry::registerKeyBinding);
    }

    public static void clientSetup(FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(GuiEventHandler.class);
        EntityRendererManager rendererManager = Minecraft.getInstance().getEntityRenderDispatcher();
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        rendererManager.register(AstralEntities.PHYSICAL_BODY_ENTITY.get(), new PhysicalBodyEntityRenderer(rendererManager));
        rendererManager.register(AstralEntities.CRYSTAL_SPIDER.get(), new CrystalSpiderRenderer(rendererManager));
        rendererManager.register(AstralEntities.CRYSTAL_WEB_PROJECTILE_ENTITY.get(), new SpriteRenderer<>(rendererManager, itemRenderer, 1, true));
        rendererManager.register(AstralEntities.INTENTION_BEAM_ENTITY.get(), new IntentionBeamRenderer(rendererManager));
        ScreenManager.register(AstralContainers.ASTRAL_INVENTORY_CONTAINER.get(), AstralInventoryScreen::new);
        registerKeybinds();
    }
}
