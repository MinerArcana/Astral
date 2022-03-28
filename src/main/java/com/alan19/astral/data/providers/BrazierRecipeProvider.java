package com.alan19.astral.data.providers;

import com.alan19.astral.Astral;
import com.alan19.astral.recipe.AstralRecipeSerializer;
import com.google.gson.JsonObject;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

import static net.minecraft.world.item.Items.*;

public class BrazierRecipeProvider extends RecipeProvider {
    public BrazierRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void buildShapelessRecipes(@Nonnull Consumer<FinishedRecipe> consumer) {
        createRecipe("potion", new ItemStack(GLASS_BOTTLE), Ingredient.of(POTION, EXPERIENCE_BOTTLE, GLASS_BOTTLE), consumer);
        createRecipe("bucket", new ItemStack(BUCKET), Ingredient.of(LAVA_BUCKET, WATER_BUCKET, MILK_BUCKET, COD_BUCKET, PUFFERFISH_BUCKET, SALMON_BUCKET, TROPICAL_FISH_BUCKET, BUCKET), consumer);
    }

    private void allowThroughBrazier(@Nonnull Consumer<FinishedRecipe> consumer, Item item, String path, Ingredient ingredient) {
        createRecipe(path, new ItemStack(item), ingredient, consumer);
    }

    private void createRecipe(String name, ItemStack output, Ingredient input, Consumer<FinishedRecipe> consumer) {
        consumer.accept(new FinishedRecipe(new ResourceLocation(Astral.MOD_ID, "brazier/" + name), output, input));
    }

    //Copied it since inner class was private
    private static class FinishedRecipe implements FinishedRecipe {
        private final ResourceLocation id;
        private final ItemStack output;
        private final Ingredient input;
        private final int cookTime;

        private FinishedRecipe(ResourceLocation id, ItemStack output, Ingredient input, int cookTime) {
            this.id = id;
            this.output = output;
            this.input = input;
            this.cookTime = cookTime;
        }

        private FinishedRecipe(ResourceLocation id, ItemStack output, Ingredient input) {
            this.id = id;
            this.output = output;
            this.input = input;
            this.cookTime = 100;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            json.addProperty("cookTime", cookTime);
            json.add("input", input.toJson());

            JsonObject resultObject = serializeItemStack(output);
            json.add("result", resultObject);
        }

        public static JsonObject serializeItemStack(ItemStack output) {
            JsonObject resultObject = new JsonObject();
            resultObject.addProperty("item", ForgeRegistries.ITEMS.getKey(output.getItem()).toString());
            if (output.getCount() > 1) {
                resultObject.addProperty("count", output.getCount());
            }

            if (output.hasTag() && output.getTag() != null) {
                resultObject.addProperty("nbt", output.getTag().toString());
            }
            return resultObject;
        }

        @Override
        @Nonnull
        public ResourceLocation getId() {
            return id;
        }

        @Override
        @Nonnull
        public RecipeSerializer<?> getType() {
            return AstralRecipeSerializer.BRAZIER_DESTROY_SERIALIZER.get();
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
        return "Astral Offering Brazier Recipes";
    }

}
