package com.alan199921.astral.api;

import com.alan199921.astral.api.psychicinventory.IPsychicInventory;
import com.alan199921.astral.api.sleepmanager.ISleepManager;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class AstralAPI {
    @CapabilityInject(ISleepManager.class)
    public static Capability<ISleepManager> sleepManagerCapability;
    public static Capability<IPsychicInventory> psychicInventoryCapability;
}
