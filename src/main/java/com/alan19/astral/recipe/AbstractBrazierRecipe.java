package com.alan19.astral.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

import javax.annotation.Nonnull;

public abstract class AbstractBrazierRecipe implements Recipe<Container> {

    private final RecipeType<?> type;
    private final ResourceLocation id;
    private final int cookTime;
    private final Ingredient ingredient;

    public AbstractBrazierRecipe(RecipeType<?> type, ResourceLocation id, int cookTime, Ingredient ingredient) {
        this.type = type;
        this.id = id;
        this.cookTime = cookTime;
        this.ingredient = ingredient;
    }

    public AbstractBrazierRecipe(RecipeType<?> type, ResourceLocation id, Ingredient ingredient) {
        this.type = type;
        this.id = id;
        this.ingredient = ingredient;
        this.cookTime = 100;
    }

    @Override
    @Nonnull
    public ResourceLocation getId() {
        return id;
    }

    @Override
    @Nonnull
    public RecipeType<?> getType() {
        return type;
    }

    public int getCookTime() {
        return cookTime;
    }

    public abstract boolean matches(ItemStack items);

    public Ingredient getIngredient() {
        return ingredient;
    }
}
