package com.alan19.astral.blocks;

import com.alan19.astral.blocks.etherealblocks.EthericGrowth;
import com.alan19.astral.blocks.etherealblocks.TallEthericGrowth;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.RegistryObject;

import static com.alan19.astral.blocks.AstralBlocks.*;

@OnlyIn(Dist.CLIENT)
public class BlockRenderHandler {
    private BlockRenderHandler() {
        //Static function only
    }

    public static void setRenderLayers() {
        RenderType cutout = RenderType.cutout();
        ItemBlockRenderTypes.setRenderLayer(FEVERWEED_BLOCK.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(SNOWBERRY_BUSH.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(OFFERING_BRAZIER.get(), cutout);
        BLOCKS.getEntries().stream().map(RegistryObject::get).filter(block -> block instanceof EthericGrowth || block instanceof TallEthericGrowth).forEach(block -> ItemBlockRenderTypes.setRenderLayer(block, cutout));
        ItemBlockRenderTypes.setRenderLayer(COMFORTABLE_CUSHION.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ETHEREAL_SAPLING.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(METAPHORIC_FLESH_BLOCK.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(CRYSTAL_WEB.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ETHERIC_POWDER.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ETHEREAL_SPAWNER.get(), cutout);
    }

    public static void setupBlockColors() {
        BlockColors blockColors = new BlockColors();
        blockColors.register((state, reader, pos, color) -> RedStoneWireBlock.getColorForPower(15), ETHERIC_POWDER.get());
    }
}
