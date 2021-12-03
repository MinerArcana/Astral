package com.alan19.astral.events.astraltravel.flight;

import com.alan19.astral.Astral;
import com.alan19.astral.effects.AstralEffects;
import com.alan19.astral.network.AstralNetwork;
import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MovementInput;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Astral.MOD_ID, value = Dist.CLIENT)
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
            GameSettings settings = mc.options;
            if (mc.getConnection() != null) {
                boolean upNow = settings.keyJump.isDown();
                boolean downNow = settings.keyShift.isDown();
                boolean forwardsNow = settings.keyUp.isDown();
                boolean backwardsNow = settings.keyDown.isDown();
                boolean leftNow = settings.keyLeft.isDown();
                boolean rightNow = settings.keyRight.isDown();
                boolean sprintNow = settings.keySprint.isDown();

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

    @SubscribeEvent
    public static void cancelRegularMovement(InputUpdateEvent event) {
        if (event.getPlayer().hasEffect(AstralEffects.ASTRAL_TRAVEL.get())) {
            final MovementInput movementInput = event.getMovementInput();
            movementInput.down = false;
            movementInput.up = false;
            movementInput.forwardImpulse = 0;
            movementInput.right = false;
            movementInput.leftImpulse = 0;
        }
    }
}
