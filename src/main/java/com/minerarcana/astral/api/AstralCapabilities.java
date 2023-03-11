package com.minerarcana.astral.api;

import com.minerarcana.astral.api.innerrealmcubegen.InnerRealmCubeGen;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class AstralCapabilities {
    public static final Capability<InnerRealmCubeGen> CUBE_GEN_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });
}
