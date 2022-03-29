package com.alan19.astral.potions;

import com.alan19.astral.configs.AstralConfig;
import com.alan19.astral.configs.EffectDurations;
import com.alan19.astral.effects.AstralEffects;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.List;

import static com.alan19.astral.effects.AstralEffects.MIND_VENOM;
import static net.minecraft.world.effect.MobEffects.*;

public class PotionEffectInstances {
    private static final EffectDurations effectDuration = AstralConfig.getEffectDuration();

    public static final int SNOWBERRY_BREW_NAUSEA_DURATION = effectDuration.snowberryBrewNauseaDuration.get();
    public static final int SNOWBERRY_BREW_REGENERATION_DURATION = effectDuration.snowberryBrewRegenerationDuration.get();
    public static final int FEVERWEED_BREW_LUCK_DURATION = effectDuration.feverweedBrewLuckDuration.get();
    public static final int FEVERWEED_BREW_HUNGER_DURATION = effectDuration.feverweedBrewHungerDuration.get();
    public static final int ASTRAL_TRAVEL_DURATION = effectDuration.astralTravelDuration.get();

    //Potions
    public static final List<MobEffectInstance> STRONG_SNOWBERRY_INSTANCE = ImmutableList.of(new MobEffectInstance(REGENERATION, SNOWBERRY_BREW_REGENERATION_DURATION * 2 / 3, 2), new MobEffectInstance(CONFUSION, SNOWBERRY_BREW_NAUSEA_DURATION * 2 / 3, 2));
    public static final List<MobEffectInstance> LONG_SNOWBERRY_INSTANCE = ImmutableList.of(new MobEffectInstance(REGENERATION, SNOWBERRY_BREW_REGENERATION_DURATION * 2, 1), new MobEffectInstance(CONFUSION, SNOWBERRY_BREW_NAUSEA_DURATION * 2, 1));
    public static final List<MobEffectInstance> SNOWBERRY_BASE_INSTANCE = ImmutableList.of(new MobEffectInstance(REGENERATION, SNOWBERRY_BREW_REGENERATION_DURATION, 1), new MobEffectInstance(CONFUSION, SNOWBERRY_BREW_NAUSEA_DURATION, 1));
    public static final List<MobEffectInstance> STRONG_FEVERWEED_INSTANCE = ImmutableList.of(new MobEffectInstance(LUCK, FEVERWEED_BREW_LUCK_DURATION * 2 / 3, 2), new MobEffectInstance(HUNGER, FEVERWEED_BREW_HUNGER_DURATION * 2 / 3, 2));
    public static final List<MobEffectInstance> LONG_ASTRAL_TRAVEL_INSTANCE = ImmutableList.of(new MobEffectInstance(AstralEffects.ASTRAL_TRAVEL.get(), ASTRAL_TRAVEL_DURATION * 2));
    public static final List<MobEffectInstance> STRONG_ASTRAL_TRAVEL_INSTANCE = ImmutableList.of(new MobEffectInstance(AstralEffects.ASTRAL_TRAVEL.get(), ASTRAL_TRAVEL_DURATION / 2, 1));
    public static final List<MobEffectInstance> BASE_ASTRAL_TRAVEL_INSTANCE = ImmutableList.of(new MobEffectInstance(AstralEffects.ASTRAL_TRAVEL.get(), ASTRAL_TRAVEL_DURATION));
    public static final List<MobEffectInstance> BASE_FEVERWEED_INSTANCE = ImmutableList.of(new MobEffectInstance(LUCK, FEVERWEED_BREW_LUCK_DURATION, 1), new MobEffectInstance(HUNGER, FEVERWEED_BREW_HUNGER_DURATION, 1));
    public static final List<MobEffectInstance> LONG_FEVERWEED_INSTANCE = ImmutableList.of(new MobEffectInstance(LUCK, FEVERWEED_BREW_LUCK_DURATION * 2, 1), new MobEffectInstance(HUNGER, FEVERWEED_BREW_HUNGER_DURATION * 2, 1));
    public static final List<MobEffectInstance> BASE_MIND_VENOM_INSTANCE = ImmutableList.of((new MobEffectInstance(MIND_VENOM.get(), 900)));
    public static final List<MobEffectInstance> LONG_MIND_VENOM_INSTANCE = ImmutableList.of(new MobEffectInstance(MIND_VENOM.get(), 1800));
    public static final List<MobEffectInstance> STRONG_MIND_VENOM_INSTANCE = ImmutableList.of(new MobEffectInstance(MIND_VENOM.get(), 450, 1));

    //Botanical Brewery
    public static final List<MobEffectInstance> SNOWBERRY_BOTANICAL_BREW = ImmutableList.of(new MobEffectInstance(REGENERATION, SNOWBERRY_BREW_REGENERATION_DURATION * 2, 2), new MobEffectInstance(CONFUSION, SNOWBERRY_BREW_NAUSEA_DURATION * 2, 2));
    public static final List<MobEffectInstance> FEVERWEED_BOTANICAL_BREW = ImmutableList.of(new MobEffectInstance(LUCK, FEVERWEED_BREW_LUCK_DURATION * 2, 2), new MobEffectInstance(HUNGER, FEVERWEED_BREW_HUNGER_DURATION * 2, 2));
}
