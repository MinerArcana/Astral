package com.alan199921.astral.potions;

import com.alan199921.astral.items.AstralItems;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.crafting.IngredientNBT;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class PotionBrews {
    @SubscribeEvent
    public static void createPotionRecipes(final FMLCommonSetupEvent event) {
        //Astral Travel recipe
        Ingredient travellingMedicineIngredient = Ingredient.fromItems(AstralItems.TRAVELLING_MEDICINE);
        createBrewingRecipes(Potions.AWKWARD, travellingMedicineIngredient, AstralPotions.ASTRAL_TRAVEL_POTION, AstralPotions.LONG_ASTRAL_TRAVEL_POTION, AstralPotions.STRONG_ASTRAL_TRAVEL_POTION);

        //Snowberry brew recipe
        Ingredient snowberryIngredient = Ingredient.fromStacks(new ItemStack(AstralItems.SNOWBERRY));
        createBrewingRecipes(Potions.THICK, snowberryIngredient, AstralPotions.SNOWBERRY_BREW, AstralPotions.LONG_SNOWBERRY_BREW, AstralPotions.STRONG_SNOWBERRY_BREW);

        //Feverweed brew recipe
        Ingredient feverweedIngredient = Ingredient.fromStacks(new ItemStack(AstralItems.FEVERWEED));
        createBrewingRecipes(Potions.THICK, feverweedIngredient, AstralPotions.FEVERWEED_BREW, AstralPotions.LONG_FEVERWEED_BREW, AstralPotions.STRONG_FEVERWEED_BREW);
    }

    private static ItemStack potionToItemStack(Potion potion) {
        return PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), potion);
    }

    private static ItemStack potionToSplashPotionItemStack(Potion potion) {
        return PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), potion);
    }

    /**
     * Registers potion recipes for each form of potion (regular, splash)
     *
     * @param base    The potion that is used to create the desired output (usually awkward potion)
     * @param reagent The ingredient that is used with the base ot create the output
     * @param result  The potion that is created from the output
     */
    private static void createBrewingRecipes(Potion base, Ingredient reagent, Potion result, Potion longResult, Potion strongResult) {
        //Regular
        BrewingRecipeRegistry.addRecipe(PotionIngredient.asPotion(base), reagent, potionToItemStack(result));
        Ingredient redstoneIngredient = Ingredient.fromItems(Items.REDSTONE);
        Ingredient glowstoneIngredient = Ingredient.fromItems(Items.GLOWSTONE_DUST);
        BrewingRecipeRegistry.addRecipe(PotionIngredient.asPotion(result), redstoneIngredient, potionToItemStack(longResult));
        BrewingRecipeRegistry.addRecipe(PotionIngredient.asPotion(result), glowstoneIngredient, potionToItemStack(strongResult));

        //Splash
        BrewingRecipeRegistry.addRecipe(PotionIngredient.asSplashPotion(base), reagent, potionToSplashPotionItemStack(result));
        BrewingRecipeRegistry.addRecipe(PotionIngredient.asSplashPotion(result), redstoneIngredient, potionToSplashPotionItemStack(longResult));
        BrewingRecipeRegistry.addRecipe(PotionIngredient.asSplashPotion(result), glowstoneIngredient, potionToSplashPotionItemStack(strongResult));
    }

    public static class PotionIngredient extends IngredientNBT {

        private PotionIngredient(Potion potion, ItemStack stack) {
            super(PotionUtils.addPotionToItemStack(stack, potion));
        }

        public static PotionIngredient asSplashPotion(Potion potion) {
            return new PotionIngredient(potion, new ItemStack(Items.SPLASH_POTION));
        }

        public static PotionIngredient asPotion(Potion potion) {
            return new PotionIngredient(potion, new ItemStack(Items.POTION));
        }
    }
}
