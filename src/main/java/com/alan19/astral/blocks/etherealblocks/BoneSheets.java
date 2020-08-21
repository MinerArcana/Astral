package com.alan19.astral.blocks.etherealblocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class BoneSheets extends EtherealBlock implements Ethereal {
    public BoneSheets() {
        super(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(1).hardnessAndResistance(2.0F, 6.0F).notSolid());
    }
}
