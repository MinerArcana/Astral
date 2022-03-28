package com.alan19.astral.client.gui;

import com.alan19.astral.network.AstralNetwork;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.resources.ResourceLocation;

public class AstralInventoryButton extends ImageButton {
    public AstralInventoryButton(AbstractContainerScreen<?> gui, int xIn, int yIn, int widthIn, int heightIn, int xTexStartIn, int yTexStartIn, int yDiffTextIn, ResourceLocation resourceLocationIn) {
        super(xIn, yIn, widthIn, heightIn, xTexStartIn, yTexStartIn, yDiffTextIn, resourceLocationIn, onPress -> {
            if (!(gui instanceof AstralInventoryScreen)) {
                if (gui instanceof InventoryScreen) {
                    InventoryScreen inventoryScreen = (InventoryScreen) gui;
                    RecipeBookComponent recipeBookGui = inventoryScreen.getRecipeBookComponent();

                    if (recipeBookGui.isVisible()) {
                        recipeBookGui.toggleVisibility();
                    }
                }
                AstralNetwork.sendOpenAstralInventory();
            }
        });
    }
}
