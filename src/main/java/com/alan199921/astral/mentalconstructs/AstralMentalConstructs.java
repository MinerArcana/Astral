package com.alan199921.astral.mentalconstructs;

import com.alan199921.astral.Astral;
import com.alan199921.astral.api.AstralAPI;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class AstralMentalConstructs {
    private static final DeferredRegister<MentalConstructType<?>> MENTAL_CONSTRUCTS = new DeferredRegister<>(AstralAPI.MENTAL_CONSTRUCT_TYPES.get(), Astral.MOD_ID);

    public static RegistryObject<MentalConstructType<ComfortableCushion>> GARDEN = MENTAL_CONSTRUCTS.register("garden", () -> new MentalConstructType<>());

    public static void register(IEventBus modBus) {
        MENTAL_CONSTRUCTS.register(modBus);
    }
}
