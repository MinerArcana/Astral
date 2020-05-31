package com.alan19.astral.blocks.etherealblocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;

public class MetaphoricFleshBlock extends EtherealBlock implements Ethereal {
    public MetaphoricFleshBlock() {
        super(Block.Properties.create(Material.ORGANIC, MaterialColor.RED).hardnessAndResistance(2.0F));
    }
}
