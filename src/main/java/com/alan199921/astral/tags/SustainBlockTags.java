package com.alan199921.astral.tags;

import com.alan199921.astral.Astral;
import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

public class SustainBlockTags {
    public static final Tag<Block> SNOWBERRY_SUSTAIN = BlockTags.getCollection().get(new ResourceLocation(Astral.MOD_ID, "snowberry_sustain"));
    public static final Tag<Block> FEVERWEED_SUSTAIN = BlockTags.getCollection().get(new ResourceLocation(Astral.MOD_ID, "feverweed_sustain"));
}
