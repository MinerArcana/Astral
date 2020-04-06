package com.alan199921.astral.datagen;

import com.alan199921.astral.Astral;
import com.alan199921.astral.items.AstralItems;
import com.alan199921.astral.tags.AstralTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.Items;

import java.util.function.Consumer;

public class Recipes extends RecipeProvider {

    public Recipes(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {

        ShapelessRecipeBuilder.shapelessRecipe(AstralItems.INTROSPECTION_MEDICINE.get())
                .addIngredient(AstralTags.MUSHROOMS)
                .addIngredient(Items.POISONOUS_POTATO)
                .addIngredient(AstralItems.FEVERWEED.get())
                .addIngredient(Items.BOWL)
                .setGroup(Astral.MOD_ID)
                .build(consumer);

        ShapelessRecipeBuilder.shapelessRecipe(AstralItems.TRAVELING_MEDICINE.get())
                .addIngredient(Items.SUGAR)
                .addIngredient(AstralItems.SNOWBERRY.get())
                .addIngredient(Items.BOWL)
                .addIngredient(AstralItems.FEVERWEED.get())
                .setGroup(Astral.MOD_ID)
                .build(consumer);
    }
}
