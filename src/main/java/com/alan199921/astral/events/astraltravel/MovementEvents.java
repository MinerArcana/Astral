package com.alan199921.astral.events.astraltravel;

import com.alan199921.astral.Astral;
import com.alan199921.astral.api.AstralAPI;
import com.alan199921.astral.api.innerrealmteleporter.InnerRealmTeleporterProvider;
import com.alan199921.astral.api.sleepmanager.ISleepManager;
import com.alan199921.astral.api.sleepmanager.SleepManager;
import com.alan199921.astral.effects.AstralEffects;
import com.alan199921.astral.flight.FlightHandler;
import com.alan199921.astral.util.RenderingUtils;
import net.minecraft.entity.MoverType;
import net.minecraft.util.MovementInput;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Astral.MOD_ID)
public class MovementEvents {
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

    @SubscribeEvent
    public static void astralFlight(TickEvent.PlayerTickEvent event) {
        if (event.player.isPotionActive(AstralEffects.ASTRAL_TRAVEL)) {
            final LazyOptional<ISleepManager> iSleepManagerLazyOptional = event.player.getCapability(AstralAPI.sleepManagerCapability);
            if (iSleepManagerLazyOptional.isPresent()) {
                final ISleepManager sleepManager = iSleepManagerLazyOptional.orElseGet(SleepManager::new);
                if (sleepManager.isEntityTraveling()) {
                    FlightHandler.handleAstralFlight(event.player);
                }
                else {
                    sleepManager.addSleep();
                    if (sleepManager.isEntityTraveling()) {
                        StartAndEndHandling.spawnPhysicalBody(event.player);
                        final Boolean goingToInnerRealm = iSleepManagerLazyOptional.map(ISleepManager::isGoingToInnerRealm).orElseGet(() -> false);
                        if (Boolean.TRUE.equals(goingToInnerRealm)) {
                            event.player.getEntityWorld().getCapability(InnerRealmTeleporterProvider.TELEPORTER_CAPABILITY).ifPresent(cap -> cap.teleport(event.player));
                            iSleepManagerLazyOptional.ifPresent(iSleepManager -> iSleepManager.setGoingToInnerRealm(false));
                        }
                        else {
                            event.player.setMotion(0, 10, 0);
                            event.player.move(MoverType.SELF, new Vec3d(0, 1, 0));
                        }
                        if (event.player.world.isRemote()) {
                            RenderingUtils.reloadRenderers();
                        }
                    }
                }
            }

        }
    }
}
