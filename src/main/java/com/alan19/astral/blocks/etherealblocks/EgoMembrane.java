package com.alan19.astral.blocks.etherealblocks;

import com.alan19.astral.blocks.AstralBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.PushReaction;

import javax.annotation.ParametersAreNonnullByDefault;

public class EgoMembrane extends Block implements Ethereal {
    public EgoMembrane() {
        super(Properties.of(Material.EGG)
                .strength(-1.0F, 3600000.0F)
                .noDrops()
                .lightLevel(value -> 14));
    }

    @Override
    @ParametersAreNonnullByDefault
    public void playerWillDestroy(Level worldIn, BlockPos pos, BlockState state, Player player) {
        worldIn.setBlock(pos, AstralBlocks.EGO_MEMBRANE.get().defaultBlockState(), 2);
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.IGNORE;
    }
}
