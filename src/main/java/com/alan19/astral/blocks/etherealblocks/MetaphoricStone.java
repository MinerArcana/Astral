package com.alan19.astral.blocks.etherealblocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

public class MetaphoricStone extends EtherealBlock implements Ethereal {
    public MetaphoricStone() {
        super(Block.Properties.of(Material.STONE, MaterialColor.STONE).strength(1.5F, 6.0F).noOcclusion());
    }
}
