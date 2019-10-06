package com.alan199921.astral.effects;

import net.minecraft.potion.Effect;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class AstralEffects {
    public static Effect astralTravelEffect;


    @SubscribeEvent
    public static void onEffectRegistry(final RegistryEvent.Register<Effect> event) {
        astralTravelEffect = registerEffect(event.getRegistry(), new AstralTravelEffect(), "astral_travel");
    }

    /**
     * Registers a new effect
     *
     * @param registry The registry to register the effect in
     * @param effect   An instance of an effect
     * @param name     The registry name of the effect
     * @return A registered effect object
     */
    private static Effect registerEffect(IForgeRegistry<Effect> registry, Effect effect, String name) {
        Effect namedEffect = effect.setRegistryName(name);
        registry.register(namedEffect);
        return namedEffect;
    }

}
