package com.minerarcana.astral.data;

import com.minerarcana.astral.items.AstralItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;

import javax.annotation.Nonnull;
import java.util.function.Consumer;


public class AstralRecipes extends RecipeProvider {

    public AstralRecipes(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void buildCraftingRecipes(@Nonnull Consumer<FinishedRecipe> consumer) {

        ShapelessRecipeBuilder.shapeless(AstralItems.TRAVELING_MEDICINE.get())
                .requires(Items.SUGAR)
                .requires(AstralItems.SNOWBERRIES.get())
                .requires(Items.BOWL)
                .requires(AstralItems.FEVERWEED_ITEM.get())
                .unlockedBy("bowl", has(Items.BOWL))
                .save(consumer);
    }

    @Nonnull
    @Override
    public String getName() {
        return "Astral Recipes";
    }
}
