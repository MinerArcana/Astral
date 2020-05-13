package com.alan199921.astral.events;

import com.alan199921.astral.Astral;
import com.alan199921.astral.api.AstralAPI;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Astral.MOD_ID)
public class MentalConstructEvents {
    @SubscribeEvent
    public static void passiveMentalConstructs(TickEvent.PlayerTickEvent event) {
        if (event.player instanceof ServerPlayerEntity) {
            AstralAPI.getConstructTracker(((ServerPlayerEntity) event.player).getServerWorld()).ifPresent(tracker -> tracker.getMentalConstructsForPlayer(event.player).performAllPassiveEffects(event.player));
        }
    }
}
