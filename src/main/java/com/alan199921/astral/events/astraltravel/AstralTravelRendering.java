package com.alan199921.astral.events.astraltravel;

import com.alan199921.astral.Astral;
import com.alan199921.astral.api.AstralAPI;
import com.alan199921.astral.effects.AstralEffects;
import com.alan199921.astral.events.AstralRendering;
import com.alan199921.astral.util.RenderingUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Astral.MOD_ID)
public class AstralTravelRendering {

    @SubscribeEvent
    public static void reloadWhenAstralTravelEnds(TickEvent.ClientTickEvent event) {
        if (Minecraft.getInstance().player != null && RenderingUtils.shouldReloadRenderers() && !Minecraft.getInstance().player.isPotionActive(AstralEffects.ASTRAL_TRAVEL)) {
            RenderingUtils.reloadRenderers();
            RenderingUtils.setReloadRenderers(false);
        }
    }

    @SubscribeEvent
    public static void renderAstralEntities(RenderLivingEvent<LivingEntity, EntityModel<LivingEntity>> event) {
        if (Minecraft.getInstance().player != null && !TravelEffects.isAstralTravelActive(Minecraft.getInstance().player) && TravelEffects.isAstralTravelActive(event.getEntity())) {
            event.setCanceled(true);
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
            if (TravelEffects.isAstralTravelActive(playerEntity)) {
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
}
