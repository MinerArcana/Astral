package com.alan19.astral.tags;

import com.alan19.astral.Astral;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class AstralTags {
    public static final TagKey<Block> ASTRAL_INTERACT = BlockTags.create(new ResourceLocation(Astral.MOD_ID, "astral_interact"));
    public static final TagKey<Item> ASTRAL_PICKUP = ItemTags.create(new ResourceLocation(Astral.MOD_ID, "astral_pickup"));
    public static final TagKey<Block> SNOWBERRY_SUSTAIN = BlockTags.create(new ResourceLocation(Astral.MOD_ID, "snowberry_sustain"));
    public static final TagKey<Block> FEVERWEED_SUSTAIN = BlockTags.create(new ResourceLocation(Astral.MOD_ID, "feverweed_sustain"));
    public static final TagKey<EntityType<?>> NEUTRAL_MOBS = create(new ResourceLocation(Astral.MOD_ID, "neutral_mobs"));
    public static final TagKey<Block> GARDEN_OBJECTS = BlockTags.create(new ResourceLocation(Astral.MOD_ID, "garden_objects"));
    public static final TagKey<Block> GARDEN_PLANTS = BlockTags.create(new ResourceLocation(Astral.MOD_ID, "garden_plants"));
    public static final TagKey<Block> ETHEREAL_VEGETATION_PLANTABLE_ON = BlockTags.create(new ResourceLocation(Astral.MOD_ID, "ethereal_vegetation_plantable_on"));
    public static final TagKey<Block> ETHERIC_GROWTHS = BlockTags.create(new ResourceLocation(Astral.MOD_ID, "etheric_growths"));
    public static final TagKey<Block> SMALL_ETHERIC_GROWTHS = BlockTags.create(new ResourceLocation(Astral.MOD_ID, "small_etheric_growths"));
    public static final TagKey<Block> LARGE_ETHERIC_GROWTHS = BlockTags.create(new ResourceLocation(Astral.MOD_ID, "large_etheric_growths"));
    public static final TagKey<EntityType<?>> ETHEREAL_BEINGS = create(new ResourceLocation(Astral.MOD_ID, "ethereal_beings"));
    public static final TagKey<EntityType<?>> SPIRITUAL_BEINGS = create(new ResourceLocation(Astral.MOD_ID, "spiritual_beings"));
    public static final TagKey<Item> BASIC_ASTRAL_PLANTS = ItemTags.create(new ResourceLocation(Astral.MOD_ID, "basic_astral_plants"));
    public static final TagKey<EntityType<?>> ATTUNED_ENTITIES = create(new ResourceLocation(Astral.MOD_ID, "attuned_entities"));

    public static TagKey<EntityType<?>> create(ResourceLocation location) {
        return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, location);
    }
}
