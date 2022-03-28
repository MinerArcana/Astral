package com.alan19.astral.blocks.etherealblocks;

import com.alan19.astral.blocks.AstralBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
    public void playerWillDestroy(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        worldIn.setBlock(pos, AstralBlocks.EGO_MEMBRANE.get().defaultBlockState(), 2);
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.IGNORE;
    }
}
