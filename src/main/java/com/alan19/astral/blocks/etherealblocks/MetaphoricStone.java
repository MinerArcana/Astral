package com.alan19.astral.blocks.etherealblocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.common.ToolType;

public class MetaphoricStone extends EtherealBlock implements Ethereal {
    public MetaphoricStone() {
        super(Block.Properties.create(Material.ROCK, MaterialColor.STONE).hardnessAndResistance(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0).notSolid());
    }
}
