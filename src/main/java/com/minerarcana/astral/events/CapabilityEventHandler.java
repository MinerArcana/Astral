package com.minerarcana.astral.events;

import com.minerarcana.astral.Astral;
import com.minerarcana.astral.api.innerrealmcubegen.IInnerRealmCubeGen;
import com.minerarcana.astral.api.innerrealmcubegen.InnerRealmCubeGenProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Astral.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CapabilityEventHandler {
    @SubscribeEvent
    public static void registerCaps(RegisterCapabilitiesEvent event) {
        event.register(IInnerRealmCubeGen.class);
    }

    @SubscribeEvent
    public static void attachToEntity(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            event.addCapability(InnerRealmCubeGenProvider.KEY, new InnerRealmCubeGenProvider());
        }
    }
}
