package com.alan199921.astral.potions;

import com.alan199921.astral.items.AstralItems;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import javax.annotation.Nullable;
import java.util.stream.Stream;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class PotionBrews {
    @SubscribeEvent
    public static void createPotionRecipes(final FMLCommonSetupEvent event) {
        //Astral Travel recipe
        Ingredient travellingMedicineIngredient = Ingredient.fromItems(AstralItems.travellingMedicine);
        createBrewingRecipes(Potions.AWKWARD, travellingMedicineIngredient, AstralPotions.astralTravelPotion, AstralPotions.longAstralTravelPotion, AstralPotions.strongAstralTravelPotion);

        //Snowberry brew recipe
        Ingredient snowberryIngredient = Ingredient.fromStacks(new ItemStack(AstralItems.snowberry));
        createBrewingRecipes(Potions.THICK, snowberryIngredient, AstralPotions.snowberryBrew, AstralPotions.longSnowberryBrew, AstralPotions.strongSnowberryBrew);

        //Feverweed brew recipe
        Ingredient feverweedIngredient = Ingredient.fromStacks(new ItemStack(AstralItems.feverweed));
        createBrewingRecipes(Potions.THICK, feverweedIngredient, AstralPotions.feverweedBrew, AstralPotions.longFeverweedBrew, AstralPotions.strongFeverweedBrew);
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

    public static class PotionIngredient extends Ingredient {

        private ItemStack stack;

        private PotionIngredient(Potion potion, ItemStack stack) {
            super(Stream.of(new SingleItemList(PotionUtils.addPotionToItemStack(stack, potion))));
            this.stack = PotionUtils.addPotionToItemStack(stack, potion);
        }

        public static PotionIngredient asSplashPotion(Potion potion) {
            return new PotionIngredient(potion, new ItemStack(Items.SPLASH_POTION));
        }

        public static PotionIngredient asPotion(Potion potion) {
            return new PotionIngredient(potion, new ItemStack(Items.POTION));
        }

        @Override
        public boolean test(@Nullable ItemStack input) {
            if (input == null) {
                return false;
            } else {
                return this.stack.getItem() == input.getItem() && this.stack.getDamage() == input.getDamage() && this.stack.areShareTagsEqual(input);
            }
        }
    }
}
