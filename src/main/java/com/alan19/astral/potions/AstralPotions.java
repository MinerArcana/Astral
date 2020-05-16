package com.alan19.astral.potions;

import com.alan19.astral.configs.AstralConfig;
import com.alan19.astral.effects.AstralEffects;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class AstralPotions {

    @ObjectHolder("astral:astral_travel_potion")
    public static final Potion ASTRAL_TRAVEL_POTION = null;
    @ObjectHolder("astral:long_astral_travel_potion")
    public static final Potion LONG_ASTRAL_TRAVEL_POTION = null;
    @ObjectHolder("astral:strong_astral_travel_potion")
    public static final Potion STRONG_ASTRAL_TRAVEL_POTION = null;
    @ObjectHolder("astral:feverweed_brew")
    public static final Potion FEVERWEED_BREW = null;
    @ObjectHolder("astral:long_feverweed_brew")
    public static final Potion LONG_FEVERWEED_BREW = null;
    @ObjectHolder("astral:strong_feverweed_brew")
    public static final Potion STRONG_FEVERWEED_BREW = null;
    @ObjectHolder("astral:snowberry_brew")
    public static final Potion SNOWBERRY_BREW = null;
    @ObjectHolder("astral:long_snowberry_brew")
    public static final Potion LONG_SNOWBERRY_BREW = null;
    @ObjectHolder("astral:strong_snowberry_brew")
    public static final Potion STRONG_SNOWBERRY_BREW = null;

    @SubscribeEvent
    public static void onPotionRegistry(final RegistryEvent.Register<Potion> event) {
        int baseAstralTravelDuration = AstralConfig.getPotionEffectDurations().getAstralTravelDuration();
        registerPotion(event.getRegistry(), AstralEffects.ASTRAL_TRAVEL, "astral_travel_potion", baseAstralTravelDuration, 0);
        registerPotion(event.getRegistry(), AstralEffects.ASTRAL_TRAVEL, "long_astral_travel_potion", baseAstralTravelDuration * 2, 0);
        registerPotion(event.getRegistry(), AstralEffects.ASTRAL_TRAVEL, "strong_astral_travel_potion", baseAstralTravelDuration / 2, 1);

        //Register more complex potions
        AstralConfig.PotionEffectDurations baseBrewDurations = AstralConfig.getPotionEffectDurations();

        //Feverweed Brew
        int feverweedBrewLuckDuration = baseBrewDurations.getFeverweedBrewLuckDuration();
        int feverweedBrewHungerDuration = baseBrewDurations.getFeverweedBrewHungerDuration();
        event.getRegistry().register(new Potion(new EffectInstance(Effects.LUCK, feverweedBrewLuckDuration, 1), new EffectInstance(Effects.HUNGER, feverweedBrewHungerDuration, 1)).setRegistryName("feverweed_brew"));
        event.getRegistry().register(new Potion(new EffectInstance(Effects.LUCK, feverweedBrewLuckDuration * 2, 0), new EffectInstance(Effects.HUNGER, feverweedBrewHungerDuration * 2, 0)).setRegistryName("long_feverweed_brew"));
        event.getRegistry().register(new Potion(new EffectInstance(Effects.LUCK, feverweedBrewLuckDuration * 2 / 3, 2), new EffectInstance(Effects.HUNGER, feverweedBrewHungerDuration * 2 / 3, 2)).setRegistryName("strong_feverweed_brew"));

        //Snowberry Brew
        int snowberryBrewRegenerationDuration = baseBrewDurations.getSnowberryBrewRegenerationDuration();
        int snowberryBrewNauseaDuration = baseBrewDurations.getSnowberryBrewNauseaDuration();
        event.getRegistry().register(new Potion(new EffectInstance(Effects.REGENERATION, snowberryBrewRegenerationDuration, 1), new EffectInstance(Effects.NAUSEA, snowberryBrewNauseaDuration, 1)).setRegistryName("snowberry_brew"));
        event.getRegistry().register(new Potion(new EffectInstance(Effects.REGENERATION, snowberryBrewRegenerationDuration * 2, 0), new EffectInstance(Effects.NAUSEA, snowberryBrewNauseaDuration * 2, 0)).setRegistryName("long_snowberry_brew"));
        event.getRegistry().register(new Potion(new EffectInstance(Effects.REGENERATION, snowberryBrewRegenerationDuration * 2 / 3, 2), new EffectInstance(Effects.NAUSEA, snowberryBrewNauseaDuration * 2 / 3, 2)).setRegistryName("strong_snowberry_brew"));
    }

    /**
     * Function to create and register a potion with only one effect
     *
     * @param eventRegistry The registry to register the potion to
     * @param effect        What effect should the potion apply
     * @param name          The registry name of the potion
     * @param duration      The duration of the potion, in ticks
     * @param amplifier     The level of the effect
     */
    private static void registerPotion(IForgeRegistry<Potion> eventRegistry, Effect effect, String name, int duration, int amplifier) {
        Potion potion = new Potion(new EffectInstance(effect, duration, amplifier)).setRegistryName(name);
        eventRegistry.register(potion);
    }

}