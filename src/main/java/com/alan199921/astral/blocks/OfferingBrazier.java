package com.alan199921.astral.blocks;

import net.minecraft.block.FurnaceBlock;
import net.minecraft.block.material.Material;

public class OfferingBrazier extends FurnaceBlock {
    public OfferingBrazier() {
        super(Properties.create(Material.ROCK).hardnessAndResistance(2f)
                .lightValue(14));
    }
}
