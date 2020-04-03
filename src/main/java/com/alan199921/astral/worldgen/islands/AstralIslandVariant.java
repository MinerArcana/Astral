package com.alan199921.astral.worldgen.islands;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum AstralIslandVariant implements IStringSerializable {
    CRESCENT(0),
    FULL(1),
    GIBBOUS(2),
    QUARTER(3);


    private int index;

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
                return GIBBOUS;
            case 3:
                return FULL;
            default:
                return CRESCENT;
        }
    }

    @Override
    public String getName() {
        return this.toString().toLowerCase(Locale.US);
    }
}
