package com.alan19.astral.mentalconstructs;

import com.alan19.astral.Astral;
import com.alan19.astral.api.AstralAPI;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class AstralMentalConstructs {
    public static final DeferredRegister<MentalConstructType> MENTAL_CONSTRUCTS = new DeferredRegister<>(AstralAPI.MENTAL_CONSTRUCT_TYPES.get(), Astral.MOD_ID);

    public static final RegistryObject<MentalConstructType> GARDEN = MENTAL_CONSTRUCTS.register("garden", () -> new MentalConstructType(Garden::new));

    public static void register(IEventBus modBus) {
        MENTAL_CONSTRUCTS.register(modBus);
    }
}