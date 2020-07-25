package com.alan19.astral.recipe;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public abstract class AbstractBrazierRecipe implements IRecipe<IInventory> {

    private final IRecipeType<?> type;
    private final ResourceLocation id;
    private final int cookTime;
    private final Ingredient ingredient;

    public AbstractBrazierRecipe(IRecipeType<?> type, ResourceLocation id, int cookTime, Ingredient ingredient) {
        this.type = type;
        this.id = id;
        this.cookTime = cookTime;
        this.ingredient = ingredient;
    }

    public AbstractBrazierRecipe(IRecipeType<?> type, ResourceLocation id, Ingredient ingredient) {
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
    public IRecipeType<?> getType() {
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
