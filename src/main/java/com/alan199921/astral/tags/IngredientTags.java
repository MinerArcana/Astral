package com.alan199921.astral.tags;

import com.alan199921.astral.Astral;
import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

public class IngredientTags {
    public static final Tag<Item> MUSHROOMS = ItemTags.getCollection().get(new ResourceLocation(Astral.MOD_ID, "mushrooms"));
}
