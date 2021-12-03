package com.alan19.astral.recipe.serializer;

import com.alan19.astral.recipe.AstralRecipeTypes;
import com.alan19.astral.recipe.BrazierRecipe;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BrazierRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<BrazierRecipe> {
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
    public BrazierRecipe fromNetwork(@Nonnull ResourceLocation recipeId, PacketBuffer buffer) {
        int cookTime = buffer.readInt();
        ItemStack result = buffer.readItem();
        Ingredient input = Ingredient.fromNetwork(buffer);
        return new BrazierRecipe(AstralRecipeTypes.BRAZIER_RECIPE, recipeId, cookTime, result, input);
    }

    @Override
    public void toNetwork(PacketBuffer buffer, BrazierRecipe recipe) {
        buffer.writeInt(recipe.getCookTime());
        buffer.writeItem(recipe.getResultItem());
        recipe.getIngredient().toNetwork(buffer);
    }
}
