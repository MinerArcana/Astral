package com.alan199921.astral.configs;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;

import java.nio.file.Path;
import java.util.Objects;

public class AstralConfig {
    private static AstralConfig instance;

    private final HerbEffectDurations herbEffectDurations;
    private final PotionEffectDurations potionEffectDurations;
    private final ForgeConfigSpec spec;

    private AstralConfig(ForgeConfigSpec.Builder builder) {
        this.herbEffectDurations = new HerbEffectDurations(builder);
        this.potionEffectDurations = new PotionEffectDurations(builder);
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
        private final ForgeConfigSpec.ConfigValue<Integer> travellingMedicineDuration;

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

            travellingMedicineDuration = builder.comment("Controls the duration of Astral Travel from Travelling Medicine, in ticks.")
                    .translation("astral.config.common.travellingMedicineAstralTravelDuration")
                    .define("travellingMedicineAstralTravelDuration", 1200);

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

        public int getTravellingMedicineDuration() {
            return travellingMedicineDuration.get();
        }
    }

    public static class PotionEffectDurations {
        private final ForgeConfigSpec.ConfigValue<Integer> astralTravelDuration;

        PotionEffectDurations(ForgeConfigSpec.Builder builder) {
            builder.comment("Astral potion settings").push("potionSettings");

            astralTravelDuration = builder.comment("Controls the duration of the Astral Travel potion duration, in ticks")
                    .translation("astral.config.common.astralTravelPotionDuration")
                    .define("astralTravelPotionDuration", 6000);

            builder.pop();
        }

        public int getAstralTravelDuration() {
            return astralTravelDuration.get();
        }
    }
}
