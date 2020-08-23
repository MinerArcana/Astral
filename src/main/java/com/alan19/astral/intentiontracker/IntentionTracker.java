package com.alan19.astral.intentiontracker;

import com.alan19.astral.Astral;
import com.alan19.astral.ClientSetup;
import com.alan19.astral.network.AstralNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Astral.MOD_ID)
public class IntentionTracker {
    @SubscribeEvent
    public static void onInput(InputEvent event){
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (player != null && ClientSetup.INTENTION_TRACKER_BUTTON.isPressed()) {
            AstralNetwork.sendIntentionBeam();
        }
    }

    @SubscribeEvent
    public static void onKeyReleased(GuiScreenEvent.KeyboardKeyReleasedEvent event){
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (player != null && !ClientSetup.INTENTION_TRACKER_BUTTON.isPressed()) {
            AstralNetwork.deleteIntentionBeam();
        }
    }
}
