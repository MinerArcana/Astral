package com.alan199921.astral;

import com.alan199921.astral.capabilities.inner_realm_teleporter.InnerRealmTeleporterProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid=Astral.MOD_ID, bus= Mod.EventBusSubscriber.Bus.FORGE)
public class CapabilitiesEventHandler {
    private static final ResourceLocation INNER_REALM_TELEPORTER = new ResourceLocation(Astral.MOD_ID, "inner_realm_teleporter");

    @SubscribeEvent
    public static void onAttachCapabilitiesToWorld(AttachCapabilitiesEvent<World> e)
    {
        World world = e.getObject();
        if (world != null)
        {
            e.addCapability(INNER_REALM_TELEPORTER, new InnerRealmTeleporterProvider());
            System.out.println("Attached inner realm teleporter capability!");
        }
    }
}
