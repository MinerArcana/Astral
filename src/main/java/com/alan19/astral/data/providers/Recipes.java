package com.alan19.astral.data.providers;

import com.alan19.astral.Astral;
import com.alan19.astral.data.providers.dataproviders.ShapelessNBTRecipeBuilder;
import com.alan19.astral.tags.AstralTags;
import com.alan19.astral.util.Constants;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
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
    protected void buildCraftingRecipes(@Nonnull Consumer<FinishedRecipe> consumer) {

        ShapelessRecipeBuilder.shapeless(INTROSPECTION_MEDICINE.get())
                .requires(Tags.Items.MUSHROOMS)
                .requires(Items.POISONOUS_POTATO)
                .requires(FEVERWEED.get())
                .requires(Items.BOWL)
                .unlockedBy("bowl", has(Items.BOWL))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(TRAVELING_MEDICINE.get())
                .requires(Items.SUGAR)
                .requires(SNOWBERRY.get())
                .requires(Items.BOWL)
                .requires(FEVERWEED.get())
                .unlockedBy("bowl", has(Items.BOWL))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(ETHERIC_POWDER_ITEM.get(), 3)
                .requires(Items.CHARCOAL)
                .requires(ETHER_DIRT_ITEM.get())
                .requires(CRYSTAL_CHITIN.get())
                .unlockedBy("crystal_chitin", has(CRYSTAL_CHITIN.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(OFFERING_BRAZIER_ITEM.get())
                .define('C', Items.CAULDRON)
                .define('F', Items.CAMPFIRE)
                .pattern(" C ")
                .pattern(" F ")
                .unlockedBy("cauldron", has(Items.CAULDRON))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(ETHEREAL_PLANKS_ITEM.get(), 4)
                .requires(ETHEREAL_LOG_ITEM.get())
                .unlockedBy("ethereal_wood", has(ETHEREAL_LOG_ITEM.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ETHEREAL_WOOD_ITEM.get(), 3)
                .define('W', ETHEREAL_LOG_ITEM.get())
                .pattern("WW ")
                .pattern("WW ")
                .pattern("   ")
                .unlockedBy("ethereal_log", has(ETHEREAL_LOG_ITEM.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ETHEREAL_DOOR_ITEM.get(), 3)
                .define('W', ETHEREAL_PLANKS_ITEM.get())
                .pattern("WW ")
                .pattern("WW ")
                .pattern("WW ")
                .unlockedBy("ethereal_door", has(ETHEREAL_PLANKS_ITEM.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(METAPHORIC_BONE.get(), 4)
                .define('P', ETHEREAL_PLANKS_ITEM.get())
                .pattern("   ")
                .pattern(" P ")
                .pattern(" P ")
                .unlockedBy("ethereal_planks", has(ETHEREAL_PLANKS_ITEM.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ETHEREAL_TRAPDOOR_ITEM.get(), 2)
                .define('P', ETHEREAL_PLANKS_ITEM.get())
                .pattern("   ")
                .pattern("PPP")
                .pattern("PPP")
                .unlockedBy("ethereal_planks", has(ETHEREAL_PLANKS_ITEM.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(STRIPPED_ETHEREAL_WOOD_ITEM.get(), 3)
                .define('W', STRIPPED_ETHEREAL_LOG_ITEM.get())
                .pattern("WW ")
                .pattern("WW ")
                .pattern("   ")
                .unlockedBy("stripped_ethereal_log", has(STRIPPED_ETHEREAL_LOG_ITEM.get()))
                .save(consumer);

        createTwoByTwoRecipe(consumer, DREAMWEAVE.get(), DREAMCORD.get(), 1);

        ShapedRecipeBuilder.shaped(COMFORTABLE_CUSHION_ITEM.get(), 1)
                .define('W', DREAMWEAVE.get())
                .define('L', ItemTags.LEAVES)
                .pattern("LLL")
                .pattern("WWW")
                .pattern("WWW")
                .unlockedBy("dreamweave", has(DREAMWEAVE.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(INDEX_OF_KNOWLEDGE_ITEM.get(), 1)
                .define('S', METAPHORIC_STONE_ITEM.get())
                .define('E', SLEEPLESS_EYE.get())
                .define('B', Items.BOOK)
                .pattern(" B ")
                .pattern("ESE")
                .pattern("SSS")
                .unlockedBy("metaphoric_stone", has(METAPHORIC_STONE_ITEM.get()))
                .save(consumer);

        createTwoByTwoRecipe(consumer, METAPHORIC_BONE_BLOCK_ITEM.get(), METAPHORIC_BONE.get(), 1);
        createTwoByTwoRecipe(consumer, Items.GLASS, CRYSTAL_CHITIN.get(), 1, new ResourceLocation(Astral.MOD_ID, "chitin_glass"));
        createTwoByTwoRecipe(consumer, METAPHORIC_FLESH_BLOCK_ITEM.get(), METAPHORIC_FLESH.get(), 1);

        ShapelessNBTRecipeBuilder.shapelessRecipe(Constants.getAstronomicon())
                .addIngredient(AstralTags.BASIC_ASTRAL_PLANTS)
                .addIngredient(Ingredient.of(Items.PAPER), 3)
                .addCriterion("basic_astral_plants", has(AstralTags.BASIC_ASTRAL_PLANTS))
                .addCondition(new ModLoadedCondition("patchouli"))
                .build(consumer, new ResourceLocation(Astral.MOD_ID, "astronomicon"));

        ShapedRecipeBuilder.shaped(PHANTASMAL_SWORD.get())
                .define('P', PHANTOM_EDGE.get())
                .define('B', METAPHORIC_BONE.get())
                .pattern(" P ")
                .pattern(" P ")
                .pattern(" B ")
                .unlockedBy("phantom_edge", has(PHANTOM_EDGE.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(PHANTASMAL_PICKAXE.get())
                .define('P', PHANTOM_EDGE.get())
                .define('B', METAPHORIC_BONE.get())
                .pattern("PPP")
                .pattern(" B ")
                .pattern(" B ")
                .unlockedBy("phantom_edge", has(PHANTOM_EDGE.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(PHANTASMAL_SHOVEL.get())
                .define('P', PHANTOM_EDGE.get())
                .define('B', METAPHORIC_BONE.get())
                .pattern(" P ")
                .pattern(" B ")
                .pattern(" B ")
                .unlockedBy("phantom_edge", has(PHANTOM_EDGE.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(PHANTASMAL_AXE.get())
                .define('P', PHANTOM_EDGE.get())
                .define('B', METAPHORIC_BONE.get())
                .pattern("PP ")
                .pattern("PB ")
                .pattern(" B ")
                .unlockedBy("phantom_edge", has(PHANTOM_EDGE.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(PHANTASMAL_SHEARS.get())
                .define('P', PHANTOM_EDGE.get())
                .pattern("   ")
                .pattern(" P ")
                .pattern("P  ")
                .unlockedBy("phantom_edge", has(PHANTOM_EDGE.get()))
                .save(consumer);

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(METAPHORIC_BONE_BLOCK_ITEM.get()), METAPHORIC_STONE_ITEM.get(), .1f, 200)
                .unlockedBy("metaphoric_bone_block", has(METAPHORIC_BONE_BLOCK_ITEM.get()))
                .save(consumer);
    }

    private ShapedRecipeBuilder generateTwoByTwoRecipeBuilder(Item output, Item input, int count) {
        char inputFirstLetter = Character.toUpperCase(input.getRegistryName().getPath().charAt(0));
        return ShapedRecipeBuilder.shaped(output, count)
                .define(inputFirstLetter, input)
                .pattern("   ")
                .pattern(String.format(" %s%s", inputFirstLetter, inputFirstLetter))
                .pattern(String.format(" %s%s", inputFirstLetter, inputFirstLetter))
                .unlockedBy(input.getRegistryName().getPath(), has(input));
    }

    private void createTwoByTwoRecipe(@Nonnull Consumer<FinishedRecipe> consumer, Item output, Item input, int count) {
        generateTwoByTwoRecipeBuilder(output, input, count).save(consumer);
    }

    private void createTwoByTwoRecipe(@Nonnull Consumer<FinishedRecipe> consumer, Item output, Item input, int count, ResourceLocation resourceLocation) {
        generateTwoByTwoRecipeBuilder(output, input, count).save(consumer, resourceLocation);
    }

    @Nonnull
    @Override
    public String getName() {
        return "Astral Recipes";
    }
}
