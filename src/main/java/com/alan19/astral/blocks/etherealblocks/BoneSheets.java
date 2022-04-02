package com.alan19.astral.blocks.etherealblocks;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

public class BoneSheets extends EtherealBlock implements Ethereal {
    public BoneSheets() {
        super(BlockBehaviour.Properties.of(Material.STONE).strength(2.0F, 6.0F).noOcclusion());
    }
}
