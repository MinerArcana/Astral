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
        Ingredient awkwardPotion = new PotionIngredient(Potions.AWKWARD);
        Ingredient redstoneIngredient = Ingredient.fromStacks(new ItemStack(Items.REDSTONE));
        Ingredient glowstoneIngredient = Ingredient.fromStacks(new ItemStack(Items.GLOWSTONE_DUST));
        Ingredient snowberryBrewIngredient = new PotionIngredient(AstralPotions.snowberryBrew);
        Ingredient feverweedBrewIngredient = new PotionIngredient(AstralPotions.feverweedBrew);

        //Snowberry brew recipe
        Ingredient snowberryIngredient = Ingredient.fromStacks(new ItemStack(AstralItems.snowberry));
        BrewingRecipeRegistry.addRecipe(awkwardPotion, snowberryIngredient, potionToItemStack(AstralPotions.snowberryBrew));
        BrewingRecipeRegistry.addRecipe(snowberryBrewIngredient, redstoneIngredient, potionToItemStack(AstralPotions.longSnowberryBrew));
        BrewingRecipeRegistry.addRecipe(snowberryBrewIngredient, glowstoneIngredient, potionToItemStack(AstralPotions.strongSnowberryBrew));

        //Feverweed brew recipe
        Ingredient feverweedIngredient = Ingredient.fromStacks(new ItemStack(AstralItems.feverweed));
        BrewingRecipeRegistry.addRecipe(awkwardPotion, feverweedIngredient, potionToItemStack(AstralPotions.feverweedBrew));
        BrewingRecipeRegistry.addRecipe(feverweedBrewIngredient, redstoneIngredient, potionToItemStack(AstralPotions.longFeverweedBrew));
        BrewingRecipeRegistry.addRecipe(feverweedBrewIngredient, glowstoneIngredient, potionToItemStack(AstralPotions.strongFeverweedBrew));
    }

    private static ItemStack potionToItemStack(Potion potion) {
        return PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), potion);
    }

    public static class PotionIngredient extends Ingredient {

        private ItemStack stack;

        PotionIngredient(Potion potion) {
            super(Stream.of(new SingleItemList(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), potion))));
            this.stack = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), potion);
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
