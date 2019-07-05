package com.alan199921.astral.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;

public class AstralMeridian extends Block {

    /**
     * Plane IntegerProperty:
     * 0 = XY Plane
     * 1 = YZ Plane
     * 2 = XZ Plane
     */
    public static final IntegerProperty PLANE = IntegerProperty.create("plane", 0, 2);

    public AstralMeridian() {
        super(Properties.create(Material.PORTAL)
        .hardnessAndResistance(99F));

        setRegistryName("astral_meridian");
    }
}
