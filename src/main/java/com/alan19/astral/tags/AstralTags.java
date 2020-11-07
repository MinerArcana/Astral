package com.alan19.astral.tags;

import com.alan19.astral.Astral;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

public class AstralTags {
    public static final ITag<Block> ASTRAL_INTERACT = BlockTags.getCollection().getTagByID(new ResourceLocation(Astral.MOD_ID, "astral_interact"));
    public static final ITag<Item> ASTRAL_PICKUP = ItemTags.getCollection().getTagByID(new ResourceLocation(Astral.MOD_ID, "astral_pickup"));
    public static final ITag<Block> SNOWBERRY_SUSTAIN = BlockTags.getCollection().getTagByID(new ResourceLocation(Astral.MOD_ID, "snowberry_sustain"));
    public static final ITag<Block> FEVERWEED_SUSTAIN = BlockTags.getCollection().getTagByID(new ResourceLocation(Astral.MOD_ID, "feverweed_sustain"));
    public static final ITag<EntityType<?>> NEUTRAL_MOBS = EntityTypeTags.getCollection().getTagByID(new ResourceLocation(Astral.MOD_ID, "neutral_mobs"));
    public static final ITag<Block> GARDEN_OBJECTS = BlockTags.getCollection().getTagByID(new ResourceLocation(Astral.MOD_ID, "garden_objects"));
    public static final ITag<Block> GARDEN_PLANTS = BlockTags.getCollection().getTagByID(new ResourceLocation(Astral.MOD_ID, "garden_plants"));
    public static final ITag<Block> ETHEREAL_VEGETATION_PLANTABLE_ON = BlockTags.getCollection().getTagByID(new ResourceLocation(Astral.MOD_ID, "ethereal_vegetation_plantable_on"));
    public static final ITag<Block> ETHERIC_GROWTHS = BlockTags.getCollection().getTagByID(new ResourceLocation(Astral.MOD_ID, "etheric_growths"));
    public static final ITag<Block> SMALL_ETHERIC_GROWTHS = BlockTags.getCollection().getTagByID(new ResourceLocation(Astral.MOD_ID, "small_etheric_growths"));
    public static final ITag<Block> LARGE_ETHERIC_GROWTHS = BlockTags.getCollection().getTagByID(new ResourceLocation(Astral.MOD_ID, "large_etheric_growths"));
    public static final ITag<EntityType<?>> ETHEREAL_BEINGS = EntityTypeTags.getCollection().getTagByID(new ResourceLocation(Astral.MOD_ID, "ethereal_beings"));
    public static final ITag<EntityType<?>> SPIRITUAL_BEINGS = EntityTypeTags.getCollection().getTagByID(new ResourceLocation(Astral.MOD_ID, "spiritual_beings"));
    public static final ITag<Item> BASIC_ASTRAL_PLANTS = ItemTags.getCollection().getTagByID(new ResourceLocation(Astral.MOD_ID, "basic_astral_plants"));
    public static final ITag<EntityType<?>> ATTUNED_ENTITIES = EntityTypeTags.getCollection().getTagByID(new ResourceLocation(Astral.MOD_ID, "attuned_entities"));
}
