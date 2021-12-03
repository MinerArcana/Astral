package com.alan19.astral.blocks.etherealblocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

import net.minecraft.block.AbstractBlock.Properties;

public class EtherDirt extends EtherealBlock implements Ethereal {
    public EtherDirt() {
        super(Properties.of(Material.DIRT)
                .strength(.5f)
                .harvestTool(ToolType.SHOVEL)
                .sound(SoundType.GRAVEL)
                .noOcclusion());
    }

}
