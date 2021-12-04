package com.alan19.astral.blocks.etherealblocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.ToolType;

public class BoneSheets extends EtherealBlock implements Ethereal {
    public BoneSheets() {
        super(Block.Properties.of(Material.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(0).strength(2.0F, 6.0F).noOcclusion());
    }
}
