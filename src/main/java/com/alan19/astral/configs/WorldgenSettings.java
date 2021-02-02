package com.alan19.astral.configs;

import net.minecraftforge.common.ForgeConfigSpec;

public class WorldgenSettings {
    public final ForgeConfigSpec.ConfigValue<Integer> feverweedPatchSpawnRate;
    public final ForgeConfigSpec.ConfigValue<Integer> feverweedPatchDistribution;
    public final ForgeConfigSpec.ConfigValue<Integer> feverweedMaxTries;
    public final ForgeConfigSpec.ConfigValue<Integer> snowberryMinPatchSize;
    public final ForgeConfigSpec.ConfigValue<Integer> snowberryMaxPatchSize;
    public final ForgeConfigSpec.ConfigValue<Integer> snowberryPatchSpawnRate;
    public final ForgeConfigSpec.ConfigValue<Integer> snowberrySpreadSnowChance;
    public final ForgeConfigSpec.ConfigValue<Integer> ethericIslesSpacing;
    public final ForgeConfigSpec.ConfigValue<Integer> ethericIslesSeparation;
    public final ForgeConfigSpec.ConfigValue<Integer> crystalWebSpreadChance;

    public WorldgenSettings(ForgeConfigSpec.Builder builder) {
        builder.comment("Settings for controlling Astral worldgen such as Feverweed, Snowberry, and Etheric Isles").push("worldgenSettings");

        feverweedPatchSpawnRate = builder.comment("Controls how often Feverweed spawns, in terms of 1 in x chunks")
                .translation("astral.config.common.feverweedPatchSpawnRate")
                .define("feverweedPatchSpawnRate", 25);

        feverweedPatchDistribution = builder.comment("Controls the radius of the feverweed patch in the X and Z directions (y is always 3 blocks)")
                .translation("astral.config.common.feverweedPatchDistribution")
                .define("feverweedPatchDistribution", 5);

        feverweedMaxTries = builder.comment("Controls how many tries the world generator will attempt to spawn a block of Feverweed")
                .translation("astral.config.common.feverweedMaxTries")
                .define("feverweedMaxTries", 64);

        snowberryMinPatchSize = builder.comment("Controls the minimum size of Snowberry patches")
                .translation("astral.config.common.snowberryMinPatchSize")
                .define("snowberryMinPatchSize", 2);

        snowberryMaxPatchSize = builder.comment("Controls the maximum size of Snowberry patches")
                .translation("astral.config.common.snowberryMaxPatchSize")
                .define("snowberryMaxPatchSize", 5);

        snowberryPatchSpawnRate = builder.comment("Controls how often Snowberry spawns, in terms of 1 in x chunks")
                .translation("astral.config.common.snowberryPatchSpawnRate")
                .define("snowberryPatchSpawnRate", 25);

        snowberrySpreadSnowChance = builder.comment("Controls how often Snowberries add snow nearby, in terms of 1 in x times it grows")
                .translation("astral.config.common.snowberrySpreadSnowChance")
                .define("snowberrySpreadSnowChance", 5);

        ethericIslesSpacing = builder.comment("The average number of chunks between two Etheric Isles")
                .translation("astral.config.common.ethericIslesSpacing")
                .define("ethericIslesSpacing", 15);

        ethericIslesSeparation = builder.comment("The minimum number of chunks between two Etheric Isles. Must be less than spacing.")
                .translation("astral.config.common.ethericIslesSeparation")
                .define("ethericIslesSeparation", 1);

        crystalWebSpreadChance = builder.comment("The 1 in x chance of a crystal web spreading when it's tick function is called.")
                .translation("astral.config.common.crystalWebSpreadChance")
                .define("crystalWebSpreadChance", 30);

        builder.pop();
    }
}
