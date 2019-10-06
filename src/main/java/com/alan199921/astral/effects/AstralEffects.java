package com.alan199921.astral.effects;

import net.minecraft.potion.Effect;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class AstralEffects {
    public static Effect astralTravelEffect;


    @SubscribeEvent
    public static void onEffectRegistry(final RegistryEvent.Register<Effect> event) {
        astralTravelEffect = registerEffect(new AstralTravelEffect(), "astral_travel");
    }

    /**
     * Registers a new effect
     *
     * @param effect An instance of an effect
     * @param name   The registry name of the effect
     * @return A registered effect object
     */
    private static Effect registerEffect(Effect effect, String name) {
        Effect namedEffect = effect.setRegistryName(name);
        ForgeRegistries.POTIONS.register(namedEffect);
        return namedEffect;
    }

}
