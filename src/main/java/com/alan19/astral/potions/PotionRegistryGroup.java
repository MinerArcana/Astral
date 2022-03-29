package com.alan19.astral.potions;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.crafting.NBTIngredient;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.Optional;
import java.util.function.Supplier;

public class PotionRegistryGroup {
    private final String name;

    private final Supplier<Potion> baseEffect;
    private Supplier<Potion> longEffect;
    private Supplier<Potion> strongEffect;

    private RegistryObject<Potion> basePotion;
    private RegistryObject<Potion> longPotion;
    private RegistryObject<Potion> strongPotion;

    private final Supplier<Ingredient> baseReagent;
    private Supplier<Potion> potionBase = () -> Potions.AWKWARD;
    private Supplier<Ingredient> longReagent = () -> Ingredient.of(Items.REDSTONE);
    private Supplier<Ingredient> strongReagent = () -> Ingredient.of(Items.GLOWSTONE_DUST);

    public Optional<Potion> convertSupplierToOptional(RegistryObject<Potion> potion) {
        return Optional.ofNullable(potion)
                .filter(RegistryObject::isPresent)
                .map(RegistryObject::get);
    }

    public RegistryObject<Potion> getBasePotion() {
        return basePotion;
    }

    public Optional<Potion> getLongPotion() {
        return convertSupplierToOptional(longPotion);
    }

    public Optional<Potion> getStrongPotion() {
        return convertSupplierToOptional(strongPotion);
    }

    public PotionRegistryGroup(String name, Supplier<Potion> baseEffect, Supplier<Ingredient> baseReagent) {
        this.name = name;
        this.baseEffect = baseEffect;
        this.baseReagent = baseReagent;
    }

    @SubscribeEvent
    public void registerBrewingRecipes() {
        BrewingRecipeRegistry.addRecipe(PotionIngredient.asPotion(potionBase.get()), baseReagent.get(), potionToItemStack(basePotion.get()));
        BrewingRecipeRegistry.addRecipe(PotionIngredient.asSplashPotion(potionBase.get()), baseReagent.get(), potionToSplashPotionItemStack(basePotion.get()));
        if (longEffect != null) {
            BrewingRecipeRegistry.addRecipe(PotionIngredient.asPotion(basePotion.get()), longReagent.get(), potionToItemStack(longPotion.get()));
            BrewingRecipeRegistry.addRecipe(PotionIngredient.asSplashPotion(basePotion.get()), longReagent.get(), potionToSplashPotionItemStack(longPotion.get()));
        }
        if (strongEffect != null) {
            BrewingRecipeRegistry.addRecipe(PotionIngredient.asPotion(basePotion.get()), strongReagent.get(), potionToItemStack(strongPotion.get()));
            BrewingRecipeRegistry.addRecipe(PotionIngredient.asSplashPotion(basePotion.get()), strongReagent.get(), potionToSplashPotionItemStack(strongPotion.get()));
        }


    }

    public PotionRegistryGroup register(DeferredRegister<Potion> potionRegistry) {
        basePotion = potionRegistry.register(name, baseEffect);
        if (longEffect != null) {
            longPotion = potionRegistry.register("long_" + name, longEffect);
        }
        if (strongEffect != null) {
            strongPotion = potionRegistry.register("strong_" + name, strongEffect);
        }
        return this;
    }

    public PotionRegistryGroup addLongBrew(Supplier<Potion> longEffect) {
        this.longEffect = longEffect;
        return this;
    }

    public PotionRegistryGroup addLongBrew(Supplier<Potion> longEffect, Supplier<ItemLike> reagent) {
        this.longEffect = longEffect;
        this.longReagent = () -> Ingredient.of(reagent.get());
        return this;
    }

    public PotionRegistryGroup addStrongBrew(Supplier<Potion> strongEffect) {
        this.strongEffect = strongEffect;
        return this;
    }

    public PotionRegistryGroup addStrongBrew(Supplier<Potion> strongEffect, Supplier<ItemLike> reagent) {
        this.strongEffect = strongEffect;
        this.strongReagent = () -> Ingredient.of(reagent.get());
        return this;
    }

    public PotionRegistryGroup setBase(Supplier<Potion> base) {
        this.potionBase = base;
        return this;
    }

    private static ItemStack potionToItemStack(Potion potion) {
        return PotionUtils.setPotion(new ItemStack(Items.POTION), potion);
    }

    private static ItemStack potionToSplashPotionItemStack(Potion potion) {
        return PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), potion);
    }

    public static class PotionIngredient extends NBTIngredient {

        private PotionIngredient(Potion potion, ItemStack stack) {
            super(PotionUtils.setPotion(stack, potion));
        }

        public static PotionIngredient asSplashPotion(Potion potion) {
            return new PotionIngredient(potion, new ItemStack(Items.SPLASH_POTION));
        }

        public static PotionIngredient asPotion(Potion potion) {
            return new PotionIngredient(potion, new ItemStack(Items.POTION));
        }
    }
}
