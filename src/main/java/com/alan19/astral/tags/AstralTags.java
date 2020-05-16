package com.alan19.astral.tags;

import com.alan19.astral.Astral;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

public class AstralTags {
    public static final Tag<Block> ASTRAL_INTERACT = BlockTags.getCollection().getOrCreate(new ResourceLocation(Astral.MOD_ID, "astral_interact"));
    public static final Tag<Item> ASTRAL_PICKUP = ItemTags.getCollection().getOrCreate(new ResourceLocation(Astral.MOD_ID, "astral_pickup"));
    public static final Tag<Block> SNOWBERRY_SUSTAIN = BlockTags.getCollection().getOrCreate(new ResourceLocation(Astral.MOD_ID, "snowberry_sustain"));
    public static final Tag<Block> FEVERWEED_SUSTAIN = BlockTags.getCollection().getOrCreate(new ResourceLocation(Astral.MOD_ID, "feverweed_sustain"));
    public static final Tag<Item> MUSHROOMS = ItemTags.getCollection().getOrCreate(new ResourceLocation(Astral.MOD_ID, "mushrooms"));
    public static final Tag<EntityType<?>> NEUTRAL_MOBS = EntityTypeTags.getCollection().getOrCreate(new ResourceLocation(Astral.MOD_ID, "neutral_mobs"));
    public static final Tag<Block> GARDEN_OBJECTS = BlockTags.getCollection().getOrCreate(new ResourceLocation(Astral.MOD_ID, "garden_objects"));
    public static final Tag<Block> GARDEN_PLANTS = BlockTags.getCollection().getOrCreate(new ResourceLocation(Astral.MOD_ID, "garden_plants"));
}
