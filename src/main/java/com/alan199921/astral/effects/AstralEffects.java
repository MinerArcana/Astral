package com.alan199921.astral.effects;

import net.minecraft.potion.Effect;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class AstralEffects {

    /*
    Instantiation is required for it to not crash with Quark since effects are registered after items and the effect
    on Travelling Medicine would be null
    */
    @ObjectHolder("astral:astral_travel")
    public static final Effect ASTRAL_TRAVEL = new AstralTravelEffect();


    @SubscribeEvent
    public static void onEffectRegistry(final RegistryEvent.Register<Effect> event) {
        registerEffect(event.getRegistry(), ASTRAL_TRAVEL, "astral_travel");
    }

    /**
     * Registers a new effect
     *
     * @param registry The registry to register the effect in
     * @param effect   An instance of an effect
     * @param name     The registry name of the effect
     */
    private static void registerEffect(IForgeRegistry<Effect> registry, Effect effect, String name) {
        Effect namedEffect = effect.setRegistryName(name);
        registry.register(namedEffect);
    }

}
