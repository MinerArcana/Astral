package com.alan19.astral.potions;

import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class PotionRegistryGroup {
    private final String name;
    private final EffectInstance baseEffect;
    private EffectInstance longEffect;
    private EffectInstance strongEffect;

    private RegistryObject<Potion> basePotion;
    private RegistryObject<Potion> longPotion;
    private RegistryObject<Potion> strongPotion;
    private IItemProvider longCatalyst = Items.REDSTONE;
    private final IItemProvider strongCatalyst = Items.GLOWSTONE;

    public Potion getBasePotion() {
        return basePotion.get();
    }

    public Potion getLongPotion() {
        return longPotion.get();
    }

    public Potion getStrongPotion() {
        return strongPotion.get();
    }

    public PotionRegistryGroup(String name, EffectInstance baseEffect, EffectInstance longEffect, EffectInstance strongEffect) {
        this.name = name;
        this.baseEffect = baseEffect;
        this.longEffect = longEffect;
        this.strongEffect = strongEffect;
    }

    public PotionRegistryGroup register(DeferredRegister<Potion> potionRegistry) {
        basePotion = potionRegistry.register(name, () -> new Potion(baseEffect));
        longPotion = potionRegistry.register("long_" + name, () -> new Potion(longEffect));
        strongPotion = potionRegistry.register("string_" + name, () -> new Potion(strongEffect));
        return this;
    }

    public PotionRegistryGroup addLongBrew(EffectInstance longEffect) {
        this.longEffect = longEffect;
        return this;
    }

    public PotionRegistryGroup addLongBrew(EffectInstance longEffect, IItemProvider catalyst) {
        this.longEffect = longEffect;
        this.longCatalyst = catalyst;
    }

    public PotionRegistryGroup addStrongBrew(EffectInstance strongEffect) {
        this.strongEffect = strongEffect;
        return this;
    }
}
