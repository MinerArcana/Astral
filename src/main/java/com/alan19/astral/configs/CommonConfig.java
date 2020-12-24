package com.alan19.astral.configs;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfig {
    public final EffectDurations effectDurations;
    public final TravelingSettings travelingSettings;
    public final WorldgenSettings worldgenSettings;

    public CommonConfig(ForgeConfigSpec.Builder builder) {
        effectDurations = new EffectDurations(builder);
        travelingSettings = new TravelingSettings(builder);
        worldgenSettings = new WorldgenSettings(builder);
    }
}
