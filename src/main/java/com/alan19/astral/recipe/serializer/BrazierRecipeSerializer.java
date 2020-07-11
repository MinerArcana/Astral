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
    public BrazierRecipe read(@Nonnull ResourceLocation recipeId, JsonObject json) {
        final int cookTime = json.get("cookTime").getAsInt();
        final Ingredient input = Ingredient.deserialize(json.get("input"));
        final ItemStack output = ShapedRecipe.deserializeItem(json.getAsJsonObject("output"));
        return new BrazierRecipe(AstralRecipeTypes.BRAZIER_SACRIFICE_RECIPE, recipeId, cookTime, output, input);
    }

    @Nullable
    @Override
    public BrazierRecipe read(@Nonnull ResourceLocation recipeId, PacketBuffer buffer) {
        int cookTime = buffer.readInt();
        ItemStack output = buffer.readItemStack();
        Ingredient input = Ingredient.read(buffer);
        return new BrazierRecipe(AstralRecipeTypes.BRAZIER_SACRIFICE_RECIPE, recipeId, cookTime, output, input);
    }

    @Override
    public void write(PacketBuffer buffer, BrazierRecipe recipe) {
        buffer.writeInt(recipe.getCookTime());
        buffer.writeItemStack(recipe.getRecipeOutput());
        recipe.getIngredient().write(buffer);
    }
}
