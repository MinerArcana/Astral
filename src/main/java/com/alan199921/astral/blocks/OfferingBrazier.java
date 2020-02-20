package com.alan199921.astral.blocks;

import com.alan199921.astral.blocks.tileentities.OfferingBrazierTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class OfferingBrazier extends Block {
    public OfferingBrazier() {
        super(Block.Properties.create(Material.ROCK).hardnessAndResistance(2f)
                .lightValue(14));
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new OfferingBrazierTile();
    }

    @Override
    public boolean onBlockActivated(BlockState blockState, World world, BlockPos blockPos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult blockRayTraceResult) {
        TileEntity entity = world.getTileEntity(blockPos);
        if (!playerEntity.isSneaking() && entity instanceof OfferingBrazierTile) {
            ((OfferingBrazierTile) entity).extractInsertItem(playerEntity, hand);
        }
        return super.onBlockActivated(blockState, world, blockPos, playerEntity, hand, blockRayTraceResult);
    }
}
