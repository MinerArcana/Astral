package com.alan19.astral.data.providers.dataproviders;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Adapted from https://github.com/Rongmario/AntimatterAPI/blob/master/src/main/java/muramasa/antimatter/datagen/builder/AntimatterShapelessRecipeBuilder.java with permission from Rongmario
 */
public class ShapelessNBTRecipeBuilder {
    private static final Logger LOGGER = LogManager.getLogger();
    private final ItemStack result;
    private final int count;
    private final List<Ingredient> ingredients = Lists.newArrayList();
    private final Advancement.Builder advancementBuilder = Advancement.Builder.advancement();
    private String group;
    private List<ICondition> conditions = new ArrayList<>();

    public ShapelessNBTRecipeBuilder(ItemStack resultIn, int countIn) {
        this.result = resultIn;
        this.count = countIn;
    }

    /**
     * Creates a new builder for a shapeless recipe.
     */
    public static ShapelessNBTRecipeBuilder shapelessRecipe(ItemStack resultIn) {
        return new ShapelessNBTRecipeBuilder(resultIn, 1);
    }

    /**
     * Creates a new builder for a shapeless recipe.
     */
    public static ShapelessNBTRecipeBuilder shapelessRecipe(ItemStack resultIn, int countIn) {
        return new ShapelessNBTRecipeBuilder(resultIn, countIn);
    }

    /**
     * Adds an ingredient that can be any item in the given tag.
     */
    public ShapelessNBTRecipeBuilder addIngredient(Tag<Item> tagIn) {
        return this.addIngredient(Ingredient.of(tagIn));
    }

    /**
     * Adds an ingredient of the given item.
     */
    public ShapelessNBTRecipeBuilder addIngredient(ItemLike itemIn) {
        return this.addIngredient(itemIn, 1);
    }

    /**
     * Adds the given ingredient multiple times.
     */
    public ShapelessNBTRecipeBuilder addIngredient(ItemLike itemIn, int quantity) {
        for (int i = 0; i < quantity; ++i) {
            this.addIngredient(Ingredient.of(itemIn));
        }

        return this;
    }

    /**
     * Adds an ingredient.
     */
    public ShapelessNBTRecipeBuilder addIngredient(Ingredient ingredientIn) {
        return this.addIngredient(ingredientIn, 1);
    }

    /**
     * Adds an ingredient multiple times.
     */
    public ShapelessNBTRecipeBuilder addIngredient(Ingredient ingredientIn, int quantity) {
        for (int i = 0; i < quantity; ++i) {
            this.ingredients.add(ingredientIn);
        }

        return this;
    }

    /**
     * Adds a criterion needed to unlock the recipe.
     */
    public ShapelessNBTRecipeBuilder addCriterion(String name, CriterionTriggerInstance criterionIn) {
        this.advancementBuilder.addCriterion(name, criterionIn);
        return this;
    }

    public ShapelessNBTRecipeBuilder setGroup(String groupIn) {
        this.group = groupIn;
        return this;
    }

    /**
     * Builds this recipe into an {@link IFinishedRecipe}.
     */
    public void build(Consumer<FinishedRecipe> consumerIn) {
        this.build(consumerIn, ForgeRegistries.ITEMS.getKey(this.result.getItem()));
    }

    /**
     * Builds this recipe into an {@link IFinishedRecipe}. Use {@link #build(Consumer)} if save is the same as the ID for
     * the result.
     */
    public void build(Consumer<FinishedRecipe> consumerIn, String save) {
        ResourceLocation resourcelocation = ForgeRegistries.ITEMS.getKey(this.result.getItem());
        if ((new ResourceLocation(save)).equals(resourcelocation)) {
            throw new IllegalStateException("Shapeless Recipe " + save + " should remove its 'save' argument");
        }
        else {
            this.build(consumerIn, new ResourceLocation(save));
        }
    }

    /**
     * Builds this recipe into an {@link IFinishedRecipe}.
     */
    public void build(Consumer<FinishedRecipe> consumerIn, ResourceLocation id) {
        this.validate(id);
        this.advancementBuilder.parent(new ResourceLocation("recipes/root")).addCriterion("has_the_recipe", new RecipeUnlockedTrigger.TriggerInstance(EntityPredicate.Composite.ANY, id)).rewards(AdvancementRewards.Builder.recipe(id)).requirements(RequirementsStrategy.OR);
        consumerIn.accept(new ShapelessNBTRecipeBuilder.Result(id, this.result, this.count, this.group == null ? "" : this.group, this.ingredients, this.advancementBuilder, new ResourceLocation(id.getNamespace(), "recipes/" + this.result.getItem().getItemCategory().getRecipeFolderName() + "/" + id.getPath()), conditions));
    }

    /**
     * Makes sure that this recipe is valid and obtainable.
     */
    private void validate(ResourceLocation id) {
        if (this.advancementBuilder.getCriteria().isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + id);
        }
    }

    /**
     * Adds a condition to the recipe
     * @param condition A condition that has to be fulfulled for the recipe to load
     * @return A ShapelessNBTRecipeBuilder that contains the new condition
     */
    public ShapelessNBTRecipeBuilder addCondition(ICondition condition) {
        conditions.add(condition);
        return this;
    }

    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final ItemStack resultItemStack;
        private final int count;
        private final String group;
        private final List<Ingredient> ingredients;
        private final Advancement.Builder advancementBuilder;
        private final ResourceLocation advancementId;
        private final List<ICondition> conditions;

        public Result(ResourceLocation idIn, ItemStack resultIn, int countIn, String groupIn, List<Ingredient> ingredientsIn, Advancement.Builder advancementBuilderIn, ResourceLocation advancementIdIn, List<ICondition> conditions) {
            this.id = idIn;
            this.resultItemStack = resultIn;
            this.count = countIn;
            this.group = groupIn;
            this.ingredients = ingredientsIn;
            this.advancementBuilder = advancementBuilderIn;
            this.advancementId = advancementIdIn;
            this.conditions = conditions;
        }

        @ParametersAreNonnullByDefault
        public void serializeRecipeData(JsonObject json) {
            if (!this.group.isEmpty()) {
                json.addProperty("group", this.group);
            }

            JsonArray jsonArray = new JsonArray();

            this.ingredients.stream().map(Ingredient::toJson).forEach(jsonArray::add);

            json.add("ingredients", jsonArray);

            JsonObject resultObject = new JsonObject();
            resultObject.addProperty("item", ForgeRegistries.ITEMS.getKey(this.resultItemStack.getItem()).toString());
            if (this.count > 1) {
                resultObject.addProperty("count", this.count);
            }

            json.add("result", resultObject);
            if (resultItemStack.hasTag()) {
                resultObject.addProperty("nbt", resultItemStack.getTag().toString());
            }

            for (ICondition condition : conditions) {
                JsonArray conds = new JsonArray();
                conds.add(CraftingHelper.serialize(condition));
                json.add("conditions", conds);
            }

        }

        public RecipeSerializer<?> getType() {
            return RecipeSerializer.SHAPELESS_RECIPE;
        }

        /**
         * Gets the ID for the recipe.
         */
        public ResourceLocation getId() {
            return this.id;
        }

        /**
         * Gets the JSON for the advancement that unlocks this recipe. Null if there is no advancement.
         */
        @Nullable
        public JsonObject serializeAdvancement() {
            return this.advancementBuilder.serializeToJson();
        }

        /**
         * Gets the ID for the advancement associated with this recipe. Should not be null if {@link #getAdvancementJson}
         * is non-null.
         */
        @Nullable
        public ResourceLocation getAdvancementId() {
            return this.advancementId;
        }
    }
}
