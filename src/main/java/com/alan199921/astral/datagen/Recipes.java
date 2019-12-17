package com.alan199921.astral.datagen;

import com.alan199921.astral.Astral;
import com.alan199921.astral.items.AstralItems;
import com.alan199921.astral.tags.IngredientTags;
import net.minecraft.data.*;
import net.minecraft.item.Items;

import java.util.function.Consumer;

public class Recipes extends RecipeProvider {

    public Recipes(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {

        ShapelessRecipeBuilder.shapelessRecipe(AstralItems.INTROSPECTION_MEDICINE)
                .addIngredient(IngredientTags.MUSHROOMS)
                .addIngredient(Items.POISONOUS_POTATO)
                .addIngredient(AstralItems.FEVERWEED)
                .addIngredient(Items.BOWL)
                .setGroup(Astral.MOD_ID)
                .build(consumer);

        ShapelessRecipeBuilder.shapelessRecipe(AstralItems.TRAVELLING_MEDICINE)
                .addIngredient(Items.SUGAR)
                .addIngredient(AstralItems.SNOWBERRY)
                .addIngredient(Items.BOWL)
                .addIngredient(AstralItems.FEVERWEED)
                .setGroup(Astral.MOD_ID)
                .build(consumer);
    }
}
