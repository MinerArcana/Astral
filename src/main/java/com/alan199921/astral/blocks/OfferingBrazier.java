package com.alan199921.astral.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockRenderLayer;

public class OfferingBrazier extends Block {
    public OfferingBrazier() {
        super(Block.Properties.create(Material.ROCK).hardnessAndResistance(2f)
                .lightValue(14));
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

}
