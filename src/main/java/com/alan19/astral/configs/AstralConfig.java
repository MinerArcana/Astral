package com.alan19.astral.configs;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;

import java.nio.file.Path;
import java.util.Objects;

//TODO Get rid of builder comments and make fields public
public class AstralConfig {
    private static AstralConfig instance;

    private final WorldgenSettings worldgenSettings;
    private final HerbEffectDurations herbEffectDurations;
    private final PotionEffectDurations potionEffectDurations;
    private final FlightSettings flightSettings;
    private final TravelingSettings travelingSettings;
    private final ForgeConfigSpec spec;

    private AstralConfig(ForgeConfigSpec.Builder builder) {
        this.worldgenSettings = new WorldgenSettings(builder);
        this.herbEffectDurations = new HerbEffectDurations(builder);
        this.potionEffectDurations = new PotionEffectDurations(builder);
        this.flightSettings = new FlightSettings(builder);
        this.travelingSettings = new TravelingSettings(builder);
        this.spec = builder.build();
    }

    /**
     * Initializes AstralConfig
     *
     * @return An instance of the ForgeConfigSpec
     */
    public static ForgeConfigSpec initialize() {
        AstralConfig astralConfig = new AstralConfig(new ForgeConfigSpec.Builder());
        instance = astralConfig;
        return astralConfig.getSpec();
    }

    /**
     * Static getter for an instance of AstralConfig
     *
     * @return An instance of AstralConfig
     */
    public static AstralConfig getInstance() {
        return Objects.requireNonNull(instance, "Called for Config before it's Initialization");
    }

    /**
     * Getter for an instance of HerbEffectDurations
     *
     * @return An instance of HerbEffectDurations
     */
    public static HerbEffectDurations getHerbEffectDurations() {
        return instance.herbEffectDurations;
    }

    /**
     * Getter for PotionEffectDurations
     *
     * @return An instance of PotionEffectDurations
     */
    public static PotionEffectDurations getPotionEffectDurations() {
        return instance.potionEffectDurations;
    }

    public static FlightSettings getFlightSettings() {
        return instance.flightSettings;
    }

    public static WorldgenSettings getWorldgenSettings() {
        return instance.worldgenSettings;
    }

    public static TravelingSettings getTravelingSettings() {
        return instance.travelingSettings;
    }

    /**
     * Function to load the config information from disk
     *
     * @param spec The spec that the disk config is being loaded into
     * @param path The path of the config file
     */
    public static void loadConfig(ForgeConfigSpec spec, Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path)
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();

        configData.load();
        spec.setConfig(configData);
    }


    /**
     * Getter for the ForceConfigSpec. Needs to be accessed through getInstance() since function is not static.
     *
     * @return The ForceConfigSpec object for the mod
     */
    public ForgeConfigSpec getSpec() {
        return spec;
    }

    /**
     * Inner class that holds configs for worldgen herb potion effect durations
     * Contains getters for each of the config values
     */
    public static class HerbEffectDurations {
        private final ForgeConfigSpec.ConfigValue<Integer> feverweedLuckDuration;
        private final ForgeConfigSpec.ConfigValue<Integer> feverweedHungerDuration;
        private final ForgeConfigSpec.ConfigValue<Integer> snowberryRegenerationDuration;
        private final ForgeConfigSpec.ConfigValue<Integer> snowberryNauseaDuration;
        private final ForgeConfigSpec.ConfigValue<Integer> travelingMedicineDuration;

        HerbEffectDurations(ForgeConfigSpec.Builder builder) {
            builder.comment("Astral herb potion effect settings").push("herbSettings");

            feverweedLuckDuration = builder.comment("Controls the duration of luck from Feverweed, in ticks.")
                    .translation("astral.config.common.feverweedLuckDuration")
                    .define("feverweedLuckDuration", 300);

            feverweedHungerDuration = builder.comment("Controls the duration of hunger from Feverweed, in ticks.")
                    .translation("astral.config.common.feverweedHungerDuration")
                    .define("feverweedHungerDuration", 300);

            snowberryRegenerationDuration = builder.comment("Controls the duration of regeneration from Snowberries, in ticks.")
                    .translation("astral.config.common.snowberriesRegenerationDuration")
                    .define("snowberriesRegenerationDuration", 300);

            snowberryNauseaDuration = builder.comment("Controls the duration of nausea from Snowberries, in ticks.")
                    .translation("astral.config.common.snowberriesNauseaDuration")
                    .define("snowberriesNauseaDuration", 300);

            travelingMedicineDuration = builder.comment("Controls the duration of Astral Travel from Traveling Medicine, in ticks.")
                    .translation("astral.config.common.travelingMedicineAstralTravelDuration")
                    .define("travelingMedicineAstralTravelDuration", 1200);

            builder.pop();
        }

        public int getFeverweedLuckDuration() {
            return feverweedLuckDuration.get();
        }

        public int getFeverweedHungerDuration() {
            return feverweedHungerDuration.get();
        }

        public int getSnowberryRegenerationDuration() {
            return snowberryRegenerationDuration.get();
        }

        public int getSnowberryNauseaDuration() {
            return snowberryNauseaDuration.get();
        }

        public int gettravelingMedicineDuration() {
            return travelingMedicineDuration.get();
        }
    }


    public static class PotionEffectDurations {
        private final ForgeConfigSpec.ConfigValue<Integer> astralTravelDuration;
        private final ForgeConfigSpec.ConfigValue<Integer> feverweedBrewLuckDuration;
        private final ForgeConfigSpec.ConfigValue<Integer> feverweedBrewHungerDuration;
        private final ForgeConfigSpec.ConfigValue<Integer> snowberryBrewRegenerationDuration;
        private final ForgeConfigSpec.ConfigValue<Integer> snowberryBrewNauseaDuration;

        PotionEffectDurations(ForgeConfigSpec.Builder builder) {
            builder.comment("Astral potion settings").push("potionSettings");

            astralTravelDuration = builder.comment("Controls the duration of the Astral Travel potion duration, in ticks")
                    .translation("astral.config.common.astralTravelPotionDuration")
                    .define("astralTravelPotionDuration", 6000);
            feverweedBrewLuckDuration = builder.comment("Controls the duration of luck from Feverweed Brew, in ticks.")
                    .translation("astral.config.common.feverweedBrewLuckDuration")
                    .define("feverweedBrewLuckDuration", 600);

            feverweedBrewHungerDuration = builder.comment("Controls the duration of hunger from Feverweed Brew, in ticks.")
                    .translation("astral.config.common.feverweedBrewHungerDuration")
                    .define("feverweedBrewHungerDuration", 600);

            snowberryBrewRegenerationDuration = builder.comment("Controls the duration of regeneration from Snowberry Brew, in ticks.")
                    .translation("astral.config.common.snowberryBrewRegenerationDuration")
                    .define("snowberryBrewRegenerationDuration", 600);

            snowberryBrewNauseaDuration = builder.comment("Controls the duration of nausea from Snowberry Brew, in ticks.")
                    .translation("astral.config.common.snowberryBrewNauseaDuration")
                    .define("snowberryBrewNauseaDuration", 600);

            builder.pop();
        }

        public int getFeverweedBrewLuckDuration() {
            return feverweedBrewLuckDuration.get();
        }

        public int getFeverweedBrewHungerDuration() {
            return feverweedBrewHungerDuration.get();
        }

        public int getSnowberryBrewRegenerationDuration() {
            return snowberryBrewRegenerationDuration.get();
        }

        public int getSnowberryBrewNauseaDuration() {
            return snowberryBrewNauseaDuration.get();
        }

        public int getAstralTravelDuration() {
            return astralTravelDuration.get();
        }
    }

    public static class FlightSettings {
        private final ForgeConfigSpec.ConfigValue<Double> baseSpeed;
        private final ForgeConfigSpec.ConfigValue<Double> maxMultiplier;
        private final ForgeConfigSpec.ConfigValue<Double> maxPenalty;
        private final ForgeConfigSpec.ConfigValue<Integer> heightPenaltyLimit;
        private final ForgeConfigSpec.ConfigValue<Integer> decelerationDistance;

        FlightSettings(ForgeConfigSpec.Builder builder) {
            builder.comment("Astral Travel flight settings").comment("The speed of flight using Astral Travel decays with every block the player is above the closest block below them.").push("flightSettings");

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

            builder.pop();
        }

        public double getBaseSpeed() {
            return baseSpeed.get();
        }

        public double getMaxMultiplier() {
            return maxMultiplier.get();
        }

        public double getMaxPenalty() {
            return maxPenalty.get();
        }

        public int getHeightPenaltyLimit() {
            return heightPenaltyLimit.get();
        }

        public int getDecelerationDistance() {
            return decelerationDistance.get();
        }
    }

    public static class TravelingSettings {
        private final ForgeConfigSpec.ConfigValue<Integer> startupTime;
        private final ForgeConfigSpec.ConfigValue<Integer> syncInterval;

        TravelingSettings(ForgeConfigSpec.Builder builder) {
            startupTime = builder.comment("Controls how long it takes for the benefits of Astral travel to kick in, in ticks")
                    .translation("astral.config.common.startupTime")
                    .define("startupTime", 50);
            syncInterval = builder.comment("Controls how long it takes for the Physical Body to update its stats to the player, in ticks")
                    .translation("astral.config.common.syncInterval")
                    .define("syncInterval", 5);
        }

        public int getStartupTime() {
            return startupTime.get();
        }

        public int getSyncInterval() {
            return syncInterval.get();
        }
    }

    public static class WorldgenSettings {
        private final ForgeConfigSpec.ConfigValue<Integer> feverweedMinPatchSize;
        private final ForgeConfigSpec.ConfigValue<Integer> feverweedMaxPatchSize;
        private final ForgeConfigSpec.ConfigValue<Integer> feverweedPatchSpawnRate;
        private final ForgeConfigSpec.ConfigValue<Integer> feverweedPatchDistribution;
        private final ForgeConfigSpec.ConfigValue<Integer> feverweedMaxTries;
        private final ForgeConfigSpec.ConfigValue<Integer> snowberryMinPatchSize;
        private final ForgeConfigSpec.ConfigValue<Integer> snowberryMaxPatchSize;
        private final ForgeConfigSpec.ConfigValue<Integer> snowberryPatchSpawnRate;
        private final ForgeConfigSpec.ConfigValue<Integer> snowberrySpreadSnowChance;

        public WorldgenSettings(ForgeConfigSpec.Builder builder) {
            builder.comment("Astral worldgen settings").push("worldgenSettings");

            feverweedMinPatchSize = builder.comment("Controls the minimum size of Feverweed patches")
                    .translation("astral.config.common.feverweedMinPatchSize")
                    .define("feverweedMinPatchSize", 4);

            feverweedMaxPatchSize = builder.comment("Controls the maximum size of Feverweed patches")
                    .translation("astral.config.common.feverweedMaxPatchSize")
                    .define("feverweedMaxPatchSize", 10);

            feverweedPatchSpawnRate = builder.comment("Controls how often Feverweed spawns, in terms of 1 in x chunks")
                    .translation("astral.config.common.feverweedPatchSpawnRate")
                    .define("feverweedPatchSpawnRate", 25);

            feverweedPatchDistribution = builder.comment("Controls how distributed Feverweed blocks will be in a Feverweed patch")
                    .translation("astral.config.common.feverweedPatchDistribution")
                    .define("feverweedPatchDistribution", 5);

            feverweedMaxTries = builder.comment("Controls how many tries the world generator will attempt to spawn a block of Feverweed")
                    .translation("astral.config.common.feverweedMaxTries")
                    .define("feverweedMaxTries", 40);

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
        }

        public int getFeverweedPatchSpawnRate() {
            return feverweedPatchSpawnRate.get();
        }

        public int getFeverweedPatchDistribution() {
            return feverweedPatchDistribution.get();
        }

        public int getFeverweedMaxTries() {
            return feverweedMaxTries.get();
        }

        public int getSnowberryMinPatchSize() {
            return snowberryMinPatchSize.get();
        }

        public int getSnowberryMaxPatchSize() {
            return snowberryMaxPatchSize.get();
        }

        public int getSnowberryPatchSpawnRate() {
            return snowberryPatchSpawnRate.get();
        }

        public int getFeverweedMaxPatchSize() {
            return feverweedMaxPatchSize.get();
        }

        public int getFeverweedMinPatchSize() {
            return feverweedMinPatchSize.get();
        }

        public int getSnowberrySpreadSnowChance() {
            return snowberrySpreadSnowChance.get();
        }
    }
}
