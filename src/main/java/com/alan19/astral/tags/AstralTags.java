package com.alan19.astral.tags;

import com.alan19.astral.Astral;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

public class AstralTags {
    public static final INamedTag<Block> ASTRAL_INTERACT = BlockTags.createOptional(new ResourceLocation(Astral.MOD_ID, "astral_interact"));
    public static final INamedTag<Item> ASTRAL_PICKUP = ItemTags.createOptional(new ResourceLocation(Astral.MOD_ID, "astral_pickup"));
    public static final INamedTag<Block> SNOWBERRY_SUSTAIN = BlockTags.createOptional(new ResourceLocation(Astral.MOD_ID, "snowberry_sustain"));
    public static final INamedTag<Block> FEVERWEED_SUSTAIN = BlockTags.createOptional(new ResourceLocation(Astral.MOD_ID, "feverweed_sustain"));
    public static final INamedTag<EntityType<?>> NEUTRAL_MOBS = EntityTypeTags.createOptional(new ResourceLocation(Astral.MOD_ID, "neutral_mobs"));
    public static final INamedTag<Block> GARDEN_OBJECTS = BlockTags.createOptional(new ResourceLocation(Astral.MOD_ID, "garden_objects"));
    public static final INamedTag<Block> GARDEN_PLANTS = BlockTags.createOptional(new ResourceLocation(Astral.MOD_ID, "garden_plants"));
    public static final INamedTag<Block> ETHEREAL_VEGETATION_PLANTABLE_ON = BlockTags.createOptional(new ResourceLocation(Astral.MOD_ID, "ethereal_vegetation_plantable_on"));
    public static final INamedTag<Block> ETHERIC_GROWTHS = BlockTags.createOptional(new ResourceLocation(Astral.MOD_ID, "etheric_growths"));
    public static final INamedTag<Block> SMALL_ETHERIC_GROWTHS = BlockTags.createOptional(new ResourceLocation(Astral.MOD_ID, "small_etheric_growths"));
    public static final INamedTag<Block> LARGE_ETHERIC_GROWTHS = BlockTags.createOptional(new ResourceLocation(Astral.MOD_ID, "large_etheric_growths"));
    public static final INamedTag<EntityType<?>> ETHEREAL_BEINGS = EntityTypeTags.createOptional(new ResourceLocation(Astral.MOD_ID, "ethereal_beings"));
    public static final INamedTag<EntityType<?>> SPIRITUAL_BEINGS = EntityTypeTags.createOptional(new ResourceLocation(Astral.MOD_ID, "spiritual_beings"));
    public static final INamedTag<Item> BASIC_ASTRAL_PLANTS = ItemTags.createOptional(new ResourceLocation(Astral.MOD_ID, "basic_astral_plants"));
    public static final INamedTag<EntityType<?>> ATTUNED_ENTITIES = EntityTypeTags.createOptional(new ResourceLocation(Astral.MOD_ID, "attuned_entities"));
}
