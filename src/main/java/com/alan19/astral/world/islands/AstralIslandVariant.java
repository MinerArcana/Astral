package com.alan19.astral.world.islands;

import net.minecraft.util.IStringSerializable;

public enum AstralIslandVariant implements IStringSerializable {
    COMMA(0),
    QUESTION(1),
    INVERTED_PYRAMID(2),
    QUARTER(3);


    private final int index;

    AstralIslandVariant(int i) {
        index = i;
    }

    public int getIndex() {
        return index;
    }

    public static AstralIslandVariant getVariantFromIndex(int variant) {
        switch (variant) {
            case 1:
                return QUARTER;
            case 2:
                return INVERTED_PYRAMID;
            case 3:
                return QUESTION;
            default:
                return COMMA;
        }
    }

    @Override
    public String getSerializedName() {
        return "etheric_isle";
    }
}
