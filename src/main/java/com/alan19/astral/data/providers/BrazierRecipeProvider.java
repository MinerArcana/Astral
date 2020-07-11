package com.alan19.astral.data.providers;

import com.alan19.astral.Astral;
import com.google.gson.JsonObject;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.botania.common.crafting.ModRecipeTypes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

public class BrazierRecipeProvider extends RecipeProvider {
    public BrazierRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        createRecipe("potion", new ItemStack(Items.GLASS_BOTTLE), Ingredient.fromItems(Items.POTION), consumer);
        createRecipe("milk", new ItemStack(Items.BUCKET), Ingredient.fromItems(Items.WATER_BUCKET, Items.WATER_BUCKET, Items.MILK_BUCKET), consumer);
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

            JsonObject resultObject = new JsonObject();
            resultObject.addProperty("item", ForgeRegistries.ITEMS.getKey(output.getItem()).toString());
            if (this.output.getCount() > 1) {
                resultObject.addProperty("count", this.output.getCount());
            }

            json.add("result", resultObject);
            if (output.hasTag() && output.getTag() != null) {
                resultObject.addProperty("nbt", output.getTag().toString());
            }

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
        return "Astral Offering Brazier Recipes";
    }

}
