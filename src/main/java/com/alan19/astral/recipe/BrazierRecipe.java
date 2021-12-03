package com.alan19.astral.recipe;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BrazierRecipe extends AbstractBrazierRecipe {
    private final ItemStack output;

    public BrazierRecipe(IRecipeType<?> type, ResourceLocation id, int cookTime, ItemStack output, Ingredient input) {
        super(type, id, cookTime, input);
        this.output = output;
    }

    public BrazierRecipe(IRecipeType<?> type, ResourceLocation id, ItemStack output, Ingredient input) {
        super(type, id, input);
        this.output = output;
    }

    @Override
    public boolean matches(ItemStack items) {
        return getIngredient().test(items);
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        return false;
    }

    @Override
    public ItemStack assemble(IInventory inv) {
        return null;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    @Nonnull
    public ItemStack getResultItem() {
        return output;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return AstralRecipeSerializer.BRAZIER_DESTROY_SERIALIZER.get();
    }
}
