package com.alan19.astral.potions;

import com.alan19.astral.configs.AstralConfig;
import com.alan19.astral.effects.AstralEffects;
import com.google.common.collect.ImmutableList;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

import java.util.List;

public class PotionEffectInstances {
    public static final int SNOWBERRY_BREW_NAUSEA_DURATION = AstralConfig.getPotionEffectDurations().getSnowberryBrewNauseaDuration();
    public static final int SNOWBERRY_BREW_REGENERATION_DURATION = AstralConfig.getPotionEffectDurations().getSnowberryBrewRegenerationDuration();
    public static final int FEVERWEED_BREW_LUCK_DURATION = AstralConfig.getPotionEffectDurations().getFeverweedBrewLuckDuration();
    public static final int FEVERWEED_BREW_HUNGER_DURATION = AstralConfig.getPotionEffectDurations().getFeverweedBrewHungerDuration();
    public static final int ASTRAL_TRAVEL_DURATION = AstralConfig.getPotionEffectDurations().getAstralTravelDuration();

    public static final List<EffectInstance> STRONG_SNOWBERRY_INSTANCE = ImmutableList.of(new EffectInstance(Effects.REGENERATION, SNOWBERRY_BREW_REGENERATION_DURATION * 2 / 3, 2), new EffectInstance(Effects.NAUSEA, SNOWBERRY_BREW_NAUSEA_DURATION * 2 / 3, 2));
    public static final List<EffectInstance> LONG_SNOWBERRY_INSTANCE = ImmutableList.of(new EffectInstance(Effects.REGENERATION, SNOWBERRY_BREW_REGENERATION_DURATION * 2, 1), new EffectInstance(Effects.NAUSEA, SNOWBERRY_BREW_NAUSEA_DURATION * 2, 1));
    public static final List<EffectInstance> SNOWBERRY_BASE_INSTANCE = ImmutableList.of(new EffectInstance(Effects.REGENERATION, SNOWBERRY_BREW_REGENERATION_DURATION, 1), new EffectInstance(Effects.NAUSEA, SNOWBERRY_BREW_NAUSEA_DURATION, 1));
    public static final List<EffectInstance> STRONG_FEVERWEED_INSTANCE = ImmutableList.of(new EffectInstance(Effects.LUCK, FEVERWEED_BREW_LUCK_DURATION * 2 / 3, 2), new EffectInstance(Effects.HUNGER, FEVERWEED_BREW_HUNGER_DURATION * 2 / 2, 2));
    public static final List<EffectInstance> LONG_ASTRAL_TRAVEL_INSTANCE = ImmutableList.of(new EffectInstance(AstralEffects.ASTRAL_TRAVEL.get(), ASTRAL_TRAVEL_DURATION * 2));
    public static final List<EffectInstance> STRONG_ASTRAL_TRAVEL_INSTANCE = ImmutableList.of(new EffectInstance(AstralEffects.ASTRAL_TRAVEL.get(), ASTRAL_TRAVEL_DURATION / 2, 1));
    public static final List<EffectInstance> BASE_ASTRAL_TRAVEL_INSTANCE = ImmutableList.of(new EffectInstance(AstralEffects.ASTRAL_TRAVEL.get(), ASTRAL_TRAVEL_DURATION));
    public static final List<EffectInstance> BASE_FEVERWEED_INSTANCE = ImmutableList.of(new EffectInstance(Effects.LUCK, FEVERWEED_BREW_LUCK_DURATION, 1), new EffectInstance(Effects.HUNGER, FEVERWEED_BREW_HUNGER_DURATION, 1));
    public static final List<EffectInstance> LONG_FEVERWEED_INSTANCE = ImmutableList.of(new EffectInstance(Effects.LUCK, FEVERWEED_BREW_LUCK_DURATION * 2, 1), new EffectInstance(Effects.HUNGER, FEVERWEED_BREW_HUNGER_DURATION * 2, 1));
}
