package com.alan199921.astral.blocks;

import net.minecraft.block.ShearableDoublePlantBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.world.GrassColors;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;

import static com.alan199921.astral.blocks.AstralBlocks.*;

@OnlyIn(Dist.CLIENT)
public class BlockRenderHandler {
    private BlockRenderHandler() {
        //Static function only
    }

    public static void setRenderLayers() {
        RenderType cutout = RenderType.getCutout();
        RenderTypeLookup.setRenderLayer(FEVERWEED_BLOCK.get(), cutout);
        RenderTypeLookup.setRenderLayer(SNOWBERRY_BUSH.get(), cutout);
        RenderTypeLookup.setRenderLayer(OFFERING_BRAZIER.get(), cutout);
        RenderTypeLookup.setRenderLayer(ETHEREAL_GRASS.get(), cutout);
        RenderTypeLookup.setRenderLayer(ETHEREAL_FERN.get(), cutout);
        RenderTypeLookup.setRenderLayer(TALL_ETHEREAL_GRASS.get(), cutout);
        RenderTypeLookup.setRenderLayer(LARGE_ETHEREAL_FERN.get(), cutout);
    }

    public static void registerBiomeBasedBlockColors() {
        final BlockColors blockColors = Minecraft.getInstance().getBlockColors();
        blockColors.register((blockState, lightReader, blockPos, i) -> {
            if (blockState.get(ShearableDoublePlantBlock.PLANT_HALF) == DoubleBlockHalf.UPPER) {
                if (lightReader != null && blockPos != null) {
                    Color color = new Color(BiomeColors.getGrassColor(lightReader, blockPos.down()));
                    int adjustedBlue = color.getBlue() + 50;
                    int adjustedRed = color.getRed() - 10;
                    int adjustedGreen = color.getGreen() + 40;
                    return new Color(adjustedRed, adjustedGreen, adjustedBlue).getRGB();
                }
                return -1;
            }
            if (lightReader != null && blockPos != null) {
                Color color = new Color(BiomeColors.getGrassColor(lightReader, blockPos));
                int adjustedBlue = color.getBlue() + 50;
                int adjustedRed = color.getRed() - 10;
                int adjustedGreen = color.getGreen() + 40;
                return new Color(adjustedRed, adjustedGreen, adjustedBlue).getRGB();
            }
            return -1;
        }, LARGE_ETHEREAL_FERN.get(), TALL_ETHEREAL_GRASS.get());
        blockColors.register((blockState, lightReader, blockPos, i) -> lightReader != null && blockPos != null ? BiomeColors.getGrassColor(lightReader, blockPos) : GrassColors.get(0.5D, 1.0D), ETHEREAL_GRASS.get(), ETHEREAL_FERN.get());
    }
}
