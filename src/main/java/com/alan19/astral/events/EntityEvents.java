package com.alan19.astral.events;

import com.alan19.astral.Astral;
import com.alan19.astral.entity.ghost.GhostEntity;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Astral.MOD_ID)
public class EntityEvents {

    @SubscribeEvent
    public static void attemptGhostDismount(EntityMountEvent event) {
        if (event.isDismounting() && event.getEntityBeingMounted() instanceof GhostEntity && event.getEntityBeingMounted().isAlive()) {
            event.setCanceled(true);
        }
    }
}
