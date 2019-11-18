package com.alan199921.astral.tags;

import com.alan199921.astral.Astral;
import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

public class AstralBlockTags {
    public static final Tag<Block> ASTRAL_INTERACT = BlockTags.getCollection().get(new ResourceLocation(Astral.MOD_ID, "astral_interact"));
}
