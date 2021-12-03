package com.alan19.astral.events.astraltravel;

import com.alan19.astral.Astral;
import com.alan19.astral.api.AstralAPI;
import com.alan19.astral.effects.AstralEffects;
import com.alan19.astral.events.AstralHealthBarRendering;
import com.alan19.astral.util.RenderingUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Astral.MOD_ID, value = Dist.CLIENT)
public class AstralTravelRendering {

    @SubscribeEvent
    public static void reloadWhenAstralTravelEnds(TickEvent.ClientTickEvent event) {
        if (Minecraft.getInstance().player != null && RenderingUtils.shouldReloadRenderers() && !Minecraft.getInstance().player.hasEffect(AstralEffects.ASTRAL_TRAVEL.get())) {
            RenderingUtils.reloadRenderers();
            RenderingUtils.setReloadRenderers(false);
        }
    }

    @SubscribeEvent
    public static void renderAstralEntities(RenderLivingEvent<LivingEntity, EntityModel<LivingEntity>> event) {
        if (Minecraft.getInstance().player != null && !TravelEffects.isEntityAstral(Minecraft.getInstance().player) && TravelEffects.isEntityAstral(event.getEntity())) {
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
        if (minecraft.getCameraEntity() instanceof PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity) minecraft.getCameraEntity();
            if (TravelEffects.isEntityAstral(playerEntity)) {
                //Cancel rendering of hunger bar
                if (event.getType() == RenderGameOverlayEvent.ElementType.FOOD) {
                    event.setCanceled(true);
                }
                if (event.getType() == RenderGameOverlayEvent.ElementType.HEALTH) {
                    event.setCanceled(true);
                    AstralHealthBarRendering.renderAstralHearts(event.getMatrixStack(), minecraft, playerEntity);
                }
            }
            playerEntity.getCapability(AstralAPI.sleepManagerCapability).ifPresent(iSleepManager -> {
                if (playerEntity.hasEffect(AstralEffects.ASTRAL_TRAVEL.get()) && !iSleepManager.isEntityTraveling()) {
                    AstralHealthBarRendering.renderAstralScreenFade(event.getMatrixStack(), iSleepManager.getSleep());
                }
            });
        }
    }
}
