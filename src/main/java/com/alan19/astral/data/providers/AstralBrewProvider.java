package com.alan19.astral.data.providers;

import com.alan19.astral.Astral;
import com.alan19.astral.compat.brews.AstralBotaniaBrews;
import com.alan19.astral.items.AstralItems;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.data.recipes.BrewProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

public class AstralBrewProvider extends BrewProvider {
    public AstralBrewProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    public void registerRecipes(Consumer<net.minecraft.data.recipes.FinishedRecipe> consumer) {
        createBotanicalBreweryRecipe(consumer, AstralBotaniaBrews.ASTRAL_TRAVEL.get(), Ingredient.of(Items.NETHER_WART), Ingredient.of(AstralItems.TRAVELING_MEDICINE.get(), AstralItems.ETHERIC_POWDER_ITEM.get()), Ingredient.of(Items.GLOWSTONE_DUST));
        createBotanicalBreweryRecipe(consumer, AstralBotaniaBrews.SNOWBERRY_BREW.get(), Ingredient.of(Items.REDSTONE), Ingredient.of(AstralItems.SNOWBERRY.get()), Ingredient.of(Items.GLOWSTONE_DUST));
        createBotanicalBreweryRecipe(consumer, AstralBotaniaBrews.FEVERWEED_BREW.get(), Ingredient.of(Items.REDSTONE), Ingredient.of(AstralItems.FEVERWEED.get()), Ingredient.of(Items.GLOWSTONE_DUST));
    }

    private void createBotanicalBreweryRecipe(Consumer<FinishedRecipe> consumer, Brew brew, Ingredient... ingredients) {
        consumer.accept(new FinishedRecipe(new ResourceLocation(Astral.MOD_ID, "brew/" + brew.getRegistryName().getPath()), brew, ingredients));
    }

    //Copied it since inner class was private
    private static class FinishedBrewRecipe implements net.minecraft.data.recipes.FinishedRecipe {
        private final ResourceLocation id;
        private final Brew brew;
        private final Ingredient[] inputs;

        private FinishedRecipe(ResourceLocation id, Brew brew, Ingredient... inputs) {
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

    @Override
    @Nonnull
    public String getName() {
        return "Astral Botania Brew Recipes";
    }
}
