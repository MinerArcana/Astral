package com.alan199921.astral;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber
public class Config {

    private static final ForgeConfigSpec commonSpec;
    private static final Common COMMON;

    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        commonSpec = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    public static void register(final ModLoadingContext context) {
        context.registerConfig(ModConfig.Type.COMMON, commonSpec);
    }

    public static class Common {

        Common(final ForgeConfigSpec.Builder builder) {
            builder.comment("Astral herb settings").push("common");

            builder.comment("Controls the duration of luck from Feverweed, in ticks.")
                    .translation("astral.config.common.feverweedLuckDuration")
                    .define("feverweedLuckDuration", 300);

            builder.comment("Controls the duration of hunger from Feverweed, in ticks.")
                    .translation("astral.config.common.feverweedHungerDuration")
                    .define("feverweedHungerDuration", 300);

            builder.comment("Controls the duration of regeneration from Snowberries, in ticks.")
                    .translation("astral.config.common.snowberriesRegenerationDuration")
                    .define("snowberriesRegenerationDuration", 300);

            builder.comment("Controls the duration of nausea from Snowberries, in ticks.")
                    .translation("astral.config.common.snowberriesNauseaDuration")
                    .define("snowberriesNauseaDuration", 300);

            builder.pop();
        }
    }
}
