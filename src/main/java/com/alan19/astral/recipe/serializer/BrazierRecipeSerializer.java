package com.alan19.astral.recipe.serializer;

import com.alan19.astral.recipe.AstralRecipeTypes;
import com.alan19.astral.recipe.BrazierRecipe;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BrazierRecipeSerializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<BrazierRecipe> {
    @Nonnull
    @Override
    public BrazierRecipe fromJson(@Nonnull ResourceLocation recipeId, JsonObject json) {
        final int cookTime = json.get("cookTime").getAsInt();
        final Ingredient input = Ingredient.fromJson(json.get("input"));
        final ItemStack result = ShapedRecipe.itemFromJson(json.getAsJsonObject("result"));
        return new BrazierRecipe(AstralRecipeTypes.BRAZIER_RECIPE, recipeId, cookTime, result, input);
    }

    @Nullable
    @Override
    public BrazierRecipe fromNetwork(@Nonnull ResourceLocation recipeId, FriendlyByteBuf buffer) {
        int cookTime = buffer.readInt();
        ItemStack result = buffer.readItem();
        Ingredient input = Ingredient.fromNetwork(buffer);
        return new BrazierRecipe(AstralRecipeTypes.BRAZIER_RECIPE, recipeId, cookTime, result, input);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, BrazierRecipe recipe) {
        buffer.writeInt(recipe.getCookTime());
        buffer.writeItem(recipe.getResultItem());
        recipe.getIngredient().toNetwork(buffer);
    }
}
