package com.alan19.astral.data.providers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.common.crafting.ModRecipeTypes;

import javax.annotation.Nullable;

//Copied it since inner class was private
class FinishedBrewRecipe implements FinishedRecipe {
    private final ResourceLocation id;
    private final Brew brew;
    private final Ingredient[] inputs;

    FinishedBrewRecipe(ResourceLocation id, Brew brew, Ingredient... inputs) {
        this.id = id;
        this.brew = brew;
        this.inputs = inputs;
    }

    @Override
    public void serializeRecipeData(JsonObject json) {
        json.addProperty("brew", BotaniaAPI.instance().getBrewRegistry().getKey(brew).toString());
        JsonArray ingredients = new JsonArray();
        for (Ingredient ingr : inputs) {
            ingredients.add(ingr.toJson());
        }
        json.add("ingredients", ingredients);
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getType() {
        return ModRecipeTypes.BREW_SERIALIZER;
    }

    @Nullable
    @Override
    public JsonObject serializeAdvancement() {
        return null;
    }

    @Nullable
    @Override
    public ResourceLocation getAdvancementId() {
        return null;
    }
}
