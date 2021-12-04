package com.alan19.astral.events;

import com.alan19.astral.Astral;
import com.alan19.astral.api.AstralAPI;
import com.alan19.astral.effects.AstralEffects;
import com.alan19.astral.mentalconstructs.AstralMentalConstructs;
import com.alan19.astral.mentalconstructs.MentalConstruct;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;

@Mod.EventBusSubscriber(modid = Astral.MOD_ID)
public class MentalConstructEvents {
    @SubscribeEvent
    public static void passiveMentalConstructs(TickEvent.PlayerTickEvent event) {
        if (event.player instanceof ServerPlayer) {
            AstralAPI.getConstructTracker(((ServerPlayer) event.player).getLevel()).ifPresent(tracker -> tracker.getMentalConstructsForPlayer(event.player).performAllPassiveEffects(event.player));
        }
    }

    @SubscribeEvent
    public static void libraryMinXP(LivingHealEvent event) {
        if (event.getEntity() instanceof ServerPlayer && !((ServerPlayer) event.getEntity()).hasEffect(AstralEffects.ASTRAL_TRAVEL.get())) {
            ServerPlayer playerEntity = (ServerPlayer) event.getEntity();
            AstralAPI.getConstructTracker(playerEntity.getLevel()).ifPresent(tracker -> {
                final Map<ResourceLocation, MentalConstruct> mentalConstructs = tracker.getMentalConstructsForPlayer(playerEntity).getMentalConstructs();
                if (mentalConstructs.containsKey(AstralMentalConstructs.LIBRARY.getId())) {
                    final MentalConstruct libraryEntry = mentalConstructs.get(AstralMentalConstructs.LIBRARY.getId());
                    libraryEntry.performEffect(playerEntity, libraryEntry.getLevel());
                }
            });
        }
    }
}
