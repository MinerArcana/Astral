package com.alan19.astral.datagen.providers;

import com.alan19.astral.Astral;
import com.alan19.astral.items.AstralItems;
import net.minecraft.data.*;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class Recipes extends RecipeProvider {

    public Recipes(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(@Nonnull Consumer<IFinishedRecipe> consumer) {

        ShapelessRecipeBuilder.shapelessRecipe(AstralItems.INTROSPECTION_MEDICINE.get())
                .addIngredient(Tags.Items.MUSHROOMS)
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
                .addCriterion("cauldron", hasItem(Items.CAULDRON))
                .build(consumer);

        ShapelessRecipeBuilder.shapelessRecipe(AstralItems.ETHEREAL_PLANKS_ITEM.get(), 4)
                .addIngredient(AstralItems.ETHEREAL_WOOD_ITEM.get())
                .addCriterion("ethereal_wood", hasItem(AstralItems.ETHEREAL_WOOD_ITEM.get()))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(AstralItems.ETHEREAL_DOOR_ITEM.get(), 3)
                .key('W', AstralItems.ETHEREAL_PLANKS_ITEM.get())
                .patternLine("WW ")
                .patternLine("WW ")
                .patternLine("WW ")
                .addCriterion("ethereal_door", hasItem(AstralItems.ETHEREAL_PLANKS_ITEM.get()))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(AstralItems.METAPHORIC_BONE.get(), 4)
                .key('P', AstralItems.ETHEREAL_PLANKS_ITEM.get())
                .patternLine("   ")
                .patternLine(" P ")
                .patternLine(" P ")
                .addCriterion("ethereal_planks", hasItem(AstralItems.ETHEREAL_PLANKS_ITEM.get()))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(AstralItems.ETHEREAL_TRAPDOOR_ITEM.get(), 2)
                .key('P', AstralItems.ETHEREAL_PLANKS_ITEM.get())
                .patternLine("   ")
                .patternLine("PPP")
                .patternLine("PPP")
                .addCriterion("ethereal_planks", hasItem(AstralItems.ETHEREAL_PLANKS_ITEM.get()))
                .build(consumer);

        ShapelessRecipeBuilder.shapelessRecipe(AstralItems.STRIPPED_ETHEREAL_WOOD_ITEM.get(), 4)
                .addIngredient(AstralItems.STRIPPED_ETHEREAL_LOG_ITEM.get())
                .addCriterion("stripped_ethereal_log", hasItem(AstralItems.STRIPPED_ETHEREAL_LOG_ITEM.get()))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(AstralItems.DREAMWEAVE.get(), 1)
                .key('C', AstralItems.DREAMCORD.get())
                .patternLine("   ")
                .patternLine(" CC")
                .patternLine(" CC")
                .addCriterion("dreamcord", hasItem(AstralItems.DREAMCORD.get()))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(AstralItems.COMFORTABLE_CUSHION_ITEM.get(), 1)
                .key('W', Items.LIGHT_BLUE_WOOL)
                .patternLine("   ")
                .patternLine("WWW")
                .patternLine("WWW")
                .addCriterion("light_blue_wool", hasItem(Items.LIGHT_BLUE_WOOL))
                .build(consumer);
    }

    @Nonnull
    @Override
    public String getName() {
        return "Astral Recipes";
    }
}
