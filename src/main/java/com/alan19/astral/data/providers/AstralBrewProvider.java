package com.alan19.astral.data.providers;

import com.alan19.astral.Astral;
import com.alan19.astral.compat.brews.AstralBotaniaBrews;
import com.alan19.astral.items.AstralItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.data.recipes.BrewProvider;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class AstralBrewProvider extends BrewProvider {
    public AstralBrewProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    public void registerRecipes(@Nonnull Consumer<net.minecraft.data.recipes.FinishedRecipe> consumer) {
        createBotanicalBreweryRecipe(consumer,
                AstralBotaniaBrews.ASTRAL_TRAVEL.get(),
                Ingredient.of(Items.NETHER_WART),
                Ingredient.of(AstralItems.TRAVELING_MEDICINE.get(), AstralItems.ETHERIC_POWDER_ITEM.get()),
                Ingredient.of(Items.GLOWSTONE_DUST));
        createBotanicalBreweryRecipe(consumer,
                AstralBotaniaBrews.SNOWBERRY_BREW.get(),
                Ingredient.of(Items.REDSTONE),
                Ingredient.of(AstralItems.SNOWBERRY.get()),
                Ingredient.of(Items.GLOWSTONE_DUST));
        createBotanicalBreweryRecipe(consumer,
                AstralBotaniaBrews.FEVERWEED_BREW.get(),
                Ingredient.of(Items.REDSTONE),
                Ingredient.of(AstralItems.FEVERWEED.get()),
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
