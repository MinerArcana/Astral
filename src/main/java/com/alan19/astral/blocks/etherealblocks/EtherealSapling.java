package com.alan19.astral.blocks.etherealblocks;

import com.alan19.astral.tags.AstralTags;
import com.alan19.astral.world.trees.EtherealTree;
import net.minecraft.block.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class EtherealSapling extends SaplingBlock implements Ethereal {
    public EtherealSapling() {
        super(new EtherealTree(), BlockBehaviour.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0f).sound(SoundType.GRASS).noOcclusion());
    }

    @Nonnull
    @Override
    public RenderShape getRenderShape(@Nonnull BlockState state) {
        return Ethereal.getRenderType(super.getRenderShape(state));
    }

    @Override
    public boolean canEntityDestroy(BlockState state, BlockGetter world, BlockPos pos, Entity entity) {
        return Ethereal.canEntityDestroy(entity, super.canEntityDestroy(state, world, pos, entity));
    }

    @Nonnull
    @Override
    public VoxelShape getCollisionShape(@Nonnull BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return Ethereal.getCollisionShape(context, super.getCollisionShape(state, worldIn, pos, context));
    }

    @OnlyIn(Dist.CLIENT)
    @Nonnull
    @Override
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return Ethereal.getShape(super.getShape(state, worldIn, pos, context));
    }

    @Override
    public int getLightBlock(@Nonnull BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos) {
        return Ethereal.getOpacity();
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos) {
        return AstralTags.ETHEREAL_VEGETATION_PLANTABLE_ON.contains(state.getBlock());
    }

    @Override
    public PushReaction getPistonPushReaction(@Nonnull BlockState state) {
        return Ethereal.getPushReaction();
    }
}
