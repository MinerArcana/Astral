package com.alan199921.astral.tags;

import com.alan199921.astral.Astral;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

public class AstralTags {
    public static final Tag<Block> ASTRAL_INTERACT = BlockTags.getCollection().getOrCreate(new ResourceLocation(Astral.MOD_ID, "astral_interact"));
    public static final Tag<Item> ASTRAL_PICKUP = ItemTags.getCollection().getOrCreate(new ResourceLocation(Astral.MOD_ID, "astral_pickup"));
    public static final Tag<Block> SNOWBERRY_SUSTAIN = BlockTags.getCollection().get(new ResourceLocation(Astral.MOD_ID, "snowberry_sustain"));
    public static final Tag<Block> FEVERWEED_SUSTAIN = BlockTags.getCollection().get(new ResourceLocation(Astral.MOD_ID, "feverweed_sustain"));
    public static final Tag<Item> MUSHROOMS = ItemTags.getCollection().get(new ResourceLocation(Astral.MOD_ID, "mushrooms"));
}