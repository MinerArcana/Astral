package com.alan19.astral.potions;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class PotionRegistryGroup {
    private final String name;
    private final Supplier<EffectInstance> baseEffect;
    private Supplier<EffectInstance> longEffect;
    private Supplier<EffectInstance> strongEffect;

    private RegistryObject<Potion> basePotion;
    private RegistryObject<Potion> longPotion;
    private RegistryObject<Potion> strongPotion;
    private Supplier<Potion> potionBase = () -> Potions.AWKWARD;
    private final Supplier<Ingredient> baseCatalyst;
    private Supplier<Ingredient> longCatalyst = () -> Ingredient.fromItems(Items.REDSTONE);
    private Supplier<Ingredient> strongCatalyst = () -> Ingredient.fromItems(Items.GLOWSTONE);

    public Potion getBasePotion() {
        return basePotion.get();
    }

    public Potion getLongPotion() {
        return longPotion.get();
    }

    public Potion getStrongPotion() {
        return strongPotion.get();
    }

    public PotionRegistryGroup(String name, Supplier<EffectInstance> baseEffect, Supplier<Ingredient> baseCatalyst) {
        this.name = name;
        this.baseEffect = baseEffect;
        this.baseCatalyst = baseCatalyst;
    }

    public PotionRegistryGroup(String name, Supplier<EffectInstance> baseEffect, Supplier<IItemProvider> baseCatalyst, Supplier<Potion> potionBase) {
        this.name = name;
        this.baseEffect = baseEffect;
        this.baseCatalyst = () -> Ingredient.fromItems(baseCatalyst.get());
        this.potionBase = potionBase;
    }

    public PotionRegistryGroup register(DeferredRegister<Potion> potionRegistry) {
        basePotion = potionRegistry.register(name, () -> new Potion(baseEffect.get()));
        BrewingRecipeRegistry.addRecipe(PotionBrews.PotionIngredient.asPotion(potionBase.get()), baseCatalyst.get(), potionToItemStack(basePotion.get()));
        longPotion = potionRegistry.register("long_" + name, () -> new Potion(longEffect.get()));
        BrewingRecipeRegistry.addRecipe(PotionBrews.PotionIngredient.asPotion(basePotion.get()), longCatalyst.get(), potionToItemStack(longPotion.get()));
        strongPotion = potionRegistry.register("string_" + name, () -> new Potion(strongEffect.get()));
        BrewingRecipeRegistry.addRecipe(PotionBrews.PotionIngredient.asPotion(basePotion.get()), strongCatalyst.get(), potionToItemStack(strongPotion.get()));
        return this;
    }

    public PotionRegistryGroup addLongBrew(Supplier<EffectInstance> longEffect) {
        this.longEffect = longEffect;
        return this;
    }

    public PotionRegistryGroup addLongBrew(Supplier<EffectInstance> longEffect, Supplier<IItemProvider> catalyst) {
        this.longEffect = longEffect;
        this.longCatalyst = () -> Ingredient.fromItems(catalyst.get());
        return this;
    }

    public PotionRegistryGroup addStrongBrew(Supplier<EffectInstance> strongEffect) {
        this.strongEffect = strongEffect;
        return this;
    }

    public PotionRegistryGroup addStrongBrew(Supplier<EffectInstance> strongEffect, Supplier<IItemProvider> catalyst) {
        this.strongEffect = strongEffect;
        this.strongCatalyst = () -> Ingredient.fromItems(catalyst.get());
        return this;
    }

    private static ItemStack potionToItemStack(Potion potion) {
        return PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), potion);
    }

}
