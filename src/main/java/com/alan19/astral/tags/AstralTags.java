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
    public static final Tag<Block> ASTRAL_INTERACT = new BlockTags.Wrapper(new ResourceLocation(Astral.MOD_ID, "astral_interact"));
    public static final Tag<Item> ASTRAL_PICKUP = new ItemTags.Wrapper(new ResourceLocation(Astral.MOD_ID, "astral_pickup"));
    public static final Tag<Block> SNOWBERRY_SUSTAIN = new BlockTags.Wrapper(new ResourceLocation(Astral.MOD_ID, "snowberry_sustain"));
    public static final Tag<Block> FEVERWEED_SUSTAIN = new BlockTags.Wrapper(new ResourceLocation(Astral.MOD_ID, "feverweed_sustain"));
    public static final Tag<EntityType<?>> NEUTRAL_MOBS = new EntityTypeTags.Wrapper(new ResourceLocation(Astral.MOD_ID, "neutral_mobs"));
    public static final Tag<Block> GARDEN_OBJECTS = new BlockTags.Wrapper(new ResourceLocation(Astral.MOD_ID, "garden_objects"));
    public static final Tag<Block> GARDEN_PLANTS = new BlockTags.Wrapper(new ResourceLocation(Astral.MOD_ID, "garden_plants"));
    public static final Tag<Block> ETHEREAL_VEGETATION_PLANTABLE_ON = new BlockTags.Wrapper(new ResourceLocation(Astral.MOD_ID, "ethereal_vegetation_plantable_on"));
    public static final Tag<Block> ETHERIC_GROWTHS = new BlockTags.Wrapper(new ResourceLocation(Astral.MOD_ID, "etheric_growths"));
    public static final Tag<Block> SMALL_ETHERIC_GROWTHS = new BlockTags.Wrapper(new ResourceLocation(Astral.MOD_ID, "small_etheric_growths"));
    public static final Tag<Block> LARGE_ETHERIC_GROWTHS = new BlockTags.Wrapper(new ResourceLocation(Astral.MOD_ID, "large_etheric_growths"));
    public static final Tag<EntityType<?>> ETHEREAL_BEINGS = new EntityTypeTags.Wrapper(new ResourceLocation(Astral.MOD_ID, "ethereal_beings"));
    public static final Tag<EntityType<?>> SPIRITUAL_BEINGS = new EntityTypeTags.Wrapper(new ResourceLocation(Astral.MOD_ID, "spiritual_beings"));
    public static final Tag<Item> BASIC_ASTRAL_PLANTS = new ItemTags.Wrapper(new ResourceLocation(Astral.MOD_ID, "basic_astral_plants"));
    public static final Tag<EntityType<?>> ATTUNED_ENTITIES = new EntityTypeTags.Wrapper(new ResourceLocation(Astral.MOD_ID, "attuned_entities"));
}
