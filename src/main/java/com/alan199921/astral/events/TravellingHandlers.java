package com.alan199921.astral.events;

import net.minecraft.potion.Effects;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TravellingHandlers {
    @SubscribeEvent
    public void doNotTargetAstrals(LivingSetAttackTargetEvent event){
        if (event.getTarget().isPotionActive(Effects.INVISIBILITY)){
            event.setCanceled(true);
        }
    }
    @SubscribeEvent
    public void nullifyAstralDamage(LivingAttackEvent event){
        if (event.getEntityLiving().isPotionActive(Effects.INVISIBILITY) && !event.getSource().damageType.equals("astral")){
            event.setCanceled(true);
        }
    }
}
