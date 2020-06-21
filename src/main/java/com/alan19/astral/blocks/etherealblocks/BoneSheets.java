package com.alan19.astral.blocks.etherealblocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BoneSheets extends EtherealBlock implements Ethereal {
    public BoneSheets() {
        super(Block.Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 6.0F).notSolid());
    }
}
