package com.alan199921.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class SnowberryBush extends Block {
    public SnowberryBush() {
        super(Properties.create(Material.LEAVES)
                .sound(SoundType.STEM)
                .hardnessAndResistance(2.0f));
        setRegistryName("snowberry_bush");
    }
}
