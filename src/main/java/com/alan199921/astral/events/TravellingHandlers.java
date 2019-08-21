package com.alan199921.astral.events;

import com.alan199921.astral.Astral;
import com.alan199921.astral.util.MobHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
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
        if (event.getTarget() != null && event.getTarget().isPotionActive(Effects.FIRE_RESISTANCE)) {
            System.out.println(event.getEntityLiving().getName().getString() + " is no longer targetting you!");
            MobHelper.resetTarget(event.getEntityLiving());
        }
    }

    @SubscribeEvent
    public static void nullifyAstralDamage(LivingAttackEvent event) {
        LivingEntity livingSource = (LivingEntity) event.getSource().getImmediateSource();

        if (livingSource != null && isAstralVsNonAstral(livingSource, event.getEntityLiving())){
            event.setCanceled(true);
        }
    }

    //Function for detecting if an Astral entity is interacting with a non astral entity
    private static boolean isAstralVsNonAstral(LivingEntity mobA, LivingEntity mobB) {
        Effect astralEffect = Effects.FIRE_RESISTANCE;
        return mobA.isPotionActive(astralEffect) && !mobB.isPotionActive(astralEffect) || !mobA.isPotionActive(astralEffect) && mobB.isPotionActive(astralEffect);
    }

    @SubscribeEvent
    public static void renderAstralEntities(RenderLivingEvent event) {
        if (!Minecraft.getInstance().player.isPotionActive(Effects.FIRE_RESISTANCE) && event.getEntity().isPotionActive(Effects.FIRE_RESISTANCE)) {
            System.out.println("Making " + event.getEntity().getName().getString() + " invisible");
            event.setCanceled(true);
        }
    }
}

