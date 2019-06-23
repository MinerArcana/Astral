package com.alan199921.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.FoliageColors;
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraft.world.biome.BiomeColors;

import javax.annotation.Nullable;

public class SnowberryBush extends Block implements IBlockColor {
    public SnowberryBush() {
        super(Properties.create(Material.LEAVES)
                .sound(SoundType.STEM)
                .hardnessAndResistance(2.0f));
        setRegistryName("snowberry_bush");

    }


    @Override
    public int getColor(BlockState p_getColor_1_, @Nullable IEnviromentBlockReader p_getColor_2_, @Nullable BlockPos p_getColor_3_, int p_getColor_4_) {
        if (Minecraft.getInstance().world != null && p_getColor_3_ != null) {
            return BiomeColors.getFoliageColor(p_getColor_2_, p_getColor_3_);
        }
        return FoliageColors.getDefault();
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

}
