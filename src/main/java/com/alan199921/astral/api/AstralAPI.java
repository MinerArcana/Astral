package com.alan199921.astral.api;

import com.alan199921.astral.api.psychicinventory.IPsychicInventory;
import com.alan199921.astral.api.psychicinventory.PsychicInventory;
import com.alan199921.astral.api.sleepmanager.ISleepManager;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class AstralAPI {
    @CapabilityInject(ISleepManager.class)
    public static Capability<ISleepManager> sleepManagerCapability;

    @CapabilityInject(IPsychicInventory.class)
    public static Capability<IPsychicInventory> psychicInventoryCapability;

    public static IPsychicInventory getOverworldPsychicInventory(ServerWorld world) {
        return world.getServer().getWorld(DimensionType.OVERWORLD).getCapability(psychicInventoryCapability).orElse(new PsychicInventory());
    }
}
