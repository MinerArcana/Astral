package com.alan199921.astral;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

import java.nio.file.Path;

@Mod.EventBusSubscriber
public class Config {

    public static final ForgeConfigSpec commonSpec;
    public static final Common COMMON;

    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        commonSpec = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    public static void register(final ModLoadingContext context) {
        context.registerConfig(ModConfig.Type.COMMON, commonSpec);
    }

    public static class Common {
        public final ForgeConfigSpec.ConfigValue<Integer> feverweedLuckDuration;
        public final ForgeConfigSpec.ConfigValue<Integer> feverweedHungerDuration;
        public final ForgeConfigSpec.ConfigValue<Integer> snowberryRegenerationDuration;
        public final ForgeConfigSpec.ConfigValue<Integer> snowberryNauseaDuration;

        Common(final ForgeConfigSpec.Builder builder) {
            builder.comment("Astral herb settings").push("common");

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
    public static void loadConfig(ForgeConfigSpec spec, Path path) {

        final CommentedFileConfig configData = CommentedFileConfig.builder(path)
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();

        configData.load();
        spec.setConfig(configData);
    }
}
