package com.alan19.astral.data.providers.dataproviders;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.IRequirementsStrategy;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.Tag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
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
    private final Advancement.Builder advancementBuilder = Advancement.Builder.builder();
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
        return this.addIngredient(Ingredient.fromTag(tagIn));
    }

    /**
     * Adds an ingredient of the given item.
     */
    public ShapelessNBTRecipeBuilder addIngredient(IItemProvider itemIn) {
        return this.addIngredient(itemIn, 1);
    }

    /**
     * Adds the given ingredient multiple times.
     */
    public ShapelessNBTRecipeBuilder addIngredient(IItemProvider itemIn, int quantity) {
        for (int i = 0; i < quantity; ++i) {
            this.addIngredient(Ingredient.fromItems(itemIn));
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
    public ShapelessNBTRecipeBuilder addCriterion(String name, ICriterionInstance criterionIn) {
        this.advancementBuilder.withCriterion(name, criterionIn);
        return this;
    }

    public ShapelessNBTRecipeBuilder setGroup(String groupIn) {
        this.group = groupIn;
        return this;
    }

    /**
     * Builds this recipe into an {@link IFinishedRecipe}.
     */
    public void build(Consumer<IFinishedRecipe> consumerIn) {
        this.build(consumerIn, ForgeRegistries.ITEMS.getKey(this.result.getItem()));
    }

    /**
     * Builds this recipe into an {@link IFinishedRecipe}. Use {@link #build(Consumer)} if save is the same as the ID for
     * the result.
     */
    public void build(Consumer<IFinishedRecipe> consumerIn, String save) {
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
    public void build(Consumer<IFinishedRecipe> consumerIn, ResourceLocation id) {
        this.validate(id);
        this.advancementBuilder.withParentId(new ResourceLocation("recipes/root")).withCriterion("has_the_recipe", new RecipeUnlockedTrigger.Instance(id)).withRewards(AdvancementRewards.Builder.recipe(id)).withRequirementsStrategy(IRequirementsStrategy.OR);
        consumerIn.accept(new ShapelessNBTRecipeBuilder.Result(id, this.result, this.count, this.group == null ? "" : this.group, this.ingredients, this.advancementBuilder, new ResourceLocation(id.getNamespace(), "recipes/" + this.result.getItem().getGroup().getPath() + "/" + id.getPath()), conditions));
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

    public static class Result implements IFinishedRecipe {
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
        public void serialize(JsonObject json) {
            if (!this.group.isEmpty()) {
                json.addProperty("group", this.group);
            }

            JsonArray jsonArray = new JsonArray();

            this.ingredients.stream().map(Ingredient::serialize).forEach(jsonArray::add);

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

        public IRecipeSerializer<?> getSerializer() {
            return IRecipeSerializer.CRAFTING_SHAPELESS;
        }

        /**
         * Gets the ID for the recipe.
         */
        public ResourceLocation getID() {
            return this.id;
        }

        /**
         * Gets the JSON for the advancement that unlocks this recipe. Null if there is no advancement.
         */
        @Nullable
        public JsonObject getAdvancementJson() {
            return this.advancementBuilder.serialize();
        }

        /**
         * Gets the ID for the advancement associated with this recipe. Should not be null if {@link #getAdvancementJson}
         * is non-null.
         */
        @Nullable
        public ResourceLocation getAdvancementID() {
            return this.advancementId;
        }
    }
}
