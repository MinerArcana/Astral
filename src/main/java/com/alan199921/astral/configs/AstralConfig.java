package com.alan199921.astral.configs;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;

import java.nio.file.Path;
import java.util.Objects;

public class AstralConfig {
    private static AstralConfig instance;

    private final HerbEffectDurations herbEffectDurations;
    private final ForgeConfigSpec spec;

    private AstralConfig(ForgeConfigSpec.Builder builder) {
        this.herbEffectDurations = new HerbEffectDurations(builder);
        this.spec = builder.build();
    }

    public static ForgeConfigSpec initialize() {
        AstralConfig astralConfig = new AstralConfig(new ForgeConfigSpec.Builder());
        instance = astralConfig;
        return astralConfig.getSpec();
    }

    public static AstralConfig getInstance() {
        return Objects.requireNonNull(instance, "Called for Config before it's Initialization");
    }

    public static HerbEffectDurations getHerbEffectDurations() {
        return instance.herbEffectDurations;
    }

    public ForgeConfigSpec getSpec() {
        return spec;
    }

    public static void loadConfig(ForgeConfigSpec spec, Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path)
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();

        configData.load();
        spec.setConfig(configData);
    }

    public static class HerbEffectDurations {
        private final ForgeConfigSpec.ConfigValue<Integer> feverweedLuckDuration;
        private final ForgeConfigSpec.ConfigValue<Integer> feverweedHungerDuration;
        private final ForgeConfigSpec.ConfigValue<Integer> snowberryRegenerationDuration;
        private final ForgeConfigSpec.ConfigValue<Integer> snowberryNauseaDuration;

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

        HerbEffectDurations(ForgeConfigSpec.Builder builder) {
            builder.comment("Astral herb potion effect settings").push("common");

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

            builder.pop();
        }
    }
}
