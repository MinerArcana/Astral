package com.alan19.astral.client.gui;

import com.alan19.astral.network.AstralNetwork;
import net.minecraft.client.gui.recipebook.RecipeBookGui;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.ResourceLocation;

public class AstralInventoryButton extends ImageButton {
    public AstralInventoryButton(ContainerScreen<?> gui, int xIn, int yIn, int widthIn, int heightIn, int xTexStartIn, int yTexStartIn, int yDiffTextIn, ResourceLocation resourceLocationIn) {
        super(xIn, yIn, widthIn, heightIn, xTexStartIn, yTexStartIn, yDiffTextIn, resourceLocationIn, onPress -> {
            if (!(gui instanceof AstralInventoryScreen)) {
                if (gui instanceof InventoryScreen) {
                    InventoryScreen inventoryScreen = (InventoryScreen) gui;
                    RecipeBookGui recipeBookGui = inventoryScreen.getRecipeBookComponent();

                    if (recipeBookGui.isVisible()) {
                        recipeBookGui.toggleVisibility();
                    }
                }
                AstralNetwork.sendOpenAstralInventory();
            }
        });
    }
}
