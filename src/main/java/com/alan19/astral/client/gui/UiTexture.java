package com.alan19.astral.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Based off https://github.com/Lazzzzz/tirphycraft-1.15.2/blob/master/src/main/java/laz/tirphycraft/client/draw/UiTexture.java
 */
public class UiTexture {
    private final ResourceLocation texture;
    private final float x;
    private final float y;

    public UiTexture(ResourceLocation texture, float x, float y) {
        this.texture = texture;
        this.x = x;
        this.y = y;
    }

    public void bindTexture() {
        Minecraft.getInstance().textureManager.bind(texture);
    }

    public IDrawable getFullArea() {
        return new Area(0, 0, 1, 1);
    }

    public IDrawable getArea(int x, int y, int w, int h) {
        return new Area((float) x / w, (float) y / h, w / this.x, h / this.y);

    }

    private class Area implements IDrawable {

        private final float v;
        private final float u;
        private final float dv;
        private final float du;

        public Area(float u, float v, float du, float dv) {
            this.v = v;
            this.u = u;
            this.du = du;
            this.dv = dv;
        }

        @Override
        public void drawPartial(double x, double y, double width, double height, float x1, float y1, float x2, float y2) {
            bindTexture();
            double xi = x + width * x1;
            double xf = x + width * x2;
            double yi = y + height * y1;
            double yf = y + height * y2;
            float ui = u + du * x1;
            float uf = u + du * x2;
            float vi = v + dv * y1;
            float vf = v + dv * y2;

            Tessellator tesselator = Tessellator.getInstance();
            BufferBuilder bufferBuilder = tesselator.getBuilder();
            bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            bufferBuilder.vertex(xi, yi, 0D).uv(ui, vi).endVertex();
            bufferBuilder.vertex(xi, yf, 0D).uv(ui, vf).endVertex();
            bufferBuilder.vertex(xf, yf, 0D).uv(uf, vf).endVertex();
            bufferBuilder.vertex(xf, yi, 0D).uv(uf, vi).endVertex();
            tesselator.end();

        }

    }
}
