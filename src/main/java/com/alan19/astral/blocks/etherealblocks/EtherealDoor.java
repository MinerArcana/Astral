package com.alan19.astral.blocks.etherealblocks;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nonnull;

public class EtherealDoor extends DoorBlock implements Ethereal {
    public EtherealDoor() {
        super(Block.Properties.create(Material.WOOD).hardnessAndResistance(3.0F).sound(SoundType.WOOD).notSolid());
    }

    @Nonnull
    @Override
    public BlockRenderType getRenderType(@Nonnull BlockState state) {
        return Ethereal.getRenderType(super.getRenderType(state));
    }

    @Override
    public boolean canEntityDestroy(BlockState state, IBlockReader world, BlockPos pos, Entity entity) {
        return Ethereal.canEntityDestroy(entity, super.canEntityDestroy(state, world, pos, entity));
    }

    @Nonnull
    @Override
    public VoxelShape getCollisionShape(@Nonnull BlockState state, @Nonnull IBlockReader worldIn, @Nonnull BlockPos pos, @Nonnull ISelectionContext context) {
        return Ethereal.getCollisionShape(context, this.blocksMovement ? super.getShape(state, worldIn, pos, context) : VoxelShapes.empty());
    }

    @Nonnull
    @Override
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull IBlockReader worldIn, @Nonnull BlockPos pos, @Nonnull ISelectionContext context) {
        return Ethereal.getShape(context, super.getShape(state, worldIn, pos, context));
    }

    @Override
    public int getOpacity(@Nonnull BlockState state, @Nonnull IBlockReader worldIn, @Nonnull BlockPos pos) {
        return Ethereal.getOpacity();
    }

    @Override
    public PushReaction getPushReaction(BlockState state) {
        return Ethereal.getPushReaction();
    }
}
