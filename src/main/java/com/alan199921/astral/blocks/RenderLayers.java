package com.alan199921.astral.blocks;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.alan199921.astral.blocks.AstralBlocks.*;

@OnlyIn(Dist.CLIENT)
public class RenderLayers {
    private RenderLayers() {
        //Static function only
    }

    public static void setRenderLayers() {
        RenderType cutout = RenderType.getCutout();
        RenderTypeLookup.setRenderLayer(FEVERWEED_BLOCK.get(), cutout);
        RenderTypeLookup.setRenderLayer(SNOWBERRY_BUSH.get(), cutout);
        RenderTypeLookup.setRenderLayer(OFFERING_BRAZIER.get(), cutout);

    }
}
