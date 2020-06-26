package com.alan19.astral.client.gui;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.RecipeBookContainer;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeItemHelper;

public class AstralInventoryContainer extends RecipeBookContainer<CraftingInventory> {
    public AstralInventoryContainer(int i, PlayerInventory playerInventory) {
        super();
    }

    public AstralInventoryContainer() {
        super();
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return false;
    }

    @Override
    public void fillStackedContents(RecipeItemHelper itemHelperIn) {

    }

    @Override
    public void clear() {

    }

    @Override
    public boolean matches(IRecipe<? super CraftingInventory> recipeIn) {
        return false;
    }

    @Override
    public int getOutputSlot() {
        return 0;
    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public int getSize() {
        return 0;
    }
}
