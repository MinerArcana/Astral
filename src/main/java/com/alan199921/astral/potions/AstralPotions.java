package com.alan199921.astral.potions;

import com.alan199921.astral.configs.AstralConfig;
import com.alan199921.astral.effects.AstralEffects;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class AstralPotions {

    private static Potion astralTravelPotion;
    private static Potion longAstralTravelPotion;
    private static Potion strongAstralTravelPotion;

    @SubscribeEvent
    public static void onPotionRegistry(final RegistryEvent.Register<Potion> event) {
        int baseAstralTravelDuration = AstralConfig.getPotionEffectDurations().getAstralTravelDuration();
        astralTravelPotion = registerPotion(event.getRegistry(), AstralEffects.astralTravelEffect, "astral_travel_potion", baseAstralTravelDuration, 0);
        longAstralTravelPotion = registerPotion(event.getRegistry(), AstralEffects.astralTravelEffect, "long_astral_travel_potion", baseAstralTravelDuration * 2, 0);
        strongAstralTravelPotion = registerPotion(event.getRegistry(), AstralEffects.astralTravelEffect, "strong_astral_travel_potion", baseAstralTravelDuration / 2, 1);
    }

    /**
     * Function to create and register a potion with only one effect
     *
     * @param eventRegistry The registry to register the potion to
     * @param effect        What effect should the potion apply
     * @param name          The registry name of the potion
     * @param duration      The duration of the potion, in ticks
     * @param amplifier     The level of the effect
     * @return A potion object
     */
    public static Potion registerPotion(IForgeRegistry<Potion> eventRegistry, Effect effect, String name, int duration, int amplifier) {
        Potion potion = new Potion(new EffectInstance(effect, duration, amplifier)).setRegistryName(name);
        eventRegistry.register(potion);
        return potion;
    }
}
