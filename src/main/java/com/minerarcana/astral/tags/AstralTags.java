package com.minerarcana.astral.tags;

import com.minerarcana.astral.Astral;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class AstralTags {
    public static final TagKey<Block> SNOWBERRY_PLANTABLE_ON = BlockTags.create(new ResourceLocation(Astral.MOD_ID, "snowberry_plantable_on"));
    public static final TagKey<Block> FEVERWEED_PLANTABLE_ON = BlockTags.create(new ResourceLocation(Astral.MOD_ID, "feverweed_plantable_on"));

}
