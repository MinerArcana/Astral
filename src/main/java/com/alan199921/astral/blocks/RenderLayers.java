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
        RenderType cutoutMipped = RenderType.getCutoutMipped();
        RenderTypeLookup.setRenderLayer(FEVERWEED_BLOCK.get(), cutoutMipped);
        RenderTypeLookup.setRenderLayer(SNOWBERRY_BUSH.get(), cutoutMipped);
        RenderTypeLookup.setRenderLayer(OFFERING_BRAZIER.get(), cutoutMipped);

    }
}
