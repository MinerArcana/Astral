package com.alan19.astral.data.providers;

import com.alan19.astral.Astral;
import com.alan19.astral.data.providers.dataproviders.ShapelessNBTRecipeBuilder;
import com.alan19.astral.tags.AstralTags;
import com.alan19.astral.util.Constants;
import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

import static com.alan19.astral.items.AstralItems.*;

public class Recipes extends RecipeProvider {

    public Recipes(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(@Nonnull Consumer<IFinishedRecipe> consumer) {

        ShapelessRecipeBuilder.shapelessRecipe(INTROSPECTION_MEDICINE.get())
                .addIngredient(Tags.Items.MUSHROOMS)
                .addIngredient(Items.POISONOUS_POTATO)
                .addIngredient(FEVERWEED.get())
                .addIngredient(Items.BOWL)
                .addCriterion("bowl", hasItem(Items.BOWL))
                .build(consumer);

        ShapelessRecipeBuilder.shapelessRecipe(TRAVELING_MEDICINE.get())
                .addIngredient(Items.SUGAR)
                .addIngredient(SNOWBERRY.get())
                .addIngredient(Items.BOWL)
                .addIngredient(FEVERWEED.get())
                .addCriterion("bowl", hasItem(Items.BOWL))
                .build(consumer);

        ShapelessRecipeBuilder.shapelessRecipe(ETHERIC_POWDER_ITEM.get(), 3)
                .addIngredient(Items.BLAZE_POWDER, 2)
                .addIngredient(ETHER_DIRT_ITEM.get())
                .addIngredient(Items.REDSTONE, 3)
                .addCriterion("bowl", hasItem(ETHER_DIRT_ITEM.get()))
                .build(consumer, new ResourceLocation(Astral.MOD_ID, "etheric_powder_blaze_powder").toString());

        ShapelessRecipeBuilder.shapelessRecipe(ETHERIC_POWDER_ITEM.get(), 3)
                .addIngredient(Items.GLOWSTONE_DUST)
                .addIngredient(ETHER_DIRT_ITEM.get())
                .addIngredient(Items.REDSTONE, 3)
                .addCriterion("bowl", hasItem(ETHER_DIRT_ITEM.get()))
                .build(consumer, new ResourceLocation(Astral.MOD_ID, "etheric_powder_glowstone_dust").toString());

        ShapedRecipeBuilder.shapedRecipe(OFFERING_BRAZIER_ITEM.get())
                .key('C', Items.CAULDRON)
                .key('F', Items.CAMPFIRE)
                .patternLine(" C ")
                .patternLine(" F ")
                .addCriterion("cauldron", hasItem(Items.CAULDRON))
                .build(consumer);

        ShapelessRecipeBuilder.shapelessRecipe(ETHEREAL_PLANKS_ITEM.get(), 4)
                .addIngredient(ETHEREAL_LOG_ITEM.get())
                .addCriterion("ethereal_wood", hasItem(ETHEREAL_LOG_ITEM.get()))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ETHEREAL_WOOD_ITEM.get(), 3)
                .key('W', ETHEREAL_LOG_ITEM.get())
                .patternLine("WW ")
                .patternLine("WW ")
                .patternLine("   ")
                .addCriterion("ethereal_log", hasItem(ETHEREAL_LOG_ITEM.get()))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ETHEREAL_DOOR_ITEM.get(), 3)
                .key('W', ETHEREAL_PLANKS_ITEM.get())
                .patternLine("WW ")
                .patternLine("WW ")
                .patternLine("WW ")
                .addCriterion("ethereal_door", hasItem(ETHEREAL_PLANKS_ITEM.get()))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(METAPHORIC_BONE.get(), 4)
                .key('P', ETHEREAL_PLANKS_ITEM.get())
                .patternLine("   ")
                .patternLine(" P ")
                .patternLine(" P ")
                .addCriterion("ethereal_planks", hasItem(ETHEREAL_PLANKS_ITEM.get()))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ETHEREAL_TRAPDOOR_ITEM.get(), 2)
                .key('P', ETHEREAL_PLANKS_ITEM.get())
                .patternLine("   ")
                .patternLine("PPP")
                .patternLine("PPP")
                .addCriterion("ethereal_planks", hasItem(ETHEREAL_PLANKS_ITEM.get()))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(STRIPPED_ETHEREAL_WOOD_ITEM.get(), 3)
                .key('W', STRIPPED_ETHEREAL_LOG_ITEM.get())
                .patternLine("WW ")
                .patternLine("WW ")
                .patternLine("   ")
                .addCriterion("stripped_ethereal_log", hasItem(STRIPPED_ETHEREAL_LOG_ITEM.get()))
                .build(consumer);

        createTwoByTwoRecipe(consumer, DREAMWEAVE.get(), DREAMCORD.get(), 1);

        ShapedRecipeBuilder.shapedRecipe(COMFORTABLE_CUSHION_ITEM.get(), 1)
                .key('W', DREAMWEAVE.get())
                .key('L', ItemTags.LEAVES)
                .patternLine("LLL")
                .patternLine("WWW")
                .patternLine("WWW")
                .addCriterion("dreamweave", hasItem(DREAMWEAVE.get()))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(INDEX_OF_KNOWLEDGE_ITEM.get(), 1)
                .key('S', METAPHORIC_STONE_ITEM.get())
                .key('E', SLEEPLESS_EYE.get())
                .key('B', Items.BOOK)
                .patternLine(" B ")
                .patternLine("ESE")
                .patternLine("SSS")
                .addCriterion("metaphoric_stone", hasItem(METAPHORIC_STONE_ITEM.get()))
                .build(consumer);

        createTwoByTwoRecipe(consumer, BONE_SHEETS_ITEM.get(), METAPHORIC_BONE.get(), 1);
        createTwoByTwoRecipe(consumer, Items.GLASS, CRYSTAL_CHITIN.get(), 1, new ResourceLocation(Astral.MOD_ID, "chitin_glass"));
        createTwoByTwoRecipe(consumer, METAPHORIC_FLESH_BLOCK_ITEM.get(), METAPHORIC_FLESH.get(), 1);

        ShapelessNBTRecipeBuilder.shapelessRecipe(Constants.getAstronomicon())
                .addIngredient(AstralTags.BASIC_ASTRAL_PLANTS)
                .addIngredient(Ingredient.fromItems(Items.PAPER), 3)
                .addCriterion("basic_astral_plants", hasItem(AstralTags.BASIC_ASTRAL_PLANTS))
                .addCondition(new ModLoadedCondition("patchouli"))
                .build(consumer, new ResourceLocation(Astral.MOD_ID, "astronomicon"));

        ShapedRecipeBuilder.shapedRecipe(PHANTASMAL_SWORD.get())
                .key('P', PHANTOM_EDGE.get())
                .key('B', METAPHORIC_BONE.get())
                .patternLine(" P ")
                .patternLine(" P ")
                .patternLine(" B ")
                .addCriterion("phantom_edge", hasItem(PHANTOM_EDGE.get()))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(PHANTASMAL_PICKAXE.get())
                .key('P', PHANTOM_EDGE.get())
                .key('B', METAPHORIC_BONE.get())
                .patternLine("PPP")
                .patternLine(" B ")
                .patternLine(" B ")
                .addCriterion("phantom_edge", hasItem(PHANTOM_EDGE.get()))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(PHANTASMAL_SHOVEL.get())
                .key('P', PHANTOM_EDGE.get())
                .key('B', METAPHORIC_BONE.get())
                .patternLine(" P ")
                .patternLine(" B ")
                .patternLine(" B ")
                .addCriterion("phantom_edge", hasItem(PHANTOM_EDGE.get()))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(PHANTASMAL_AXE.get())
                .key('P', PHANTOM_EDGE.get())
                .key('B', METAPHORIC_BONE.get())
                .patternLine("PP ")
                .patternLine("PB ")
                .patternLine(" B ")
                .addCriterion("phantom_edge", hasItem(PHANTOM_EDGE.get()))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(PHANTASMAL_SHEARS.get())
                .key('P', PHANTOM_EDGE.get())
                .patternLine("   ")
                .patternLine(" P ")
                .patternLine("P  ")
                .addCriterion("phantom_edge", hasItem(PHANTOM_EDGE.get()))
                .build(consumer);

        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(BONE_SHEETS_ITEM.get()), METAPHORIC_STONE_ITEM.get(), .1f, 200)
                .addCriterion("metaphoric_bone_block", hasItem(BONE_SHEETS_ITEM.get()))
                .build(consumer);
    }

    private ShapedRecipeBuilder generateTwoByTwoRecipeBuilder(Item output, Item input, int count) {
        char inputFirstLetter = Character.toUpperCase(input.getRegistryName().getPath().charAt(0));
        return ShapedRecipeBuilder.shapedRecipe(output, count)
                .key(inputFirstLetter, input)
                .patternLine("   ")
                .patternLine(String.format(" %s%s", inputFirstLetter, inputFirstLetter))
                .patternLine(String.format(" %s%s", inputFirstLetter, inputFirstLetter))
                .addCriterion(input.getRegistryName().getPath(), hasItem(input));
    }

    private void createTwoByTwoRecipe(@Nonnull Consumer<IFinishedRecipe> consumer, Item output, Item input, int count) {
        generateTwoByTwoRecipeBuilder(output, input, count).build(consumer);
    }

    private void createTwoByTwoRecipe(@Nonnull Consumer<IFinishedRecipe> consumer, Item output, Item input, int count, ResourceLocation resourceLocation) {
        generateTwoByTwoRecipeBuilder(output, input, count).build(consumer, resourceLocation);
    }

    @Nonnull
    @Override
    public String getName() {
        return "Astral Recipes";
    }
}
