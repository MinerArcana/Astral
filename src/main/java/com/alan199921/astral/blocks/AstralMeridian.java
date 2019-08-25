package com.alan199921.astral.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;

public class AstralMeridian extends Block {

    /**
     * Direction IntegerProperty:
     * 0 = North
     * 1 = East
     * 2 = South
     * 3 = West
     */
    public static final IntegerProperty DIRECTION = IntegerProperty.create("direction", 0, 3);
    public static final BooleanProperty ASTRAL_BLOCK = BooleanProperty.create("astral_block");

    public AstralMeridian() {
        super(Properties.create(Material.PORTAL)
        .hardnessAndResistance(99F));

        this.setDefaultState(this.getStateContainer().getBaseState().with(DIRECTION, 0));
        setRegistryName("astral_meridian");
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder.add(DIRECTION));
    }
}
