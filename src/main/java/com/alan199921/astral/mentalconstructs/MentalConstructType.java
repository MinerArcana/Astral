package com.alan199921.astral.mentalconstructs;

import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.function.Supplier;

public class MentalConstructType<T> extends ForgeRegistryEntry<MentalConstructType<?>> {
    private final Supplier<MentalConstruct> factory;

    public MentalConstructType(Supplier<MentalConstruct> factory) {
        this.factory = factory;
    }

    public MentalConstruct create() {
        return this.factory.get();
    }
}
