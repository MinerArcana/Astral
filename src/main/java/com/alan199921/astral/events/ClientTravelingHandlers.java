package com.alan199921.astral.events;

import com.alan199921.astral.Astral;
import com.alan199921.astral.api.AstralAPI;
import com.alan199921.astral.effects.AstralEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.MovementInput;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.alan199921.astral.events.TravelingHandlers.isAstralTravelActive;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Astral.MOD_ID)
public class ClientTravelingHandlers {
    @SubscribeEvent
    public static void cancelRegularMovement(InputUpdateEvent event) {
        if (event.getPlayer().isPotionActive(AstralEffects.ASTRAL_TRAVEL)) {
            final MovementInput movementInput = event.getMovementInput();
            movementInput.backKeyDown = false;
            movementInput.forwardKeyDown = false;
            movementInput.moveForward = 0;
            movementInput.rightKeyDown = false;
            movementInput.moveStrafe = 0;
        }
    }

    /**
     * Make hearts white and remove the hunger bar when the player has the Astral potion effect
     *
     * @param event The game overlay render event to be cancelled if Astral Travel is active and the event is rendering food or health
     */
    @SubscribeEvent(receiveCanceled = true)
    public static void astralHUDRendering(RenderGameOverlayEvent.Pre event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.getRenderViewEntity() instanceof PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity) minecraft.getRenderViewEntity();
            if (isAstralTravelActive(playerEntity)) {
                //Cancel rendering of hunger bar
                if (event.getType() == RenderGameOverlayEvent.ElementType.FOOD) {
                    event.setCanceled(true);
                }
                if (event.getType() == RenderGameOverlayEvent.ElementType.HEALTH) {
                    event.setCanceled(true);
                    AstralRendering.renderAstralHearts(minecraft, playerEntity);
                }
            }
            playerEntity.getCapability(AstralAPI.sleepManagerCapability).ifPresent(iSleepManager -> {
                if (playerEntity.isPotionActive(AstralEffects.ASTRAL_TRAVEL) && !iSleepManager.isEntityTraveling()) {
                    AstralRendering.renderAstralScreenFade(iSleepManager.getSleep());
                }
            });
        }
    }

    @SubscribeEvent
    public static void renderAstralEntities(RenderLivingEvent event) {
        if (!isAstralTravelActive(Minecraft.getInstance().player) && isAstralTravelActive(event.getEntity())) {
            event.setCanceled(true);
        }
    }


}
