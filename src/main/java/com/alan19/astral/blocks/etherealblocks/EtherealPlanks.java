package com.alan19.astral.blocks.etherealblocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

public class EtherealPlanks extends EtherealBlock implements Ethereal {
    public EtherealPlanks() {
        super(Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion());
    }
}
