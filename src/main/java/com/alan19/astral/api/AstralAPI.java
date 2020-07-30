package com.alan19.astral.api;

import com.alan19.astral.api.bodylink.IBodyLink;
import com.alan19.astral.api.bodytracker.IBodyTracker;
import com.alan19.astral.api.constructtracker.IConstructTracker;
import com.alan19.astral.api.psychicinventory.IPsychicInventory;
import com.alan19.astral.api.sleepmanager.ISleepManager;
import com.alan19.astral.mentalconstructs.MentalConstructType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;

public class AstralAPI {
    public static final Lazy<IForgeRegistry<MentalConstructType>> MENTAL_CONSTRUCT_TYPES = Lazy.of(() -> RegistryManager.ACTIVE.getRegistry(MentalConstructType.class));

    @CapabilityInject(ISleepManager.class)
    public static Capability<ISleepManager> sleepManagerCapability;

    @CapabilityInject(IPsychicInventory.class)
    public static Capability<IPsychicInventory> psychicInventoryCapability;

    @CapabilityInject(IBodyLink.class)
    public static Capability<IBodyLink> bodyLinkCapability;

    @CapabilityInject(IConstructTracker.class)
    public static Capability<IConstructTracker> constructTrackerCapability;

    @CapabilityInject(IBodyTracker.class)
    public static Capability<IBodyTracker> bodyTrackerCapability;

    public static LazyOptional<IBodyTracker> getBodyTracker(ServerWorld world){
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

    public static ServerWorld getOverworld(ServerWorld world) {
        return world.getServer().getWorld(DimensionType.OVERWORLD);
    }
}
