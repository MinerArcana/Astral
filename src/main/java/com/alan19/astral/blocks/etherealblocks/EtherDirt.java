package com.alan19.astral.blocks.etherealblocks;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;

public class EtherDirt extends EtherealBlock implements Ethereal {
    public EtherDirt() {
        super(Properties.of(Material.DIRT)
                .strength(.5f)
                .sound(SoundType.GRAVEL)
                .noOcclusion());
    }

}
