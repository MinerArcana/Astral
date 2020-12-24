package com.alan19.astral.configs;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;

import java.nio.file.Path;
import java.util.Objects;

public class AstralConfig {
    private static AstralConfig instance;
    private final CommonConfig commonConfig;
    private final ForgeConfigSpec spec;

    private AstralConfig(ForgeConfigSpec.Builder builder) {
        commonConfig = new CommonConfig(builder);
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

    public static EffectDurations getEffectDuration() {
        return instance.commonConfig.effectDurations;
    }

    public static TravelingSettings getTravelingSettings() {
        return instance.commonConfig.travelingSettings;
    }

    public static WorldgenSettings getWorldgenSettings() {
        return instance.commonConfig.worldgenSettings;
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

    public CommonConfig getCommonConfig() {
        return instance.commonConfig;
    }

    /**
     * Getter for the ForceConfigSpec. Needs to be accessed through getInstance() since function is not static.
     *
     * @return The ForceConfigSpec object for the mod
     */
    public ForgeConfigSpec getSpec() {
        return spec;
    }

}
