package com.alan19.astral.events.astraltravel;

import com.alan19.astral.Astral;
import com.alan19.astral.api.AstralAPI;
import com.alan19.astral.api.sleepmanager.ISleepManager;
import com.alan19.astral.api.sleepmanager.SleepManager;
import com.alan19.astral.effects.AstralEffects;
import com.alan19.astral.events.astraltravel.flight.FlightHandler;
import com.alan19.astral.util.RenderingUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Astral.MOD_ID)
public class MovementEvents {

    @SubscribeEvent
    public static void astralFlight(TickEvent.PlayerTickEvent event) {
        if (event.player.hasEffect(AstralEffects.ASTRAL_TRAVEL.get())) {
            final LazyOptional<ISleepManager> iSleepManagerLazyOptional = event.player.getCapability(AstralAPI.sleepManagerCapability);
            if (iSleepManagerLazyOptional.isPresent()) {
                final ISleepManager sleepManager = iSleepManagerLazyOptional.orElseGet(SleepManager::new);
                if (sleepManager.isEntityTraveling()) {
                    FlightHandler.handleAstralFlight(event.player);
                }
                else {
                    handleSleep(event.player, sleepManager);
                }
            }

        }
    }

    public static void handleSleep(PlayerEntity player, ISleepManager sleepManager) {
        sleepManager.addSleep();
        player.getEntityData().set(Entity.DATA_POSE, Pose.SLEEPING);
        if (sleepManager.isEntityTraveling()) {
            finishSleeping(player, sleepManager);
        }
    }

    public static void finishSleeping(PlayerEntity player, ISleepManager sleepManager) {
        if (player instanceof ServerPlayerEntity) {
            StartAndEndHandling.spawnPhysicalBody((ServerPlayerEntity) player);
        }
        final boolean goingToInnerRealm = sleepManager.isGoingToInnerRealm();
        if (goingToInnerRealm) {
            if (player instanceof ServerPlayerEntity) {
                AstralAPI.getInnerRealmTeleporter((ServerWorld) player.getCommandSenderWorld()).ifPresent(cap -> cap.teleport((ServerPlayerEntity) player));
            }
            sleepManager.setGoingToInnerRealm(false);
        }
        else {
            player.setDeltaMovement(0, 10, 0);
            player.move(MoverType.SELF, new Vector3d(0, 1, 0));
        }
        if (player.level.isClientSide()) {
            RenderingUtils.reloadRenderers();
        }
    }
}
