package com.alan19.astral.configs;

import net.minecraftforge.common.ForgeConfigSpec;

public class EffectDurations {
    public final ForgeConfigSpec.ConfigValue<Integer> feverweedLuckDuration;
    public final ForgeConfigSpec.ConfigValue<Integer> feverweedHungerDuration;
    public final ForgeConfigSpec.ConfigValue<Integer> snowberryRegenerationDuration;
    public final ForgeConfigSpec.ConfigValue<Integer> snowberryNauseaDuration;
    public final ForgeConfigSpec.ConfigValue<Integer> travelingMedicineDuration;
    public final ForgeConfigSpec.ConfigValue<Integer> astralTravelDuration;
    public final ForgeConfigSpec.ConfigValue<Integer> feverweedBrewLuckDuration;
    public final ForgeConfigSpec.ConfigValue<Integer> feverweedBrewHungerDuration;
    public final ForgeConfigSpec.ConfigValue<Integer> snowberryBrewRegenerationDuration;
    public final ForgeConfigSpec.ConfigValue<Integer> snowberryBrewNauseaDuration;

    public EffectDurations(ForgeConfigSpec.Builder builder) {
        builder.comment("Settings for potion and food effects in the mod").push("effectDurations");

        feverweedLuckDuration = builder.comment("Controls the duration of luck from Feverweed, in ticks.")
                .define("feverweedLuckDuration", 300);

        feverweedHungerDuration = builder.comment("Controls the duration of hunger from Feverweed, in ticks.")
                .define("feverweedHungerDuration", 300);

        snowberryRegenerationDuration = builder.comment("Controls the duration of regeneration from Snowberries, in ticks.")
                .define("snowberriesRegenerationDuration", 300);

        snowberryNauseaDuration = builder.comment("Controls the duration of nausea from Snowberries, in ticks.")
                .define("snowberriesNauseaDuration", 300);

        travelingMedicineDuration = builder.comment("Controls the duration of Astral Travel from Traveling Medicine, in ticks.")
                .define("travelingMedicineAstralTravelDuration", 1200);

        astralTravelDuration = builder.comment("Controls the duration of the Astral Travel potion duration, in ticks")
                .define("astralTravelPotionDuration", 6000);

        feverweedBrewLuckDuration = builder.comment("Controls the duration of luck from Feverweed Brew, in ticks.")
                .define("feverweedBrewLuckDuration", 600);

        feverweedBrewHungerDuration = builder.comment("Controls the duration of hunger from Feverweed Brew, in ticks.")
                .define("feverweedBrewHungerDuration", 600);

        snowberryBrewRegenerationDuration = builder.comment("Controls the duration of regeneration from Snowberry Brew, in ticks.")
                .define("snowberryBrewRegenerationDuration", 600);

        snowberryBrewNauseaDuration = builder.comment("Controls the duration of nausea from Snowberry Brew, in ticks.")
                .define("snowberryBrewNauseaDuration", 600);

        builder.pop();
    }
}
