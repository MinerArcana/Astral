package com.alan19.astral.util;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class RenderingUtils {
    private static boolean reloadRenderers = true;

    public static boolean shouldReloadRenderers() {
        return reloadRenderers;
    }

    public static void setReloadRenderers(boolean reloadRenderers) {
        RenderingUtils.reloadRenderers = reloadRenderers;
    }

    @OnlyIn(Dist.CLIENT)
    public static void reloadRenderers() {
        Minecraft.getInstance().worldRenderer.loadRenderers();
    }
}
