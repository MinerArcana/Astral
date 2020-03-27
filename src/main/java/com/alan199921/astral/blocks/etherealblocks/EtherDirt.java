package com.alan199921.astral.blocks.etherealblocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class EtherDirt extends EtherealBlock {
    public EtherDirt() {
        super(Properties.create(Material.EARTH)
                .hardnessAndResistance(.5f)
                .harvestTool(ToolType.SHOVEL)
                .sound(SoundType.GROUND));
    }
}
