package com.alan199921.astral;

import com.alan199921.astral.capabilities.bodylink.BodyLinkProvider;
import com.alan199921.astral.capabilities.innerrealmchunkclaim.InnerRealmChunkClaimProvider;
import com.alan199921.astral.capabilities.innerrealmteleporter.InnerRealmTeleporterProvider;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid=Astral.MOD_ID, bus= Mod.EventBusSubscriber.Bus.FORGE)
public class CapabilitiesEventHandler {
    private static final ResourceLocation INNER_REALM_TELEPORTER = new ResourceLocation(Astral.MOD_ID, "inner_realm_teleporter");
    private static final ResourceLocation INNER_REALM_CHUNK_CLAIM = new ResourceLocation(Astral.MOD_ID, "inner_realm_chunk_claim");
    private static final ResourceLocation BODY_LINK = new ResourceLocation(Astral.MOD_ID, "body_link");

    @SubscribeEvent
    public static void onAttachCapabilitiesToWorld(AttachCapabilitiesEvent<World> e)
    {
        World world = e.getObject();
        if (world != null) {
            e.addCapability(INNER_REALM_TELEPORTER, new InnerRealmTeleporterProvider());
            e.addCapability(INNER_REALM_CHUNK_CLAIM, new InnerRealmChunkClaimProvider());
        }
    }

    @SubscribeEvent
    public static void onAttachCapabilitiesToEntity(AttachCapabilitiesEvent<Entity> e){
        e.addCapability(BODY_LINK, new BodyLinkProvider());
    }

}
