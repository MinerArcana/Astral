package com.minerarcana.astral.data;

import com.minerarcana.astral.Astral;
import com.minerarcana.astral.brews.AstralBotaniaBrews;
import com.minerarcana.astral.items.AstralItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.api.brew.Brew;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class AstralBrewProvider extends RecipeProvider {
    public AstralBrewProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    public void buildCraftingRecipes(@Nonnull Consumer<net.minecraft.data.recipes.FinishedRecipe> consumer) {
        createBotanicalBreweryRecipe(consumer,
                AstralBotaniaBrews.SNOWBERRY_BREW.get(),
                Ingredient.of(Items.REDSTONE),
                Ingredient.of(AstralItems.SNOWBERRIES.get()),
                Ingredient.of(Items.GLOWSTONE_DUST));
        createBotanicalBreweryRecipe(consumer,
                AstralBotaniaBrews.FEVERWEED_BREW.get(),
                Ingredient.of(Items.REDSTONE),
                Ingredient.of(AstralItems.FEVERWEED_ITEM.get()),
                Ingredient.of(Items.GLOWSTONE_DUST));
    }

    private void createBotanicalBreweryRecipe(@NotNull Consumer<net.minecraft.data.recipes.FinishedRecipe> consumer, Brew brew, Ingredient... ingredients) {
        // TODO Possibly throw an exception if brew is not found
        AstralBotaniaBrews.BREWS.getEntries().stream()
                .filter(brewRegistryObject -> brewRegistryObject.get() == brew)
                .findFirst()
                .ifPresent(entry -> consumer.accept(new FinishedBrewRecipe(new ResourceLocation(Astral.MOD_ID, "brew/" + entry.getId()), brew, ingredients)));
    }

    @Override
    @Nonnull
    public String getName() {
        return "Astral Botania Brew Recipes";
    }
}
