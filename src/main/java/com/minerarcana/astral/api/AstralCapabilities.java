package com.minerarcana.astral.api;

import com.minerarcana.astral.api.bodytracker.BodyTracker;
import com.minerarcana.astral.api.innerrealmchunkclaim.InnerRealmChunkClaim;
import com.minerarcana.astral.api.innerrealmcubegen.InnerRealmCubeGen;
import com.minerarcana.astral.api.innerrealmteleporter.InnerRealmTeleporter;
import com.minerarcana.astral.api.psychicinventory.PsychicInventory;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;

public class AstralCapabilities {
    public static final Capability<InnerRealmCubeGen> CUBE_GEN_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static final Capability<InnerRealmTeleporter> TELEPORTER_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static final Capability<InnerRealmChunkClaim> CHUNK_CLAIM_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static final Capability<BodyTracker> BODY_TRACKER_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static final Capability<PsychicInventory> PSYCHIC_INVENTORY_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });

    public static LazyOptional<InnerRealmChunkClaim> getChunkClaimTracker(Level world) {
        return getOverworld(world).getCapability(CHUNK_CLAIM_CAPABILITY);
    }

    public static LazyOptional<InnerRealmTeleporter> getInnerRealmTeleporter(Level world) {
        return getOverworld(world).getCapability(TELEPORTER_CAPABILITY);
    }

    public static LazyOptional<BodyTracker> getBodyTracker(Level world) {
        return getOverworld(world).getCapability(BODY_TRACKER_CAPABILITY);
    }

    public static LazyOptional<PsychicInventory> getPsychicInventory(Level level) {
        return getOverworld(level).getCapability(PSYCHIC_INVENTORY_CAPABILITY);
    }

    public static ServerLevel getOverworld(Level world) {
        return world.getServer().getLevel(Level.OVERWORLD);
    }

}