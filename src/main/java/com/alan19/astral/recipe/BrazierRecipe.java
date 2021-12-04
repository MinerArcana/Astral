package com.alan19.astral.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public class BrazierRecipe extends AbstractBrazierRecipe {
    private final ItemStack output;

    public BrazierRecipe(RecipeType<?> type, ResourceLocation id, int cookTime, ItemStack output, Ingredient input) {
        super(type, id, cookTime, input);
        this.output = output;
    }

    public BrazierRecipe(RecipeType<?> type, ResourceLocation id, ItemStack output, Ingredient input) {
        super(type, id, input);
        this.output = output;
    }

    @Override
    public boolean matches(ItemStack items) {
        return getIngredient().test(items);
    }

    @Override
    public boolean matches(Container inv, Level worldIn) {
        return false;
    }

    @Override
    public ItemStack assemble(Container inv) {
        return null;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    @Nonnull
    public ItemStack getResultItem() {
        return output;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return AstralRecipeSerializer.BRAZIER_DESTROY_SERIALIZER.get();
    }
}
