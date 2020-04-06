package com.alan199921.astral.datagen;

import com.alan199921.astral.Astral;
import com.alan199921.astral.items.AstralItems;
import com.alan199921.astral.tags.AstralTags;
import net.minecraft.data.*;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class Recipes extends RecipeProvider {

    public Recipes(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(@Nonnull Consumer<IFinishedRecipe> consumer) {

        ShapelessRecipeBuilder.shapelessRecipe(AstralItems.INTROSPECTION_MEDICINE.get())
                .addIngredient(AstralTags.MUSHROOMS)
                .addIngredient(Items.POISONOUS_POTATO)
                .addIngredient(AstralItems.FEVERWEED.get())
                .addIngredient(Items.BOWL)
                .addCriterion("bowl", hasItem(Items.BOWL))
                .build(consumer);

        ShapelessRecipeBuilder.shapelessRecipe(AstralItems.TRAVELING_MEDICINE.get())
                .addIngredient(Items.SUGAR)
                .addIngredient(AstralItems.SNOWBERRY.get())
                .addIngredient(Items.BOWL)
                .addIngredient(AstralItems.FEVERWEED.get())
                .addCriterion("bowl", hasItem(Items.BOWL))
                .build(consumer);

        ShapelessRecipeBuilder.shapelessRecipe(AstralItems.ETHERIC_POWDER_ITEM.get(), 3)
                .addIngredient(Items.BLAZE_POWDER, 2)
                .addIngredient(AstralItems.ETHER_DIRT_ITEM.get())
                .addIngredient(Items.REDSTONE, 3)
                .addCriterion("bowl", hasItem(AstralItems.ETHER_DIRT_ITEM.get()))
                .build(consumer, new ResourceLocation(Astral.MOD_ID, "etheric_powder_blaze_powder").toString());

        ShapelessRecipeBuilder.shapelessRecipe(AstralItems.ETHERIC_POWDER_ITEM.get(), 3)
                .addIngredient(Items.GLOWSTONE_DUST)
                .addIngredient(AstralItems.ETHER_DIRT_ITEM.get())
                .addIngredient(Items.REDSTONE, 3)
                .addCriterion("bowl", hasItem(AstralItems.ETHER_DIRT_ITEM.get()))
                .build(consumer, new ResourceLocation(Astral.MOD_ID, "etheric_powder_glowstone_dust").toString());

        ShapedRecipeBuilder.shapedRecipe(AstralItems.OFFERING_BRAZIER_ITEM.get())
                .key('C', Items.CAULDRON)
                .key('F', Items.CAMPFIRE)
                .patternLine(" C ")
                .patternLine(" F ")
                .build(consumer);
    }

    @Nonnull
    @Override
    public String getName() {
        return "Astral Recipes";
    }
}
