package com.alan199921.astral.events;

import com.alan199921.astral.Astral;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Effects;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Astral.MOD_ID)
public class TravellingHandlers {
    @SubscribeEvent
    public static void doNotTargetAstrals(LivingSetAttackTargetEvent event) {
        if (event.getEntityLiving() instanceof MobEntity && isAstralVsNonAstral(event.getTarget(), event.getEntityLiving())) {
            MobEntity mobEntity = (MobEntity) event.getEntityLiving();
            mobEntity.setAttackTarget(null);
        }
    }

    @SubscribeEvent
    public static void nullifyAstralDamage(LivingAttackEvent event) {
        if (event.getSource().getTrueSource() != null && !event.getSource().getTrueSource().isLiving() && !event.getSource().getDamageType().equals("astral") && event.getEntityLiving().isPotionActive(Effects.FIRE_RESISTANCE)){
            event.setCanceled(true);
        }
        if (event.getSource().getTrueSource() != null && event.getSource().getTrueSource().isLiving()){
            LivingEntity livingSource = (LivingEntity) event.getSource().getImmediateSource();

            if (isAstralVsNonAstral(livingSource, event.getEntityLiving())){
                event.setCanceled(true);
            }
        }


    }

    //Function for detecting if an Astral entity is interacting with a non astral entity
    private static boolean isAstralVsNonAstral(LivingEntity mobA, LivingEntity mobB) {
        if (mobA == null || mobB == null){
            return false;
        }
        Effect astralEffect = Effects.FIRE_RESISTANCE;
        return mobA.isPotionActive(astralEffect) && !mobB.isPotionActive(astralEffect) || !mobA.isPotionActive(astralEffect) && mobB.isPotionActive(astralEffect);
    }

    @SubscribeEvent
    public static void renderAstralEntities(RenderLivingEvent event) {
        if (!Minecraft.getInstance().player.isPotionActive(Effects.FIRE_RESISTANCE) && event.getEntity().isPotionActive(Effects.FIRE_RESISTANCE)) {
            System.out.println("Making " + event.getEntity().getName().getString() + " invisible!");
            event.setCanceled(true);
        }
    }
}

