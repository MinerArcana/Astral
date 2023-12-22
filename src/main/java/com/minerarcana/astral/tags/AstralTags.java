package com.minerarcana.astral.tags;

import com.minerarcana.astral.Astral;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class AstralTags {
    public static final TagKey<Block> SNOWBERRY_PLANTABLE_ON = BlockTags.create(new ResourceLocation(Astral.MOD_ID, "snowberry_plantable_on"));
    public static final TagKey<Block> FEVERWEED_PLANTABLE_ON = BlockTags.create(new ResourceLocation(Astral.MOD_ID, "feverweed_plantable_on"));

    public static final TagKey<Block> ASTRAL_INTERACTABLE = BlockTags.create(new ResourceLocation(Astral.MOD_ID, "astral_interactable"));
    public static final TagKey<Item> ASTRAL_CAN_PICKUP = ItemTags.create(new ResourceLocation(Astral.MOD_ID, "astral_can_pickup"));
    public static final TagKey<Item> ASTRAL_PLANTS = ItemTags.create(new ResourceLocation(Astral.MOD_ID, "astral_plants"));


    // A combination of Spiritual and Ethereal Mobs
    public static final TagKey<EntityType<?>> ATTUNED_MOBS = create("attuned_mobs");

    // Mobs that can deal Astral damage and see Astral creatures
    public static final TagKey<EntityType<?>> SPIRITUAL_MOBS = create("spiritual_mobs");

    // Mobs that are by default Astral Traveling
    public static final TagKey<EntityType<?>> ETHEREAL_MOBS = create("ethereal_mobs");


    private static TagKey<EntityType<?>> create(String pName) {
        return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(Astral.MOD_ID, pName));
    }

}
