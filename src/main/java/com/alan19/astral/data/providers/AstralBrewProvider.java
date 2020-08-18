package com.alan19.astral.data.providers;

import com.alan19.astral.Astral;
import com.alan19.astral.compat.brews.AstralBotaniaBrews;
import com.alan19.astral.items.AstralItems;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.data.recipes.BrewProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.function.Consumer;

public class AstralBrewProvider extends BrewProvider {
    public AstralBrewProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void registerRecipes(@Nonnull Consumer<IFinishedRecipe> consumer) {
        createBotanicalBreweryRecipe(consumer, AstralBotaniaBrews.ASTRAL_TRAVEL, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(AstralItems.TRAVELING_MEDICINE.get(), AstralItems.ETHERIC_POWDER_ITEM.get()), Ingredient.fromItems(Items.GLOWSTONE_DUST));
        createBotanicalBreweryRecipe(consumer, AstralBotaniaBrews.SNOWBERRY_BREW, Ingredient.fromItems(Items.REDSTONE), Ingredient.fromItems(AstralItems.SNOWBERRY.get()), Ingredient.fromItems(Items.GLOWSTONE_DUST));
        createBotanicalBreweryRecipe(consumer, AstralBotaniaBrews.FEVERWEED_BREW, Ingredient.fromItems(Items.REDSTONE), Ingredient.fromItems(AstralItems.FEVERWEED.get()), Ingredient.fromItems(Items.GLOWSTONE_DUST));
    }

    private void createBotanicalBreweryRecipe(Consumer<IFinishedRecipe> consumer, Brew brew, Ingredient... ingredients) {
        consumer.accept(new FinishedRecipe(new ResourceLocation(Astral.MOD_ID, "brew/" + brew.getRegistryName().getPath()), brew, ingredients));
    }

    //Copied it since inner class was private
    private static class FinishedRecipe implements IFinishedRecipe {
        private final ResourceLocation id;
        private final Brew brew;
        private final Ingredient[] inputs;

        private FinishedRecipe(ResourceLocation id, Brew brew, Ingredient... inputs) {
            this.id = id;
            this.brew = brew;
            this.inputs = inputs;
        }

        @Override
        public void serialize(JsonObject json) {
            json.addProperty("brew", brew.getRegistryName().toString());
            JsonArray ingredients = new JsonArray();
            Arrays.stream(inputs).map(Ingredient::serialize).forEach(ingredients::add);
            json.add("ingredients", ingredients);
            final JsonObject isBotaniaLoaded = CraftingHelper.serialize(new ModLoadedCondition("botania"));
            JsonArray conditionArray = new JsonArray();
            conditionArray.add(isBotaniaLoaded);
            json.add("conditions", conditionArray);
        }

        @Override
        @Nonnull
        public ResourceLocation getID() {
            return id;
        }

        @Override
        @Nonnull
        public IRecipeSerializer<?> getSerializer() {
            return ModRecipeTypes.BREW_SERIALIZER;
        }

        @Nullable
        @Override
        public JsonObject getAdvancementJson() {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementID() {
            return null;
        }
    }

    @Override
    @Nonnull
    public String getName() {
        return "Astral Botania Brew Recipes";
    }
}
