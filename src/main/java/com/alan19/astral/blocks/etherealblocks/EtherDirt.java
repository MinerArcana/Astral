package com.alan19.astral.blocks.etherealblocks;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.ToolType;

public class EtherDirt extends EtherealBlock implements Ethereal {
    public EtherDirt() {
        super(Properties.of(Material.DIRT)
                .strength(.5f)
                .harvestTool(ToolType.SHOVEL)
                .sound(SoundType.GRAVEL)
                .noOcclusion());
    }

}
