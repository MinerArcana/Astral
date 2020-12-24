package com.alan19.astral.configs;

import net.minecraftforge.common.ForgeConfigSpec;

public class TravelingSettings {
    public final ForgeConfigSpec.ConfigValue<Double> baseSpeed;
    public final ForgeConfigSpec.ConfigValue<Double> maxMultiplier;
    public final ForgeConfigSpec.ConfigValue<Double> maxPenalty;
    public final ForgeConfigSpec.ConfigValue<Integer> heightPenaltyLimit;
    public final ForgeConfigSpec.ConfigValue<Integer> decelerationDistance;
    public final ForgeConfigSpec.ConfigValue<Integer> startupTime;
    public final ForgeConfigSpec.ConfigValue<Integer> syncInterval;


    TravelingSettings(ForgeConfigSpec.Builder builder) {
        builder.comment("Settings for Astral flight").comment("The speed of flight using Astral Travel decays with every block the player is above the closest block below them.").push("flightSettings");

        baseSpeed = builder.comment("Controls the base speed of the player, in blocks per second.")
                .translation("astral.config.common.baseSpeed")
                .define("baseSpeed", 4.317);
        maxMultiplier = builder.comment("Controls the maximum multiplier of the base speed.")
                .translation("astral.config.common.maxMultiplier")
                .define("maxMultiplier", 1.5);
        maxPenalty = builder.comment("Controls the maximum penalty that could be applied to the maximum multiplier.")
                .translation("astral.config.common.maxPenalty")
                .define("maxPenalty", .5);
        heightPenaltyLimit = builder.comment("Controls the maximum height above the ground the penalty maxes out at.")
                .translation("astral.config.common.heightPenaltyLimit")
                .define("heightPenaltyLimit", 64);
        decelerationDistance = builder.comment("Controls how many blocks before the desired height should you decelerate.")
                .translation("astral.config.common.decelerationDistance")
                .define("decelerationDistance", 5);
        startupTime = builder.comment("Controls how long it takes for the benefits of Astral travel to kick in, in ticks")
                .translation("astral.config.common.startupTime")
                .define("startupTime", 50);
        syncInterval = builder.comment("Controls how long it takes for the Physical Body to update its stats to the player, in ticks")
                .translation("astral.config.common.syncInterval")
                .define("syncInterval", 5);

        builder.pop();
    }
}
