package com.alan199921.blocks;

import net.minecraft.block.BushBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockRenderLayer;

public class FeverweedBlock extends BushBlock {
    public FeverweedBlock() {
        super(Properties.create(Material.PLANTS)
                .sound(SoundType.PLANT)
                .tickRandomly()
                .doesNotBlockMovement()
                .hardnessAndResistance(0f)
        );
        setRegistryName("feverweed_block");
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public OffsetType getOffsetType() {
        return OffsetType.XYZ;
    }
}
