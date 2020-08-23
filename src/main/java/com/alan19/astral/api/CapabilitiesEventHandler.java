package com.alan19.astral.api;

import com.alan19.astral.Astral;
import com.alan19.astral.api.bodylink.BodyLinkProvider;
import com.alan19.astral.api.bodytracker.BodyTrackerProvider;
import com.alan19.astral.api.constructtracker.ConstructTrackerProvider;
import com.alan19.astral.api.heightadjustment.HeightAdjustmentProvider;
import com.alan19.astral.api.innerrealmchunkclaim.InnerRealmChunkClaimProvider;
import com.alan19.astral.api.innerrealmteleporter.InnerRealmTeleporterProvider;
import com.alan19.astral.api.intentiontracker.BeamTracker;
import com.alan19.astral.api.intentiontracker.BeamTrackerProvider;
import com.alan19.astral.api.psychicinventory.PsychicInventoryProvider;
import com.alan19.astral.api.sleepmanager.SleepManagerProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Astral.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CapabilitiesEventHandler {
    private static final ResourceLocation INNER_REALM_TELEPORTER = new ResourceLocation(Astral.MOD_ID, "inner_realm_teleporter");
    private static final ResourceLocation INNER_REALM_CHUNK_CLAIM = new ResourceLocation(Astral.MOD_ID, "inner_realm_chunk_claim");
    private static final ResourceLocation BODY_LINK = new ResourceLocation(Astral.MOD_ID, "body_link");
    private static final ResourceLocation HEIGHT_ADJUSTMENT = new ResourceLocation(Astral.MOD_ID, "height_adjustment");
    private static final ResourceLocation PSYCHIC_INVENTORY = new ResourceLocation(Astral.MOD_ID, "psychic_inventory");
    private static final ResourceLocation SLEEP_MANAGER = new ResourceLocation(Astral.MOD_ID, "sleep_manager");
    private static final ResourceLocation CONSTRUCT_TRACKER = new ResourceLocation(Astral.MOD_ID, "construct_tracker");
    private static final ResourceLocation BODY_TRACKER = new ResourceLocation(Astral.MOD_ID, "body_tracker");
    private static final ResourceLocation BEAM_TRACKER = new ResourceLocation(Astral.MOD_ID, "beam_tracker");

    @SubscribeEvent
    public static void onAttachCapabilitiesToWorld(AttachCapabilitiesEvent<World> e) {
        World world = e.getObject();
        if (world != null) {
            e.addCapability(INNER_REALM_TELEPORTER, new InnerRealmTeleporterProvider());
            e.addCapability(INNER_REALM_CHUNK_CLAIM, new InnerRealmChunkClaimProvider());
            e.addCapability(PSYCHIC_INVENTORY, new PsychicInventoryProvider());
            e.addCapability(CONSTRUCT_TRACKER, new ConstructTrackerProvider());
            e.addCapability(BODY_TRACKER, new BodyTrackerProvider());
        }
    }

    @SubscribeEvent
    public static void onAttachCapabilitiesToEntity(AttachCapabilitiesEvent<Entity> e) {
        //For player capabilities
        if (e.getObject() instanceof PlayerEntity) {
            e.addCapability(HEIGHT_ADJUSTMENT, new HeightAdjustmentProvider());
            e.addCapability(SLEEP_MANAGER, new SleepManagerProvider());
            e.addCapability(BODY_LINK, new BodyLinkProvider());
            e.addCapability(BEAM_TRACKER, new BeamTrackerProvider());
        }
    }

}
