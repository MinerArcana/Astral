package com.alan19.astral.client.gui;

import com.alan19.astral.Astral;
import net.minecraft.util.ResourceLocation;

public class AstralDrawable {
    public static final UiTexture ASTRAL_INVENTORY = new UiTexture(new ResourceLocation(Astral.MOD_ID, "textures/gui/astral_inventory.png"), 256, 256);

    public static final IDrawable ASTRAL_INVENTORY_DRAWABLE = ASTRAL_INVENTORY.getArea(0, 0, 175, 165);
}
