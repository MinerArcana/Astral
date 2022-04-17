package com.alan19.astral.api;

import com.alan19.astral.api.bodytracker.IBodyTracker;
import com.alan19.astral.api.constructtracker.IConstructTracker;
import com.alan19.astral.api.heightadjustment.IHeightAdjustmentCapability;
import com.alan19.astral.api.innerrealmchunkclaim.IInnerRealmChunkClaim;
import com.alan19.astral.api.innerrealmteleporter.IInnerRealmTeleporter;
import com.alan19.astral.api.intentiontracker.IBeamTracker;
import com.alan19.astral.api.intentiontracker.intentiontrackerbehaviors.IIntentionTrackerBehavior;
import com.alan19.astral.api.psychicinventory.IPsychicInventory;
import com.alan19.astral.api.sleepmanager.ISleepManager;
import com.alan19.astral.mentalconstructs.MentalConstructType;
import com.google.common.collect.Maps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;

import javax.annotation.Nullable;
import java.util.Map;

public class AstralAPI {
    public static final Lazy<IForgeRegistry<MentalConstructType>> MENTAL_CONSTRUCT_TYPES = Lazy.of(() -> RegistryManager.ACTIVE.getRegistry(MentalConstructType.class));
    private static final Map<Block, IIntentionTrackerBehavior> INTENTION_TRACKER_BEHAVIORS = Maps.newHashMap();

    public static final Capability<ISleepManager> SLEEP_MANAGER_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });

    public static final Capability<IPsychicInventory> PSYCHIC_INVENTORY_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });

    public static final Capability<IConstructTracker> CONSTRUCT_TRACKER_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });

    public static final Capability<IBodyTracker> BODY_TRACKER_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });

    public static final Capability<IBeamTracker> BEAM_TRACKER_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });

    public static final Capability<IInnerRealmChunkClaim> INNER_REALM_CHUNK_CLAIM_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });

    public static final Capability<IInnerRealmTeleporter> INNER_REALM_TELEPORTER_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });

    public static final Capability<IHeightAdjustmentCapability> HEIGHT_ADJUSTMENT_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });

    public static LazyOptional<IBodyTracker> getBodyTracker(ServerLevel world) {
        return getOverworld(world).getCapability(BODY_TRACKER_CAPABILITY);
    }

    public static LazyOptional<IPsychicInventory> getOverworldPsychicInventory(ServerLevel world) {
        return getOverworld(world).getCapability(PSYCHIC_INVENTORY_CAPABILITY);
    }

    public static LazyOptional<ISleepManager> getSleepManager(Player playerEntity) {
        return playerEntity.getCapability(SLEEP_MANAGER_CAPABILITY);
    }

    public static LazyOptional<IConstructTracker> getConstructTracker(ServerLevel world) {
        return getOverworld(world).getCapability(CONSTRUCT_TRACKER_CAPABILITY);
    }

    public static LazyOptional<IInnerRealmChunkClaim> getChunkClaimTracker(ServerLevel world) {
        return getOverworld(world).getCapability(INNER_REALM_CHUNK_CLAIM_CAPABILITY);
    }

    public static LazyOptional<IInnerRealmTeleporter> getInnerRealmTeleporter(ServerLevel world) {
        return getOverworld(world).getCapability(INNER_REALM_TELEPORTER_CAPABILITY);
    }

    public static ServerLevel getOverworld(ServerLevel world) {
        return world.getServer().getLevel(Level.OVERWORLD);
    }

    @Nullable
    public static IIntentionTrackerBehavior getIntentionTrackerBehavior(Block block) {
        return INTENTION_TRACKER_BEHAVIORS.get(block);
    }

    public static void addIntentionTrackerBehavior(Block block, IIntentionTrackerBehavior intentionTrackerBehavior) {
        INTENTION_TRACKER_BEHAVIORS.put(block, intentionTrackerBehavior);
    }

    public static Map<Block, IIntentionTrackerBehavior> getIntentionTrackerBehaviors() {
        return INTENTION_TRACKER_BEHAVIORS;
    }
}
