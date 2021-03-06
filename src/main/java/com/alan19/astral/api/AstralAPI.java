package com.alan19.astral.api;

import com.alan19.astral.api.bodytracker.IBodyTracker;
import com.alan19.astral.api.constructtracker.IConstructTracker;
import com.alan19.astral.api.innerrealmchunkclaim.IInnerRealmChunkClaim;
import com.alan19.astral.api.innerrealmteleporter.IInnerRealmTeleporter;
import com.alan19.astral.api.intentiontracker.IBeamTracker;
import com.alan19.astral.api.intentiontracker.intentiontrackerbehaviors.IIntentionTrackerBehavior;
import com.alan19.astral.api.psychicinventory.IPsychicInventory;
import com.alan19.astral.api.sleepmanager.ISleepManager;
import com.alan19.astral.mentalconstructs.MentalConstructType;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;

import javax.annotation.Nullable;
import java.util.Map;

public class AstralAPI {
    public static final Lazy<IForgeRegistry<MentalConstructType>> MENTAL_CONSTRUCT_TYPES = Lazy.of(() -> RegistryManager.ACTIVE.getRegistry(MentalConstructType.class));
    private static final Map<Block, IIntentionTrackerBehavior> INTENTION_TRACKER_BEHAVIORS = Maps.newHashMap();

    @CapabilityInject(ISleepManager.class)
    public static Capability<ISleepManager> sleepManagerCapability;

    @CapabilityInject(IPsychicInventory.class)
    public static Capability<IPsychicInventory> psychicInventoryCapability;

    @CapabilityInject(IConstructTracker.class)
    public static Capability<IConstructTracker> constructTrackerCapability;

    @CapabilityInject(IBodyTracker.class)
    public static Capability<IBodyTracker> bodyTrackerCapability;

    @CapabilityInject(IBeamTracker.class)
    public static Capability<IBeamTracker> beamTrackerCapability;

    @CapabilityInject(IInnerRealmChunkClaim.class)
    public static Capability<IInnerRealmChunkClaim> chunkClaimCapability;

    @CapabilityInject(IInnerRealmTeleporter.class)
    public static Capability<IInnerRealmTeleporter> teleporterCapability;

    public static LazyOptional<IBodyTracker> getBodyTracker(ServerWorld world) {
        return getOverworld(world).getCapability(bodyTrackerCapability);
    }

    public static LazyOptional<IPsychicInventory> getOverworldPsychicInventory(ServerWorld world) {
        return getOverworld(world).getCapability(psychicInventoryCapability);
    }

    public static LazyOptional<ISleepManager> getSleepManager(PlayerEntity playerEntity) {
        return playerEntity.getCapability(sleepManagerCapability);
    }

    public static LazyOptional<IConstructTracker> getConstructTracker(ServerWorld world) {
        return getOverworld(world).getCapability(constructTrackerCapability);
    }

    public static LazyOptional<IInnerRealmChunkClaim> getChunkClaimTracker(ServerWorld world) {
        return getOverworld(world).getCapability(chunkClaimCapability);
    }

    public static LazyOptional<IInnerRealmTeleporter> getInnerRealmTeleporter(ServerWorld world) {
        return getOverworld(world).getCapability(teleporterCapability);
    }

    public static ServerWorld getOverworld(ServerWorld world) {
        return world.getServer().getWorld(World.OVERWORLD);
    }

    @Nullable
    public static IIntentionTrackerBehavior getIntentionTrackerBehavior(Block block) {
        return INTENTION_TRACKER_BEHAVIORS.get(block);
    }

    public static void addIntentionTrackerBehavior(Block block, IIntentionTrackerBehavior intentionTrackerBehavior){
        INTENTION_TRACKER_BEHAVIORS.put(block, intentionTrackerBehavior);
    }

    public static Map<Block, IIntentionTrackerBehavior> getIntentionTrackerBehaviors() {
        return INTENTION_TRACKER_BEHAVIORS;
    }
}
