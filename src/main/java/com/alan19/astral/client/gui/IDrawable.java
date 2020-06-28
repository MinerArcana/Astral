package com.alan19.astral.client.gui;

/**
 * Based off https://github.com/Lazzzzz/tirphycraft-1.15.2/blob/master/src/main/java/laz/tirphycraft/util/interf/IDrawable.java
 */
public interface IDrawable {
    default void draw(double x, double y, double width, double height) {
        drawPartial(x, y, width, height, 0, 0, 1, 1);
    }

    void drawPartial(double x, double y, double width, double height, float x1, float y1, float x2, float y2);
}
