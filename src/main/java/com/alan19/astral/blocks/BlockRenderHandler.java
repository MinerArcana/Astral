package com.alan19.astral.blocks;

import com.alan19.astral.blocks.etherealblocks.EthericGrowth;
import com.alan19.astral.blocks.etherealblocks.TallEthericGrowth;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.RegistryObject;

import static com.alan19.astral.blocks.AstralBlocks.*;

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
        BLOCKS.getEntries().stream().map(RegistryObject::get).filter(block -> block instanceof EthericGrowth || block instanceof TallEthericGrowth).forEach(block -> RenderTypeLookup.setRenderLayer(block, cutout));
        RenderTypeLookup.setRenderLayer(COMFORTABLE_CUSHION.get(), cutout);
        RenderTypeLookup.setRenderLayer(ETHEREAL_SAPLING.get(), cutout);
        RenderTypeLookup.setRenderLayer(METAPHORIC_FLESH_BLOCK.get(), cutout);
        RenderTypeLookup.setRenderLayer(CRYSTAL_WEB.get(), cutout);
        RenderTypeLookup.setRenderLayer(ETHERIC_POWDER.get(), cutout);
        RenderTypeLookup.setRenderLayer(ETHEREAL_SPAWNER.get(), cutout);
    }

    public static void setupBlockColors() {
        BlockColors blockColors = new BlockColors();
        blockColors.register((state, reader, pos, color) -> RedstoneWireBlock.getRGBByPower(15), ETHERIC_POWDER.get());
    }
}
