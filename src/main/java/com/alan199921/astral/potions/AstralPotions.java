package com.alan199921.astral.potions;

import com.alan199921.astral.configs.AstralConfig;
import com.alan199921.astral.effects.AstralEffects;
import com.alan199921.astral.items.AstralItems;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.potion.*;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class AstralPotions {

    @ObjectHolder("astral:astral_travel_potion")
    public static final Potion astralTravelPotion = null;

    @ObjectHolder("astral:long_astral_travel_potion")
    public static final Potion longAstralTravelPotion = null;

    @ObjectHolder("astral:strong_astral_travel_potion")
    public static final Potion strongAstralTravelPotion = null;

    @ObjectHolder("astral:feverweed_brew")
    public static final Potion feverweedBrew = null;

    @ObjectHolder("astral:long_feverweed_brew")
    public static final Potion longFeverweedBrew = null;

    @ObjectHolder("astral:strong_feverweed_brew")
    public static final Potion strongFeverweedBrew = null;

    @ObjectHolder("astral:snowberry_brew")
    public static final Potion snowberryBrew = null;

    @ObjectHolder("astral:long_snowberry_brew")
    public static final Potion longSnowberryBrew = null;

    @ObjectHolder("astral:strong_snowberry_brew")
    public static final Potion strongSnowberryBrew = null;

    @SubscribeEvent
    public static void onPotionRegistry(final RegistryEvent.Register<Potion> event) {
        int baseAstralTravelDuration = AstralConfig.getPotionEffectDurations().getAstralTravelDuration();
        registerPotion(event.getRegistry(), AstralEffects.astralTravelEffect, "astral_travel_potion", baseAstralTravelDuration, 0);
        registerPotion(event.getRegistry(), AstralEffects.astralTravelEffect, "long_astral_travel_potion", baseAstralTravelDuration * 2, 0);
        registerPotion(event.getRegistry(), AstralEffects.astralTravelEffect, "strong_astral_travel_potion", baseAstralTravelDuration / 2, 1);

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

        Ingredient base = Ingredient.fromStacks(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), Potions.AWKWARD));
        Ingredient reagant = Ingredient.fromStacks(new ItemStack(AstralItems.snowberry));
        ItemStack snowberryPotion = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), snowberryBrew);
        BrewingRecipeRegistry.addRecipe(new BrewingRecipe(base, reagant, snowberryPotion));

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