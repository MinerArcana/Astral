package com.alan199921.astral.capabilities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class ModCapabilities {
    @CapabilityInject(InnerRealmTeleporter.class)
    public static final Capability<InnerRealmTeleporter> INNER_REAML_TELEPORTER = null;
}
