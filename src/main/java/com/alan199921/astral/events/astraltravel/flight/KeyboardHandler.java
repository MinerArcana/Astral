package com.alan199921.astral.events.astraltravel.flight;

import com.alan199921.astral.Astral;
import com.alan199921.astral.network.AstralNetwork;
import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Astral.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class KeyboardHandler {

    private static boolean up = false;
    private static boolean down = false;
    private static boolean forwards = false;
    private static boolean backwards = false;
    private static boolean left = false;
    private static boolean right = false;
    private static boolean sprint = false;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            Minecraft mc = Minecraft.getInstance();
            GameSettings settings = mc.gameSettings;
            if (mc.getConnection() != null) {
                boolean upNow = settings.keyBindJump.isKeyDown();
                boolean downNow = settings.keyBindSneak.isKeyDown();
                boolean forwardsNow = settings.keyBindForward.isKeyDown();
                boolean backwardsNow = settings.keyBindBack.isKeyDown();
                boolean leftNow = settings.keyBindLeft.isKeyDown();
                boolean rightNow = settings.keyBindRight.isKeyDown();
                boolean sprintNow = settings.keyBindSprint.isKeyDown();

                if (upNow != up || downNow != down || forwardsNow != forwards || backwardsNow != backwards || leftNow != left || rightNow != right || sprintNow != sprint) {
                    up = upNow;
                    down = downNow;
                    forwards = forwardsNow;
                    backwards = backwardsNow;
                    left = leftNow;
                    right = rightNow;
                    sprint = sprintNow;

                    AstralNetwork.sendUpdateInputMessage(upNow, downNow, forwardsNow, backwardsNow, leftNow, rightNow, sprintNow);
                    InputHandler.update(mc.player, upNow, downNow, forwardsNow, backwardsNow, leftNow, rightNow, sprintNow);
                }
            }
        }
    }
}
