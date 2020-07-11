package com.alan19.astral.data.providers;

import com.alan19.astral.Astral;
import com.alan19.astral.recipe.AstralRecipeSerializer;
import com.google.gson.JsonObject;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

import static net.minecraft.item.Items.*;

public class BrazierRecipeProvider extends RecipeProvider {
    public BrazierRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(@Nonnull Consumer<IFinishedRecipe> consumer) {
        createRecipe("potion", new ItemStack(GLASS_BOTTLE), Ingredient.fromItems(POTION, EXPERIENCE_BOTTLE, GLASS_BOTTLE), consumer);
        createRecipe("bucket", new ItemStack(BUCKET), Ingredient.fromItems(LAVA_BUCKET, WATER_BUCKET, MILK_BUCKET, COD_BUCKET, PUFFERFISH_BUCKET, SALMON_BUCKET, TROPICAL_FISH_BUCKET, BUCKET), consumer);
    }

    private void createRecipe(String name, ItemStack output, Ingredient input, Consumer<IFinishedRecipe> consumer) {
        consumer.accept(new FinishedRecipe(new ResourceLocation(Astral.MOD_ID, "brazier/" + name), output, input));
    }

    //Copied it since inner class was private
    private static class FinishedRecipe implements IFinishedRecipe {
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
        public void serialize(JsonObject json) {
            json.addProperty("cookTime", cookTime);
            json.add("input", input.serialize());

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
        public ResourceLocation getID() {
            return id;
        }

        @Override
        @Nonnull
        public IRecipeSerializer<?> getSerializer() {
            return AstralRecipeSerializer.BRAZIER_DESTROY_SERIALIZER.get();
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
        return "Astral Offering Brazier Recipes";
    }

}
