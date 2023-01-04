package com.minerarcana.astral.events;

import com.minerarcana.astral.Astral;
import com.minerarcana.astral.effect.AstralEffects;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Astral.MOD_ID)
public class AstralTravelEffects {
    @SubscribeEvent
    public static void negateAstralFallDamage(LivingFallEvent event) {
        if (event.getEntity().hasEffect(AstralEffects.ASTRAL_TRAVEL.get())) {
            event.setCanceled(true);
        }
    }
}
